package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.SituacaoJuridica;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.SituacaoJuridicaRepository;
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
public class SituacaoJuridicaRepositoryImpl implements SituacaoJuridicaRepository, PanacheRepository<SituacaoJuridica>{

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper ObjectMapper;


    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String descricao, Long idUsuario,
            Long idCredenciadaSituacaoJuridica, int pageNumber, int pageSize, String sortString) {

        StringBuilder queryBuilder = new StringBuilder("from SituacaoJuridica s where s.ativo = true");
        Map<String, Object> params = new HashMap<>();


        if (nome != null && Utils.isNotEmpty(nome)) {
            queryBuilder.append(" and UPPER(s.nome) like UPPER(:nome)");
            params.put("nome", "%" + nome.trim() + "%");
        }

        if (descricao != null && Utils.isNotEmpty(descricao)) {
            queryBuilder.append(" and UPPER(s.descricao) like UPPER(:descricao)");
            params.put("descricao", "%" + descricao.trim() + "%");
        }

        if (idUsuario != null) {
            queryBuilder.append(" and s.credenciada.id = (select u.credenciada.id from Usuario u where u.id = :idUsuario)");
            params.put("idUsuario", idUsuario);
        }
        
        if (idCredenciadaSituacaoJuridica != null) {
            queryBuilder.append(" and s.credenciada.id = :idCredenciadaSituacaoJuridica");
            params.put("idCredenciadaSituacaoJuridica", idCredenciadaSituacaoJuridica);
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

        PanacheQuery<SituacaoJuridica> baseQuery = find(queryBuilder.toString(), sort, params);
        PanacheQuery<SituacaoJuridica> countQuery = find(queryBuilder.toString(), params);

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
    public Uni<SituacaoJuridica> save(String nome, String descricao, Credenciada credenciada, Usuario usuario) {
        SituacaoJuridica object = new SituacaoJuridica();

        if(nome != null && Utils.isNotEmpty(nome)){
            object.setNome(nome);
        }

        if(descricao != null && Utils.isNotEmpty(descricao)){
            object.setDescricao(descricao);
        }

        if (credenciada != null) {
            object.setCredenciada(credenciada);            
        }else{
            if(usuario.getCredenciada() != null){
                object.setCredenciada(usuario.getCredenciada());
            }
        }

        System.out.println("### DEBUG ANTES DE PERSISTIR ### ID DO OBJETO: " + object);
        return persist(object).onItem()
                .transformToUni(entidadePersistida -> auditoriaService.auditar(
                    null,
                    entidadePersistida,
                    "CREATE",
                    usuario.getNome(),
                    entidadePersistida.getCredenciada().getId())
                    .replaceWith(entidadePersistida));   
    }
    
    @Override
    public Uni<SituacaoJuridica> findByIdAtivo(Long situacaoJuridicaId) {
        return find("id = :id and ativo = true", Parameters.with("id", situacaoJuridicaId)).firstResult();
    }

    @Override
    public Uni<Void> remove(Long situacaoJuridicaId, Usuario usuario) {
        return findById(situacaoJuridicaId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException(
                    "Nenhuma Entidade com id:" + situacaoJuridicaId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga -> {

                    SituacaoJuridica cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            SituacaoJuridica.class);
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
    public Uni<SituacaoJuridica> update(Long situacaoJuridicaId, String nome, String descricao, Credenciada credenciada,
            Usuario usuario) {
        return findByIdAtivo(situacaoJuridicaId)
                .onItem().ifNull().failWith(() -> new NotFoundException("Situação Juridica não encontrada"))
                .onItem().ifNotNull().transformToUni(entidadeAntiga -> {

                    SituacaoJuridica cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga, SituacaoJuridica.class);

                    entidadeAntiga.setNome(nome);
                    entidadeAntiga.setDescricao(descricao);
                    entidadeAntiga.setCredenciada(credenciada);

                    return persist(entidadeAntiga).onItem()
                            .transformToUni(entidadeAtualizada -> auditoriaService.auditar(
                                cloneEntidadeAntiga,
                                entidadeAtualizada, 
                                "UPDATE", 
                                usuario.getNome(),
                                credenciada.getId())
                                .replaceWith(entidadeAtualizada));
                });
    }
    
}
