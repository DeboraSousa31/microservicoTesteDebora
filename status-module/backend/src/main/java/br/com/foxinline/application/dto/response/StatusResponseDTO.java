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
public class StatusResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Long idCredenciada;
    private String nomeCredenciada;
    private Long idCategoria;
    private String nomeCategoria;
    private String corMapa;
    private String cor;
    private String posicao;
    private String legenda;
  
}
