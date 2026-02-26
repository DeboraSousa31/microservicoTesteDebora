package br.com.foxinline.application.dto.request;

import java.math.BigDecimal;

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
public class StatusRequestDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Long posicao;
    private BigDecimal pontuacaoDaAtividade;
    private String corStatus;
    private String corMapa;
    private String legenda;
    private Long idCategoria;
    private Long idUsuario;
    private Long idCredenciadaStatus;
    private int page;
    private int size;
    private String sort;
}
