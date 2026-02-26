package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.Map;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Estado;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.EstadoRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import br.com.foxinline.shared.util.CloneUtils;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;   
import io.quarkus.panache.common.Page; 



@ApplicationScoped
public class EstadoRepositoryImpl implements EstadoRepository, PanacheRepository<Estado> {

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Override
    public Uni<Estado> save(String nome, String uf, Credenciada credenciada, Usuario usuario) {
        Estado object = new Estado();

        if(nome != null && Utils.isNotEmpty(nome)){
            object.setNome(nome);
        }
        if(uf != null && Utils.isNotEmpty(uf)){
            object.setUf(uf);
        }
       
        return persist(object).onItem()
                .transformToUni(entidadePersistida -> auditoriaService.auditar(
                    null,
                    entidadePersistida,
                    "CREATE",
                    usuario.getNome(),
                    credenciada.getId())
                    .replaceWith(entidadePersistida));   
    
    }

    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String uf, Long idUsuario,
            int pageNumber, int pageSize, String sortString) {
            StringBuilder queryBuilder = new StringBuilder("from Estado e where e.ativo = true");
            Map<String, Object> params = new HashMap<>();

            if(nome != null && Utils.isNotEmpty(nome)){
                    queryBuilder.append(" and UPPER(e.nome) like UPPER(:nome)");
                    params.put("nome", "%" + nome.trim() + "%");
            }
            if(uf != null && Utils.isNotEmpty(uf)){
                    queryBuilder.append(" and UPPER(e.uf) like UPPER(:uf)");
                    params.put("uf", "%" + uf.trim() + "%");
            }
        
            if (idUsuario != null) {
                queryBuilder.append(" and e.credenciada.id = (select u.credenciada.id from Usuario u where u.id = :idUsuario)");
                params.put("idUsuario", idUsuario);
            }

            Sort sort = Sort.by("nome", Sort.Direction.Ascending);
                if (sortString != null && Utils.isNotEmpty(sortString)) {
                    String[] sortParams = sortString.split(",");
                    if (sortParams.length == 2) {
                        String field = sortParams[0].trim();
                        Sort.Direction direction = "asc".equalsIgnoreCase(sortParams[1].trim()) ? Sort.Direction.Ascending
                                : Sort.Direction.Descending;
                        sort = Sort.by(field, direction);
                    }
                }

                PanacheQuery<Estado> baseQuery = find(queryBuilder.toString(), sort, params);
                PanacheQuery<Estado> countQuery = find(queryBuilder.toString(), params);

                return countQuery.count()
                        .flatMap(count -> {
                            if (count == 0) {
                                return Uni.createFrom().item(new PaginatedSituacaoResult(List.of(), 0L));
                            }
                            return baseQuery.page(Page.of(pageNumber, pageSize)).list()
                                    .onItem().transform(listResult -> new PaginatedSituacaoResult(listResult, count));
                        }
                        );

    }
    
    @Override
    public Uni<Estado> findByUf(String uf) {
        return find("uf", uf).firstResult();
    }
    @Override
    public Uni<Estado> findByIdAtivo(Long idEstado) {
        return find("id = :id and ativo = true", Parameters.with("id", idEstado)).firstResult();
    }
    @Override
    public Uni<Void> remove(Long statusId, Usuario usuario) {
        return findById(statusId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException(
                    "Nenhuma Entidade com id:" + statusId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga -> {

                    Estado cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            Estado.class);
                    entidadeAntiga.setAtivo(false);

                    return persist(entidadeAntiga)
                            .onItem().transformToUni(entidadeAtualizada -> auditoriaService.auditar(
                                cloneEntidadeAntiga,
                                entidadeAtualizada,
                                "UPDATE", 
                                usuario.getNome(), 
                                usuario.getCredenciada().getId()))
                            .replaceWithVoid();
                    
                });
    }

    @Override
    public Uni<Estado> update(Long idEstado, String nome, String uf, Credenciada credenciada, Usuario usuario) {
        return findById(idEstado)
                .onItem().ifNull().failWith(() -> new NotFoundException("Estado não encontrado"))
                .onItem().ifNotNull().transformToUni(entidadeAntiga ->{
                    Estado cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga, Estado.class);
                    entidadeAntiga.setNome(nome);
                    entidadeAntiga.setUf(uf);
                    return persist(entidadeAntiga).onItem()
                        .transformToUni(entidadeAtualizada ->auditoriaService.auditar(cloneEntidadeAntiga, entidadeAtualizada,"UPDATE", usuario.getNome(), usuario.getCredenciada().getId()).replaceWith(entidadeAtualizada));
                });
    }
    
    
}