package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.TipoComodo;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface TipoComodoRepository {
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(
        String nomeSingular,     
        String nomePlural,
        Long idCredenciada,
        Long idUsuario,
        int pageNumber, 
        int pageSize, 
        String sortString);
    public Uni<TipoComodo> findByIdAtivo(Long tipocomodoId);

    public Uni<TipoComodo> save(String nomeSingular, String nomePlural, Credenciada credenciada, Usuario usuario);
    public Uni<Void> remove(Long tipoComodoId, Usuario usuario);
    public Uni<TipoComodo> update(Long id, String nomeSingular, String nomePlural, Credenciada credenciada, Usuario usuario);

} 