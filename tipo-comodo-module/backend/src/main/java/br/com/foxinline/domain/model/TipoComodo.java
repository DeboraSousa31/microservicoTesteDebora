package br.com.foxinline.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoComodo extends EntidadeGenerica {
    @Id
    @SequenceGenerator(sequenceName = "seq_tipoComado", name = "seq_tipoComado", allocationSize = 1)
    @GeneratedValue(generator = "seq_tipoComado", strategy = GenerationType.SEQUENCE)
    private Long id;
    
    
    private String nomeSingular;
    

    private String nomePlural;
    

    
   
}
