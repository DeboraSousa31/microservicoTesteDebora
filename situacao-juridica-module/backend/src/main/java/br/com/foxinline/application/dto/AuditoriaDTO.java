package br.com.foxinline.application.dto;

import java.time.Instant;

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
public class AuditoriaDTO {
    private Long id_Entidade;
    private String acao;
    private String nomeEntidade;
    private String valorAntigo;
    private String campoAlterado;
    private String valorNovo;
    private String usuario;
    private Instant dataHora;
    private Long id_Credenciada;
}
