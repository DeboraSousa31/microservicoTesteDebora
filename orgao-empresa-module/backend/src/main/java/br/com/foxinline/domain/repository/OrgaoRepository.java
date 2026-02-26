package br.com.foxinline.domain.repository;

import java.sql.Date;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Orgao;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import io.smallrye.mutiny.Uni;

public interface OrgaoRepository {

    
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(
        String nome, 
        String nomeResponsavel, 
        Long cidade, 
        Long tipoOrgao, 
        Long idCredenciadaFiltroBusca, 
        String codigoIBGEServentia, 
        String oficioServentia, 
        String codigoServentia, 
        String nomeTabeliao,
        Long idUsuario,
        int pageNumber, 
        int pageSize, 
        String sortString);

    public Uni<Orgao> findByIdAtivo(Long orgaoId);

    public Uni<Void> remove(Long idOrgao, Usuario usuario);

    public Uni<Orgao> save(
        String nome, 
        String email, 
        String telefone, 
        String cnpj, 
        String cpf, 
        String dataNascimento,
        String tipoDocumento, 
        String razaoSocial, 
        String nomeResponsavel, 
        Credenciada credenciada, 
        TipoOrgao tipoOrgao, 
        Usuario usuario, 
        Endereco endereco); 

    public Uni<Orgao> update(  
        Long id, 
        String nome,
        String email,
        String telefone,
        String cnpj,
        String cpf,
        String dataNascimento,
        String tipoDocumento,
        String razaoSocial,
        String nomeResponsavel,
        String nomeTabeliao,
        String codigoIBGEServentia,
        String codigoServentia,
        String oficioServentia,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String tipoLogradouro,
        Bairro bairro,
        Cidade cidade,
        Credenciada credenciada,
        TipoOrgao tipoOrgao,
        Usuario usuario);

 

 
    
} 