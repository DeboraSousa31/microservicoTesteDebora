package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.SituacaoProprietario;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.SituacaoProprietarioRepository;
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
public class SituacaoProprietarioRepositoryImpl
        implements SituacaoProprietarioRepository, PanacheRepository<SituacaoProprietario> {

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String descricao, Long idCredenciadaUsuario,
            Long idCredenciadaSituacaoProprietario, int pageNumber, int pageSize, String sortString) {

        StringBuilder queryBuilder = new StringBuilder("from SituacaoProprietario s where s.ativo = true");
        Map<String, Object> params = new HashMap<>();

        if (nome != null && Utils.isNotEmpty(nome)) {
            queryBuilder.append(" and UPPER(s.nome) like UPPER(:nome)");
            params.put("nome", "%" + nome.trim() + "%");
        }

        if (descricao != null && Utils.isNotEmpty(descricao)) {
            queryBuilder.append(" and UPPER(s.descricao) like UPPER(:descricao)");
            params.put("descricao", "%" + descricao.trim() + "%");
        }

        if (idCredenciadaUsuario != null) {
            queryBuilder.append(" and s.credenciada.id = :credenciadaId");
            params.put("credenciadaId", idCredenciadaUsuario);
        }
        
        if (idCredenciadaSituacaoProprietario != null) {
            queryBuilder.append(" and s.credenciada.id = :idCredenciadaSituacaoProprietario");
            params.put("idCredenciadaSituacaoProprietario", idCredenciadaSituacaoProprietario);
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

        PanacheQuery<SituacaoProprietario> baseQuery = find(queryBuilder.toString(), sort, params);
        PanacheQuery<SituacaoProprietario> countQuery = find(queryBuilder.toString(), params);

        return countQuery.count()
                .flatMap(count -> {
                    if (count == 0) {
                        return Uni.createFrom().item(new PaginatedSituacaoResult(List.of(), 0L));
                    }
                    return baseQuery.page(Page.of(pageNumber, pageSize)).list()
                            .onItem().transform(listResult -> new PaginatedSituacaoResult(listResult, count));
                });
    }

    @Override
    public Uni<Void> remove(Long situacaoProprietarioId, Usuario usuario) {
        return findById(situacaoProprietarioId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException(
                        "Nenhuma Entidade com id: " + situacaoProprietarioId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga -> {

                    SituacaoProprietario cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            SituacaoProprietario.class);

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
    public Uni<SituacaoProprietario> save(String nome, String descricao, Credenciada credenciada, Usuario usuario) {
        SituacaoProprietario object = new SituacaoProprietario();

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
    public Uni<SituacaoProprietario> findByIdAtivo(Long situacaoProprietarioId) {
        return find("id = :id and ativo = true", Parameters.with("id", situacaoProprietarioId)).firstResult();
    }

    @Override
    public Uni<SituacaoProprietario> update(Long situacaoProprietarioId, String nome, String descricao,
            Credenciada credenciada, Usuario usuario) {

        return findByIdAtivo(situacaoProprietarioId)
                .onItem().ifNull().failWith(() -> new NotFoundException("Entidade Não encontrada"))
                .onItem().ifNotNull().transformToUni(entidadeAntiga -> {

                    SituacaoProprietario cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                            SituacaoProprietario.class);

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
