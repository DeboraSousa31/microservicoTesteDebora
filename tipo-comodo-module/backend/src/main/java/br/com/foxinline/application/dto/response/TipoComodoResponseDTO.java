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
public class TipoComodoResponseDTO {
    private Long id;
    private String nomeSingular;
    private String nomePlural;
    private Long idCredenciada;
    private String nomeCredenciada;
}
