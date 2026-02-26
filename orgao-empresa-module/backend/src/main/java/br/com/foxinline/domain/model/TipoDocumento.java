package br.com.foxinline.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum TipoDocumento {

    CPF("Física", "CPF"),
    CNPJ("Jurídica", "CNPJ");

    private String nome;
    private String tipo;
    
     public static TipoDocumento valueString(String valor){
        for(TipoDocumento o : values()){
            if(o.getNome().toLowerCase().equals(valor.toLowerCase())){
                return o;
            }
        }
        return null;
    }

}
