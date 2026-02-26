package br.com.foxinline.infrastructure.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.domain.model.Categoria;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Status;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.StatusRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import br.com.foxinline.shared.util.CloneUtils;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
public class StatusRepositoryImpl implements StatusRepository, PanacheRepository<Status>{

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper ObjectMapper;
    
    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String descricao, Long idUsuario,
            Long idCategoria, Long idCredenciadaStatus, int pageNumber, int pageSize, String sortString) {
        

        StringBuilder queryBuilder = new StringBuilder("from Status s left join fetch s.categoria c where s.ativo = true");
        Map<String, Object> params = new HashMap<>();
   


        if (nome != null && Utils.isNotEmpty(nome)) {
            queryBuilder.append(" and UPPER(s.nome) like UPPER(:nome)");
            params.put("nome", "%" + nome.trim() + "%");
        }

        if (descricao != null && Utils.isNotEmpty(descricao)) {
            queryBuilder.append(" and UPPER(s.descricao) like UPPER(:descricao)");
            params.put("descricao", "%" + descricao.trim() + "%");
        }
        if (idCredenciadaStatus != null) {
            queryBuilder.append(" and s.credenciada.id = :idCredenciadaStatus");
            params.put("idCredenciadaStatus", idCredenciadaStatus);
        }
        if (idCategoria != null) {
            queryBuilder.append(" and s.categoria.id = :idCategoria");
            params.put("idCategoria", idCategoria);
        }

         if (idUsuario != null) {
            queryBuilder.append(" and s.credenciada.id = (select u.credenciada.id from Usuario u where u.id = :idUsuario)");
            params.put("idUsuario", idUsuario);
        }
        Sort sort = Sort.by("s.nome", Sort.Direction.Ascending);
        if (sortString != null && Utils.isNotEmpty(sortString)) {
            String[] sortParams = sortString.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0].trim();
                if (field.equals("nome") || field.equals("id") || field.equals("descricao")) {
                    field = "s." + field;
                }
        
                Sort.Direction direction = "asc".equalsIgnoreCase(sortParams[1].trim()) ? Sort.Direction.Ascending
                        : Sort.Direction.Descending;
                sort = Sort.by(field, direction);
            }
        }

        PanacheQuery<Status> baseQuery = find(queryBuilder.toString(), sort, params);
        PanacheQuery<Status> countQuery = find(queryBuilder.toString(), params);

        

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
    public Uni<Status> findByIdAtivo(Long statusId) {
       
        return find("select s from Status s left join fetch s.categoria c where s.id = :id and s.ativo = true", Parameters.with("id", statusId)).firstResult();
    }

    @Override
    public Uni<Void> remove(Long statusId, Usuario usuario) {
        return findById(statusId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException(
                    "Nenhuma Entidade com id:" + statusId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga -> {

                    Status cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            Status.class);
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
    public Uni<Status> update(Long statusId,String nome,Long posicao, BigDecimal pontuacaoDaAtividade, String descricao,String corStatus, String corMapa,String legenda,Categoria categoria, Credenciada credenciada, Usuario usuario)
        {
        return findByIdAtivo(statusId)
                .onItem().ifNull().failWith(() -> new NotFoundException("Status não encontrada"))
                .onItem().ifNotNull().transformToUni(entidadeAntiga -> {

                    Status cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga, Status.class);

                    entidadeAntiga.setNome(nome);
                    entidadeAntiga.setDescricao(descricao);
                    entidadeAntiga.setPosicao(posicao);
                    entidadeAntiga.setPontuacaoDaAtividade(pontuacaoDaAtividade);
                    entidadeAntiga.setCor(corStatus);
                    entidadeAntiga.setCorMapa(corMapa);
                    entidadeAntiga.setLegenda(legenda);
                    entidadeAntiga.setCategoria(categoria);
                    entidadeAntiga.setCredenciada(credenciada);

                    return persist(entidadeAntiga).onItem()
                            .transformToUni(entidadeAtualizada -> auditoriaService.auditar(
                                cloneEntidadeAntiga,
                                entidadeAtualizada, 
                                "UPDATE", 
                                usuario.getNome(),
                                usuario.getCredenciada().getId())
                                .replaceWith(entidadeAtualizada));
                });
    }


    @Override
    public Uni<Status> save(String nome, Long posicao, BigDecimal pontuacaoDaAtividade, String descricao, String corStatus,
            String corMapa, String legenda, Categoria categoria, Credenciada credenciada, Usuario usuario) {        
                Status object = new Status();
                if(nome != null && Utils.isNotEmpty(nome)){
                    object.setNome(nome);
                }

                if(descricao != null && Utils.isNotEmpty(descricao)){
                    object.setDescricao(descricao);
                }

                if(posicao != null && Utils.isNotEmpty(posicao)){
                    object.setPosicao(posicao);
                }

                if(pontuacaoDaAtividade != null && Utils.isNotEmpty(pontuacaoDaAtividade)){
                    object.setPontuacaoDaAtividade(pontuacaoDaAtividade);
                }

                if(corStatus != null && Utils.isNotEmpty(corStatus)){
                    object.setCor(corStatus);
                }
                if (corMapa != null && Utils.isNotEmpty(corMapa)){
                    object.setCorMapa(corMapa);
                }

                if(legenda != null && Utils.isNotEmpty(legenda)){
                    object.setLegenda(legenda);
                }

                if(categoria != null){
                    object.setCategoria(categoria);
                }

                object.setCredenciada(credenciada);
               
                return persist(object).onItem()
                        .transformToUni(entidadePersistida -> 
                                auditoriaService.auditar(
                                    null, 
                                    entidadePersistida, 
                                    "CREATE", 
                                    usuario.getNome(), 
                                    entidadePersistida.getCredenciada().getId())
                                    .replaceWith(entidadePersistida));
    }
    
}
