package br.com.foxinline.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
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
@Table(name = "status")
public class Status extends EntidadeGenerica {

    @Id
    @SequenceGenerator(sequenceName = "seq_status", name = "seq_status", allocationSize = 1)
    @GeneratedValue(generator = "seq_status", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nome;
    private Long posicao;

    @Column(scale = 2, precision = 10)
    @Digits(fraction = 2, integer = 10)
    private BigDecimal pontuacaoDaAtividade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    private String descricao;
    
    private String cor;
    private String corMapa;
    private String legenda;

}
