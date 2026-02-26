package br.com.foxinline.infrastructure.repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.domain.model.Atualizacao;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.AtualizacaoRepository;
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
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AtualizacaoRepositoryImpl implements AtualizacaoRepository, PanacheRepository<Atualizacao>{

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String descricao, Long idUsuario, String dataAgendamento,
            int pageNumber, int pageSize, String sortString) {
            StringBuilder queryBuilder = new StringBuilder("from Atualizacao a where a.ativo = true");
            Map<String, Object> params = new HashMap<>();

            if(descricao != null && Utils.isNotEmpty(descricao)){
                    queryBuilder.append(" and UPPER(a.descricao) like UPPER(:descricao)");
                    params.put("descricao", "%" + descricao.trim() + "%");
            }
         
            if (dataAgendamento != null && Utils.isNotEmpty(dataAgendamento)) {
                try {
                    LocalDate localDate = LocalDate.parse(dataAgendamento, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    
                    queryBuilder.append(" and function('DATE', a.dataAgendamento) = :dataAgendamento");
                    params.put("dataAgendamento", localDate);

                } catch (java.time.format.DateTimeParseException e) {
                    System.err.println("Formato de data inválido, filtro de data ignorado: " + dataAgendamento);
                }
            }
            if (idUsuario != null) {
                queryBuilder.append(" and a.credenciada.id = (select u.credenciada.id from Usuario u where u.id = :idUsuario)");
                params.put("idUsuario", idUsuario);
            }

            Sort sort = Sort.by("dataCriacao", Sort.Direction.Descending);
                if (sortString != null && Utils.isNotEmpty(sortString)) {
                    String[] sortParams = sortString.split(",");
                    if (sortParams.length == 2) {
                        String field = sortParams[0].trim();
                        Sort.Direction direction = "asc".equalsIgnoreCase(sortParams[1].trim()) ? Sort.Direction.Ascending
                                : Sort.Direction.Descending;
                        sort = Sort.by(field, direction);
                    }
                }

                PanacheQuery<Atualizacao> baseQuery = find(queryBuilder.toString(), sort, params);
                PanacheQuery<Atualizacao> countQuery = find(queryBuilder.toString(), params);

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
    public Uni<Atualizacao> findByIdAtivo(Long atualizacaoId) {
        return find("id = :id and ativo = true", Parameters.with("id", atualizacaoId)).firstResult();
    }

    @Override
    public Uni<Atualizacao> save(String descricao, String dataAgendamento, String textoNovidade) {
        Atualizacao object = new Atualizacao();
        if(dataAgendamento != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dataAgendamento, formatter);
            Date data = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            object.setDataAgendamento(data);
        }
        object.setDescricao(descricao);
        object.setTextoNovidade(textoNovidade);
        System.out.println(object);
        return persist(object);
    }

    @Override
    public Uni<Atualizacao> remove(Long atualizacaoId) {
        return findById(atualizacaoId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException( "Nenhuma Entidade com id:" + atualizacaoId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga ->{
                    entidadeAntiga.setAtivo(false);
                    return persist(entidadeAntiga);
                });
    }

    @Override
    public Uni<Atualizacao> update(Long id, String descricao, String dataAgendamento, String textoNovidade) {
        return findById(id)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException( "Nenhuma Entidade com id:" + id + " encontrada."))
                .onItem().transformToUni(entidadeAntiga ->{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    LocalDateTime localDateTime = LocalDateTime.parse(dataAgendamento, formatter);
                    Date data = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    entidadeAntiga.setDataAgendamento(data);
                    entidadeAntiga.setDescricao(descricao);
                    entidadeAntiga.setTextoNovidade(textoNovidade);
                    return persist(entidadeAntiga);
                });
    }

    @Override
    public Uni<Void> verificarEProcessarAgendamentos() {
       
        return find("ativo = true and pendente = true and dataAgendamento <= ?1", new Date())
                .list()
                .onItem().transformToUni(atualizacoesParaPublicar -> {
                    
                    if (atualizacoesParaPublicar == null || atualizacoesParaPublicar.isEmpty()) {
                        return Uni.createFrom().voidItem();
                    }

                    for (Atualizacao a : atualizacoesParaPublicar) {
                        a.setPendente(Boolean.FALSE);
                    }

                    return this.persist(atualizacoesParaPublicar);
                });
    }

   

    @Override
    public Uni<Void> resetarVisualizacaoAviso() {
        String hql = "UPDATE Usuario u SET u.avisoAtualizacaoVizualizado = false WHERE u.avisoAtualizacaoVizualizado = true";
        // 'update' é um método do PanacheRepository, disponível nesta classe.
        return this.update(hql).replaceWithVoid();
    }

}
