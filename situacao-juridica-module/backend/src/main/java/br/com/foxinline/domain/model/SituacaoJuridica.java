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
import lombok.ToString;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class SituacaoJuridica extends EntidadeGenerica{
    

    @Id
    @SequenceGenerator(sequenceName = "seq_situacaoJuridica", name = "seq_situacaoJuridica", allocationSize = 1)
    @GeneratedValue(generator = "seq_situacaoJuridica", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nome;
    private String descricao;
}
