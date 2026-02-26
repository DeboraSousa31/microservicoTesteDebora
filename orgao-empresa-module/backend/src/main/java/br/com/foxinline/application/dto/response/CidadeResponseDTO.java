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
public class CidadeResponseDTO {
    private Long id;
    private String nome;
    private String codigoIbge;
    private String nomeEstado;
    private String ufEstado;

    public CidadeResponseDTO(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }
}
