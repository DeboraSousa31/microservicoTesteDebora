package br.com.foxinline.domain.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class EntidadeGenerica implements Serializable {

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
