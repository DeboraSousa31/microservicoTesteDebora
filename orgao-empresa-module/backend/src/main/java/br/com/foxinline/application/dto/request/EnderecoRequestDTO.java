package br.com.foxinline.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnderecoRequestDTO {
    private Long id;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private Long idBairro;
    private Long idCidade;
    private String nomeCidade;
    private String tipoLogradouro;
    public String codigoIbge;
    public String bairroNome;
    public String nomeEstado;
    public String ufEstado;
}
