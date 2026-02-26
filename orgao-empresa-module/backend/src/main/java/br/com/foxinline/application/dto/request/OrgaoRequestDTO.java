package br.com.foxinline.application.dto.request;


import java.sql.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrgaoRequestDTO{
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cnpj;
    private String cpf;
    
    private String dataNascimento;
    private String tipoDocumento;
    private String razaoSocial;
    private String nomeResponsavel;
    private Long idCredenciadaOrgao;
    private Long idTipoOrgao;

    private String codigoIBGEServentia;
    private String codigoServentia;
    private String oficioServentia;
    private String nomeTabeliao;
    
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private Long idBairro;
    private String tipoLogradouro;
    private Long idCidade;
    private Long idEstado;
    private String cidadeNome;
    private String bairroNome;
    private String estadoNome;
    private String estadoUf; 
    private String codigoIbge;
    private EnderecoRequestDTO endereco;
    
    private Long idUsuario;

    private int page;
    private int size;
    private String sort;
}  