package br.com.foxinline.domain.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.mapstruct.Mapping;
import org.hibernate.annotations.SourceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import br.com.foxinline.application.dto.request.EnderecoRequestDTO;
import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
import br.com.foxinline.application.dto.response.BairroResponseDTO;
import br.com.foxinline.application.dto.response.CidadeResponseDTO;
import br.com.foxinline.application.dto.response.EnderecoResponseDTO;
import br.com.foxinline.application.dto.response.OrgaoResponseDTO;
import br.com.foxinline.domain.model.Orgao;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.model.endereco.Endereco;



// Adicionado NullValuePropertyMappingStrategy para ignorar campos nulos na conversão,
// o que é útil em cenários de atualização (PUT/PATCH).
@Mapper(componentModel = "cdi", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrgaoMapper {

    // =================================================================
    // Mapeamento Principal: DTO de Requisição -> Entidade
    // =================================================================


    @Mappings({
        @Mapping(source = "idTipoOrgao", target = "tipoDeOrgao.id"),
        @Mapping(source = "idCredenciadaOrgao", target = "credenciada.id"),
        @Mapping(source = "endereco", target = "enderecoDoOrgao"),
        @Mapping(source = "dataNascimento", target = "dataNascimento", dateFormat = "yyyy-MM-dd")
    })
    Orgao toEntity(OrgaoRequestDTO dto);


    // =================================================================
    // Mapeamento Principal: Entidade -> DTO de Resposta
    // =================================================================


    @Mappings({
        @Mapping(source = "tipoDeOrgao.nome", target = "nomeTipoOrgao"),
        @Mapping(source = "credenciada.usuario.nome", target = "nomeCredenciadaOrgao"),
        @Mapping(source = "credenciada.id", target = "idCredenciadaOrgao"),
        @Mapping(source = "dataNascimento", target = "dataNascimento", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "enderecoDoOrgao", target = "endereco"),
        @Mapping(source = "tipoDeOrgao", target = "tipoOrgao"),

    })

    OrgaoResponseDTO toResponse(Orgao entity);

    List<OrgaoResponseDTO> toResponseList(List<Orgao> list);


    // =================================================================
    // Mapeamento de Endereço (Requisição -> Entidade)
    // =================================================================

    @Mappings({
        @Mapping(source = "idBairro", target = "bairro.id"),
        @Mapping(source = "idCidade", target = "bairro.cidade.id") 
    })
    Endereco toEndereco(EnderecoRequestDTO dto);


    // =================================================================
    // Mapeamento de Endereço (Entidade -> Resposta) 
    // =================================================================

    @Mappings({
        @Mapping(source = "cep", target = "cep"),
        @Mapping(source = "logradouro", target = "logradouro"),
        @Mapping(source = "numero", target = "numero"),
        @Mapping(source = "complemento", target = "complemento"),
        @Mapping(source = "tipoLogradouro", target = "tipoLogradouro"),
        @Mapping(source = "bairro", target = "bairro", qualifiedByName = "bairroToBairroResponse"),
        @Mapping(source = "bairro.cidade", target = "cidade", qualifiedByName = "cidadeToCidadeResponse")
        
    })
    EnderecoResponseDTO toEnderecoResponse(Endereco endereco);


    
    @Named("bairroToBairroResponse")
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "nome", target = "nome"),
            @Mapping(source = "cidade.id", target = "idCidade"),
            @Mapping(source = "cidade.nome", target = "nomeCidade"),
            @Mapping(source = "cidade.estado.nome", target = "nomeEstado"), 
            @Mapping(source = "cidade.estado.uf", target = "ufEstado")
    })
    BairroResponseDTO toBairroResponseDTO(Bairro bairro);

    @Named("cidadeToCidadeResponse")
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "nome", target = "nome"),
            @Mapping(source = "codigoIBGE", target = "codigoIbge"),
            @Mapping(source = "estado.nome", target = "nomeEstado"), 
            @Mapping(source = "estado.uf", target = "ufEstado")
    })
    CidadeResponseDTO toCidadeResponseDTO(Cidade cidade);

 
    
    default CidadeResponseDTO toLookupCidadeDTO(Cidade cidade) {
        if (cidade == null) return null;
        CidadeResponseDTO dto = new CidadeResponseDTO();
        dto.setId(cidade.getId());
        dto.setNome(cidade.getNome());
        dto.setCodigoIbge(cidade.getCodigoIBGE());
        // if (cidade.getEstado() != null) {
        //     dto.setNomeEstado(cidade.getEstado().getNome());
        //     dto.setUfEstado(cidade.getEstado().getUf());
        // }
        return dto;
    }

    default BairroResponseDTO toLookupBairroDTO(Bairro bairro) {
        if (bairro == null) return null;
        BairroResponseDTO dto = new BairroResponseDTO();
        dto.setId(bairro.getId());
        dto.setNome(bairro.getNome());
       

        if (bairro.getCidade() != null) {
            dto.setIdCidade(bairro.getCidade().getId());
            dto.setNomeCidade(bairro.getCidade().getNome());
            if (bairro.getCidade().getEstado() != null) {
                dto.setNomeEstado(bairro.getCidade().getEstado().getNome());
                dto.setUfEstado(bairro.getCidade().getEstado().getUf());
            }
        }
        return dto;
    }
}



