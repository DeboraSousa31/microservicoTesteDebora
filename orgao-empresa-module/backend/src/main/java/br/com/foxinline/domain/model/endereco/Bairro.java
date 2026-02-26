package br.com.foxinline.domain.model.endereco;

import br.com.foxinline.domain.model.EntidadeGenerica;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Bairro extends EntidadeGenerica {

    @Id
    @SequenceGenerator(sequenceName = "seq_bairro", name = "seq_bairro", allocationSize = 50000)
    @GeneratedValue(generator = "seq_bairro", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

}
