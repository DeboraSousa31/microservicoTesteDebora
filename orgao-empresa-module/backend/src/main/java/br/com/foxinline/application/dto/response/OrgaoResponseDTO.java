package br.com.foxinline.application.dto.response;

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
public class OrgaoResponseDTO {

    private Long id;
    private String nome;
    private Long idUsuario;
    private String email;
    private String telefone;
    private String cnpj;
    private String cpf;
    private String dataNascimento;
    private String tipoDocumento;
    private String razaoSocial;
    private String nomeResponsavel;
    private String nomeCredenciadaOrgao;
    private String nomeTipoOrgao;
    private Long idCredenciadaOrgao;
    // private String cep;
    // private String logradouro;
    // private String numero;
    // private String complemento;
    // private Long idBairro;
    // private String tipoLogradouro;
    

    private String codigoIBGEServentia;
    private String codigoServentia;
    private String oficioServentia;
    private String nomeTabeliao;

    private EnderecoResponseDTO endereco;
    private TipoOrgaoResponseDTO tipoOrgao;


}
