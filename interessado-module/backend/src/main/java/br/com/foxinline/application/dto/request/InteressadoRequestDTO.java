package br.com.foxinline.application.dto.request;

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
public class InteressadoRequestDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String cnpj;
    private String rg;
    private String email;
    private String telefone;
    private String profissao;
    private String orgaoEmissor;
    private String dataNascimento;
    private String tipoDocumento;
    private String razaoSocial;
    private String nomeFantasia;
    private Long idUsuario;
    private Long idCredenciadaInteressado;
    private int page;
    private int size;
    private String sort;
}
