package br.com.foxinline.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class Credenciada extends Pessoa {
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
