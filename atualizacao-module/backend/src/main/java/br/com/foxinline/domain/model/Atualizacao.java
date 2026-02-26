package br.com.foxinline.domain.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "atualizacao")
@Getter
@Setter
@AllArgsConstructor

public class Atualizacao extends EntidadeGenerica {
    @Id
    @SequenceGenerator(sequenceName = "seq_atualizacao", name = "seq_atualizacao", allocationSize = 1)
    @GeneratedValue(generator = "seq_atualizacao", strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(length = 9999)
    private String descricao;
    
    @Column(length = 9999)
    private String textoNovidade;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAgendamento;
    private Boolean pendente;
    
    public Atualizacao() {
        this.pendente = true;
    }
}
