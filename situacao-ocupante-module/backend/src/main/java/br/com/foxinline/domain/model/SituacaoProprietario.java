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
public class SituacaoProprietario extends EntidadeGenerica {
    @Id
    @SequenceGenerator(sequenceName = "seq_situacaoOcupante", name = "seq_situacaoOcupante", allocationSize = 1)
    @GeneratedValue(generator = "seq_situacaoOcupante", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nome;
    private String descricao;
}