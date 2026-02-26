package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.foxinline.application.dto.response.CredenciadaResponseDTO;
import br.com.foxinline.domain.model.Credenciada;

@Mapper(componentModel = "cdi")
public interface CredenciadaMapper {
    @Mapping(source = "usuario.nome", target = "nome")
    CredenciadaResponseDTO toResponseDTO(Credenciada entity);
    List<CredenciadaResponseDTO> toResponseList(List<Credenciada> entities);
}
