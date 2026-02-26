package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedTipoOrgaoResult;
import br.com.foxinline.domain.repository.TipoOrgaoRepository;
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
public class TipoOrgaoRepositoryImpl
        implements TipoOrgaoRepository, PanacheRepository<TipoOrgao> {

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Uni<PaginatedTipoOrgaoResult> pesquisaPaginada(String nome, String descricao, Long idCredenciadaUsuario,
            Long idCredenciadaTipoOrgao, int pageNumber, int pageSize, String sortString) {

        StringBuilder queryBuilder = new StringBuilder("from TipoOrgao to where to.ativo = true");
        Map<String, Object> params = new HashMap<>();

        if (nome != null && Utils.isNotEmpty(nome)) {
            queryBuilder.append(" and UPPER(to.nome) like UPPER(:nome)");
            params.put("nome", "%" + nome.trim() + "%");
        }

        if (descricao != null && Utils.isNotEmpty(descricao)) {
            queryBuilder.append(" and UPPER(to.descricao) like UPPER(:descricao)");
            params.put("descricao", "%" + descricao.trim() + "%");
        }

        if (idCredenciadaUsuario != null) {
            queryBuilder.append(" and to.credenciada.id = :credenciadaId");
            params.put("credenciadaId", idCredenciadaUsuario);
        }
        
        if (idCredenciadaTipoOrgao != null) {
            queryBuilder.append(" and to.credenciada.id = :idCredenciadaTipoOrgao");
            params.put("idCredenciadaTipoOrgao", idCredenciadaTipoOrgao);
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

        PanacheQuery<TipoOrgao> baseQuery = find(queryBuilder.toString(), sort, params);
        PanacheQuery<TipoOrgao> countQuery = find(queryBuilder.toString(), params);

        return countQuery.count()
                .flatMap(count -> {
                    if (count == 0) {
                        return Uni.createFrom().item(new PaginatedTipoOrgaoResult(List.of(), 0L));
                    }
                    return baseQuery.page(Page.of(pageNumber, pageSize)).list()
                            .onItem().transform(listResult -> new PaginatedTipoOrgaoResult(listResult, count));
                });
    }

    @Override
    public Uni<Void> remove(Long tipoOrgaoId, Usuario usuario) {
        return findById(tipoOrgaoId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException(
                        "Nenhuma Entidade com id: " + tipoOrgaoId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga -> {

                    TipoOrgao cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            TipoOrgao.class);

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
    public Uni<TipoOrgao> save(String nome, String descricao, Credenciada credenciada, Usuario usuario) {
        TipoOrgao object = new TipoOrgao();

        if (nome != null && Utils.isNotEmpty(nome)) {
            object.setNome(nome);
        }

        if (descricao != null && Utils.isNotEmpty(descricao)) {
            object.setDescricao(descricao);
        }

        if (credenciada != null) {
            object.setCredenciada(credenciada);
        } else {
            if (usuario.getCredenciada() != null) {
                object.setCredenciada(usuario.getCredenciada());
            }
        }

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
    public Uni<TipoOrgao> findByIdAtivo(Long tipoOrgaoId) {
        return find("id = :id and ativo = true", Parameters.with("id", tipoOrgaoId)).firstResult();
    }

    @Override
    public Uni<TipoOrgao> update(Long tipoOrgaoId, String nome, String descricao,
            Credenciada credenciada, Usuario usuario) {

        return findByIdAtivo(tipoOrgaoId)
                .onItem().ifNull().failWith(() -> new NotFoundException("Entidade Não encontrada"))
                .onItem().ifNotNull().transformToUni(entidadeAntiga -> {

                    TipoOrgao cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            TipoOrgao.class);

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
