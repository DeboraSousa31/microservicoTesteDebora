package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.foxinline.application.dto.response.SituacaoJuridicaResponseDTO;
import br.com.foxinline.domain.model.SituacaoJuridica;

@Mapper(componentModel = "cdi")
public interface SituacaoJuridicaMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "nome", target = "nome"),
            @Mapping(source = "descricao", target = "descricao"),
            @Mapping(source = "credenciada.id", target = "idCredenciada"),
            @Mapping(source = "credenciada.usuario.nome", target = "nomeCredenciada")
    })

    SituacaoJuridicaResponseDTO toResponse(SituacaoJuridica entity);
    List<SituacaoJuridicaResponseDTO> toResponseList(List<SituacaoJuridica> entityList);
    
}
