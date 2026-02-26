package br.com.foxinline.infrastructure.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.TipoComodo;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.TipoComodoRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import br.com.foxinline.shared.util.CloneUtils;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class TipoComodoRepositoryImpl implements TipoComodoRepository, PanacheRepository<TipoComodo>{

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nomeSingular, String nomePlural, Long idCredenciada,Long idUsuario,
            int pageNumber, int pageSize, String sortString) {
            StringBuilder queryBuilder = new StringBuilder("from TipoComodo c left join fetch c.credenciada cr where c.ativo = true");
            Map<String, Object> params = new HashMap<>();

            if(nomeSingular != null && Utils.isNotEmpty(nomeSingular)){
                    queryBuilder.append(" and UPPER(c.nomeSingular) like UPPER(:nomeSingular)");
                    params.put("nomeSingular", "%" + nomeSingular.trim() + "%");
            }
            if(nomePlural != null && Utils.isNotEmpty(nomePlural)){
                    queryBuilder.append(" and UPPER(c.nomePlural) like UPPER(:nomePlural)");
                    params.put("nomePlural", "%" + nomePlural.trim() + "%");
            }
         
            if(idCredenciada != null){
                queryBuilder.append(" and c.credenciada.id = :idCredenciada");
                params.put("idCredenciada", idCredenciada);
            }
         
            if (idUsuario != null) {
                queryBuilder.append(" and c.credenciada.id = (select u.credenciada.id from Usuario u where u.id = :idUsuario)");
                params.put("idUsuario", idUsuario);
            }

            Sort sort = Sort.by("nomeSingular", Sort.Direction.Ascending);
        if (sortString != null && Utils.isNotEmpty(sortString)) {
            String[] sortParams = sortString.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0].trim();
                if (!field.contains(".")) {
                    field = "c." + field;
                }
                Sort.Direction direction = "asc".equalsIgnoreCase(sortParams[1].trim()) ? Sort.Direction.Ascending
                        : Sort.Direction.Descending;
                sort = Sort.by(field, direction);
            }
        }

        PanacheQuery<TipoComodo> baseQuery = find(queryBuilder.toString(), sort, params);
        PanacheQuery<TipoComodo> countQuery = find(queryBuilder.toString(), params);

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
    public Uni<TipoComodo> findByIdAtivo(Long atualizacaoId) {
        return find("id = :id and ativo = true", Parameters.with("id", atualizacaoId)).firstResult();
    }

    @Override
    public Uni<TipoComodo> save(String nomeSingular, String nomePlural, Credenciada credenciada, Usuario usuario) {
        TipoComodo object = new TipoComodo();
       
        if(nomeSingular != null && Utils.isNotEmpty(nomeSingular)){
            object.setNomeSingular(nomeSingular);
        }
        if(nomePlural != null && Utils.isNotEmpty(nomePlural)){
            object.setNomePlural(nomePlural);
        }
        if(credenciada != null){
            object.setCredenciada(credenciada);
        }
        
    
        //return persist(object).onItem()
        //         .transformToUni(entidadePersistida -> auditoriaService.auditar(
        //             null,
        //             entidadePersistida,
        //             "CREATE",
        //             usuario.getNome(),
        //             entidadePersistida.getCredenciada().getId()
        //             )
        //             .replaceWith(entidadePersistida));   

        return persist(object).onItem()
            .transformToUni(entidadePersistida -> {
       
        Long idCredenciadaParaAuditoria = (entidadePersistida.getCredenciada() != null) 
            ? entidadePersistida.getCredenciada().getId() 
            : usuario.getCredenciada().getId();

            return auditoriaService.auditar(
                null,
                entidadePersistida,
                "CREATE",
                usuario.getNome(),
                idCredenciadaParaAuditoria 
            )
            .replaceWith(entidadePersistida);
        });
    }

    @Override
    public Uni<Void> remove(Long tipoComodoId, Usuario usuario) {
         return findById(tipoComodoId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException(
                    "Nenhuma Entidade com id:" + tipoComodoId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga -> {

                    TipoComodo cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            TipoComodo.class);
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
    public Uni<TipoComodo> update(Long id, String nomeSingular, String nomePlural, Credenciada credenciada, Usuario usuario) {
        return findByIdAtivo(id)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException( "Nenhuma Entidade (Tipo Cômodo) com id:" + id + " encontrada."))
                .onItem().ifNotNull().transformToUni(entidadeAntiga ->{

                    TipoComodo cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga, TipoComodo.class);

                    entidadeAntiga.setNomePlural(nomePlural);
                    entidadeAntiga.setNomeSingular(nomeSingular);
                    entidadeAntiga.setCredenciada(credenciada);

                    return persist(entidadeAntiga).onItem()
                        .transformToUni(entidadeAtualizada -> {
                
                    Long idCredenciadaParaAuditoria = (entidadeAtualizada.getCredenciada() != null) 
                        ? entidadeAtualizada.getCredenciada().getId() 
                        : usuario.getCredenciada().getId();

                        return auditoriaService.auditar(
                            cloneEntidadeAntiga,
                            entidadeAtualizada,
                            "UPDATE",
                            usuario.getNome(),
                            idCredenciadaParaAuditoria 
                        )
                        .replaceWith(entidadeAtualizada);
                    });


                    
        });
    }


}
