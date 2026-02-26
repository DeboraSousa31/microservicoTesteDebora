package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.SituacaoProprietario;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface SituacaoProprietarioRepository {
        public Uni<SituacaoProprietario> save(String nome, String descricao, Credenciada credenciada, Usuario usuario);

        public Uni<Void> remove(Long situacaoProprietarioId, Usuario usuario);

        public Uni<SituacaoProprietario> findByIdAtivo(Long situacaoProprietarioId);

        public Uni<SituacaoProprietario> update(Long situacaoProprietarioId, String nome, String descricao,
                        Credenciada credenciada, Usuario usuario);

        public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String descricao, Long idCredenciadaUsuario,
                        Long idCredenciadaFiltroBusca,
                        int pageNumber, int pageSize, String sortString);
        
}