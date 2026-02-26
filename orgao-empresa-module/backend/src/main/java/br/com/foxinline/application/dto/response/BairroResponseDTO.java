package br.com.foxinline.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter

@ToString
public class BairroResponseDTO {
    private Long id;
    private String nome;
    private Long idCidade;
    private String nomeCidade;
    private String nomeEstado;
    private String ufEstado;

    public BairroResponseDTO(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }
    public BairroResponseDTO(){
    }

    
 
}
