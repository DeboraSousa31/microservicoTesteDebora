package br.com.foxinline.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString(exclude = {"usuario"}) 
public class Credenciada extends Pessoa {
    
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
