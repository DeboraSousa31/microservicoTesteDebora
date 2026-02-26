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
public class TipoComodoRequestDTO {
    private Long id;
    private String nomeSingular;
    private String nomePlural;
    private Long idUsuario;
    private Long idCredenciadaTipoComodo;
    private int page;
    private int size;
    private String sort;
    
}
