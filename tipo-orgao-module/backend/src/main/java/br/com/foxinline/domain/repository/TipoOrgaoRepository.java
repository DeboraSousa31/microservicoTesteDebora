package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedTipoOrgaoResult;
import io.smallrye.mutiny.Uni;

public interface TipoOrgaoRepository {
        public Uni<TipoOrgao> save(String nome, String descricao, Credenciada credenciada, Usuario usuario);

        public Uni<Void> remove(Long tipoOrgaoId, Usuario usuario);

        public Uni<TipoOrgao> findByIdAtivo(Long tipoOrgaoId);

        public Uni<TipoOrgao> update(Long tipoOrgaoId, String nome, String descricao,
                        Credenciada credenciada, Usuario usuario);

        public Uni<PaginatedTipoOrgaoResult> pesquisaPaginada(String nome, String descricao, Long idCredenciadaUsuario,
                        Long idCredenciadaFiltroBusca,
                        int pageNumber, int pageSize, String sortString);
        
}