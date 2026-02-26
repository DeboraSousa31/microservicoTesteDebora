package br.com.foxinline.domain.repository;

import java.math.BigDecimal;

import br.com.foxinline.domain.model.Categoria;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Status;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface StatusRepository {
    public Uni<Status> save(String nome,Long posicao, BigDecimal pontuacaoDaAtividade, String descricao,String cor, String corMapa,String legenda,Categoria categoria, Credenciada credenciada, Usuario usuario);
    
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String descricao, Long idUsuario, Long idCategoria,
                        Long idCredenciadaStatus,
                        int pageNumber, int pageSize, String sortString);
    public Uni<Status> update(Long idStatus,String nome,Long posicao, BigDecimal pontuacaoDaAtividade, String descricao,String cor, String corMapa,String legenda,Categoria categoria, Credenciada credenciada, Usuario usuario);


    public Uni<Status> findByIdAtivo(Long situacaoJuridicaId);
    public Uni<Void> remove(Long statusId, Usuario usuario);
        
}

