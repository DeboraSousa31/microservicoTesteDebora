package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.foxinline.application.dto.response.CredenciadaResponseDTO;
import br.com.foxinline.domain.model.Credenciada;

@Mapper(componentModel = "cdi")
public interface CredenciadaMapper {
    CredenciadaResponseDTO toResponseDTO(Credenciada entity);
    List<CredenciadaResponseDTO> toResponseList(List<Credenciada> entities);
}
