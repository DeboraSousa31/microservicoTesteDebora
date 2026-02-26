package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.SituacaoJuridica;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface SituacaoJuridicaRepository {
    public Uni<SituacaoJuridica> save(String nome, String descricao, Credenciada Credenciada, Usuario usuario);
    
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String descricao, Long idUsuario,
                        Long idCredenciadaFiltroBusca,
                        int pageNumber, int pageSize, String sortString);
    public Uni<SituacaoJuridica> update(Long situacaoJuridicaId, String nome, String descricao,
                        Credenciada credenciada, Usuario usuario);


    public Uni<SituacaoJuridica> findByIdAtivo(Long situacaoJuridicaId);
    public Uni<Void> remove(Long situacaoProprietarioId, Usuario usuario);
        
}

