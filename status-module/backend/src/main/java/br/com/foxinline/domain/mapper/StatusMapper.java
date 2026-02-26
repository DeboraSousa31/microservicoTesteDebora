package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import br.com.foxinline.application.dto.response.CategoriaResponseDTO;
import br.com.foxinline.application.dto.response.StatusResponseDTO;
import br.com.foxinline.domain.model.Categoria;
import br.com.foxinline.domain.model.Status;

@Mapper(componentModel = "cdi")
public interface StatusMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "nome", target = "nome"),
            @Mapping(source = "descricao", target = "descricao"),
            @Mapping(source = "credenciada.id", target = "idCredenciada"),
            @Mapping(source = "credenciada.usuario.nome", target = "nomeCredenciada"),
            @Mapping(source = "categoria.id", target = "idCategoria"),
            @Mapping(source = "categoria.nome", target = "nomeCategoria"),
            @Mapping(source = "posicao", target = "posicao"),
            @Mapping(source = "legenda", target = "legenda"),

       

    })
    StatusResponseDTO toResponse(Status entity);
    List<StatusResponseDTO> toResponseList(List<Status> entityList);



    
}
