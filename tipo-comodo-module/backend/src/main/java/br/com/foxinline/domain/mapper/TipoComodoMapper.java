package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.foxinline.application.dto.response.TipoComodoResponseDTO;
import br.com.foxinline.domain.model.TipoComodo;
@Mapper(componentModel="cdi")
public interface TipoComodoMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "nomeSingular", target = "nomeSingular"),
        @Mapping(source = "nomePlural", target = "nomePlural"),
        @Mapping(source = "credenciada.id", target = "idCredenciada"),
        @Mapping(source = "credenciada.usuario.nome", target = "nomeCredenciada"),
        
    })

    TipoComodoResponseDTO toResponse(TipoComodo entity);
    List<TipoComodoResponseDTO> toResponseList(List<TipoComodo> entity);
    
} 