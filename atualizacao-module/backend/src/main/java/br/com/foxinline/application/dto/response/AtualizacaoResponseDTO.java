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
public class AtualizacaoResponseDTO {
    private Long id;
    private String descricao;
    private String dataAgendamento;
    private String textoNovidade;
    private Long idCredenciadaAtualizacao;
    private String nomeCredenciadaAtualizacao;
    private String pendente;
}
