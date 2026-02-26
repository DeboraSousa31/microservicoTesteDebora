package br.com.foxinline.domain.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@MappedSuperclass
@Getter
@Setter
@ToString
public class EntidadeGenerica {
   
    
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date dataCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date dataAtualizacao;

    private Boolean ativo;

    @OneToOne
    @JsonIgnore
    private Credenciada credenciada;

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = new Date();
    }

    @PrePersist
    public void prePersist() {
        this.dataCriacao = new Date();
        this.ativo = true;
    }
    
    @Version()
    private int version;

    public EntidadeGenerica() {
        this.ativo = true;
        this.dataCriacao = new Date();
    }
}
