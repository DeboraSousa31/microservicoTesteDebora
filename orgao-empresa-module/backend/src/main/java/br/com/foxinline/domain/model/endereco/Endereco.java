package br.com.foxinline.domain.model.endereco;

import br.com.foxinline.domain.model.EntidadeGenerica;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Endereco extends EntidadeGenerica {
    @Id
    @SequenceGenerator(name = "seq_endereco", sequenceName = "seq_endereco", allocationSize = 1)
    @GeneratedValue(generator = "seq_endereco", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String cep;

    private String logradouro;

    private String numero;

    private String complemento;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bairro_id")
    private Bairro bairro;
    
    @Enumerated(EnumType.STRING)
    private TipoLogradouro tipoLogradouro;

    public boolean existEndereco() {
        return !this.logradouro.equals("") || !this.cep.equals("") || this.getBairro() != null || this.getBairro().getCidade() != null;
    }
}
