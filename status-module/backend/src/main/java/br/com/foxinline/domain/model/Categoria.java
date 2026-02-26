package br.com.foxinline.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "categoria")
public class Categoria extends EntidadeGenerica {
    @Id
    @SequenceGenerator(sequenceName = "seq_categoria", name = "seq_categoria", allocationSize = 1)
    @GeneratedValue(generator = "seq_categoria", strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nome")
    private String nome;
}
