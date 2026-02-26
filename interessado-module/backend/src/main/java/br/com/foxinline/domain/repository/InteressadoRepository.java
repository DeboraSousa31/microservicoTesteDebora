package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Interessado;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface InteressadoRepository {

    public Uni<PaginatedSituacaoResult> pesquisaPaginada(
        String nome, 
        String cpf, 
        Long idCredenciadaInteressado,
        String cnpj, 
        String email,
        String telefone,
        String orgaoEmissor,
        String razaoSocial,
        String nomeFantasia,
        String profissao,
        Long idUsuario,
        String dataNascimento,
        String tipoDocumento,
        int pageNumber, 
        int pageSize, 
        String sortString);

    public Uni<Interessado> save(
        String nome,
        String cpf,
        String cnpj,
        String email,
        String profissao,
        String telefone,
        String nomeFantasia,
        String razaoSocial,
        String orgaoEmissor,
        String dataNascimento,
        String rg,
        String tipoDocumento,
        Credenciada credenciada,
        Usuario usuario
    );
    public Uni<Interessado> update(
        Long id,
        String nome,
        String cpf,
        String cnpj,
        String email,
        String profissao,
        String telefone,
        String nomeFantasia,
        String razaoSocial,
        String orgaoEmissor,
        String dataNascimento,
        String rg,
        String tipoDocumento,
        Credenciada credenciada,
        Usuario usuario
    );
    public Uni<Interessado> findByIdAtivo(Long idInteressado);
    public Uni<Void> remove(Long interessadoId, Usuario usuario);
}
