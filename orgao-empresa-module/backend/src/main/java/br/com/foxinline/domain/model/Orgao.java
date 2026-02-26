package br.com.foxinline.domain.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import br.com.foxinline.domain.model.endereco.Endereco;

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
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraph(
    name = "orgao-with-full-address", // Dê um nome ao seu plano de busca
    attributeNodes = {
        @NamedAttributeNode("tipoDeOrgao"),
        @NamedAttributeNode("credenciada"),
        @NamedAttributeNode(value = "enderecoDoOrgao", subgraph = "graph-endereco")
    },
    subgraphs = {
        @NamedSubgraph(name = "graph-endereco", attributeNodes = {
            @NamedAttributeNode(value = "bairro", subgraph = "graph-bairro")
        }),
        @NamedSubgraph(name = "graph-bairro", attributeNodes = {
            @NamedAttributeNode(value = "cidade", subgraph = "graph-cidade")
        }),
        @NamedSubgraph(name = "graph-cidade", attributeNodes = {
            @NamedAttributeNode("estado")
        })
    }
)
public class Orgao extends EntidadeGenerica{

    @Id
    @SequenceGenerator(sequenceName = "seq_orgao", name = "seq_orgao", allocationSize = 1)
    @GeneratedValue(generator = "seq_orgao", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nome;
    private String razaoSocial;
    private String telefone;
    
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "enderecodoorgao_id") 
    private Endereco enderecoDoOrgao;

    @ManyToOne(fetch = FetchType.LAZY)
    private TipoOrgao tipoDeOrgao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    private String cnpj;
    private String cpf;
    private String email;
    private String nomePrefeito;
    private String nomeResponsavel;

    // @OneToMany(mappedBy = "orgao", fetch = FetchType.LAZY)
    // @JsonIgnore
    // private List<Usuario> usuarios;

    private String codigoIBGEServentia;
    private String codigoServentia;
    private String oficioServentia;
    private String nomeTabeliao;

    // @ManyToOne(fetch = FetchType.LAZY)
    // private Credenciada credenciada;



}
