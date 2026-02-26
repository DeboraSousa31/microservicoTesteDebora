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
public class AtualizacaoRequestDTO {
    private Long id;
    private String descricao;
    private String dataAgendamento;
    private String textoNovidade;
    private Long idCredenciadaAtualizacao;
    private Long idUsuario;
    private int page;
    private int size;
    private String sort;
}
