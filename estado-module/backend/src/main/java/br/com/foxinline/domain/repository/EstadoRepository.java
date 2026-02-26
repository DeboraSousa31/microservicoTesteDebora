package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Estado;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface EstadoRepository {
        public Uni<PaginatedSituacaoResult> pesquisaPaginada(
        String nome,
        String uf,     
        Long idUsuario,
        int pageNumber, 
        int pageSize, 
        String sortString);
    Uni<Estado> save(String nome, String uf, Credenciada credenciada, Usuario usuario);
    Uni<Estado> findByUf(String uf);
    Uni<Estado> findByIdAtivo(Long estadoId);
    Uni<Void> remove(Long idEstado, Usuario usuario);
    Uni<Estado> update(Long idEstado, String nome, String uf, Credenciada credenciada ,Usuario usuario);
}
