package br.com.foxinline.domain.model.endereco;

import java.time.LocalDateTime;

import br.com.foxinline.domain.model.EntidadeGenerica;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;


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
@Entity
public class Estado extends EntidadeGenerica {
    
    @Id
    @SequenceGenerator(name = "seq_estado", sequenceName = "seq_estado", initialValue = 1)
    @GeneratedValue(generator = "seq_estado", strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private String nome;
    
    private String uf;

  
    
    
}

