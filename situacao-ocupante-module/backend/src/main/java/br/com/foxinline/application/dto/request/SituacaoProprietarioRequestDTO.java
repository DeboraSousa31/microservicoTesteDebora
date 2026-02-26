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
public class SituacaoProprietarioRequestDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Long idCredenciadaUsuario;
    private Long idUsuario;
    private Long idCredenciadaSituacaoProprietario;
    private int page; 
    private int size;  
    private String sort; 
}
