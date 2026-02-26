package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Atualizacao;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface AtualizacaoRepository {
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(
        String descricao,     
        Long idUsuario,
        String dataAgendamento,
        int pageNumber, 
        int pageSize, 
        String sortString);
    public Uni<Atualizacao> findByIdAtivo(Long atualizacaoId);

    public Uni<Atualizacao> save(String descricao, String dataAgendamento, String textoNovidade);
    public Uni<Atualizacao> remove(Long atualizacaoId);
    public Uni<Atualizacao> update(Long id, String descricao, String dataAgendamento, String textoNovidade);
    public Uni<Void> resetarVisualizacaoAviso();
    public Uni<Void> verificarEProcessarAgendamentos();
} 