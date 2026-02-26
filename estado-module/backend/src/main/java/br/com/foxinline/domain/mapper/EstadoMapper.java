package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import br.com.foxinline.application.dto.response.EstadoResponseDTO;
import br.com.foxinline.domain.model.Estado;

@Mapper(componentModel="cdi")
public interface EstadoMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "nome", target = "nome"),

    })

    EstadoResponseDTO toResponse(Estado entity);
    List<EstadoResponseDTO> toResponseList(List<Estado> entity);
    
} 