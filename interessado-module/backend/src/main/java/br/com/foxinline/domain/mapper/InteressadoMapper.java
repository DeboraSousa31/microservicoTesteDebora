package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.foxinline.application.dto.response.InteressadoResponseDTO;
import br.com.foxinline.domain.model.Interessado;

@Mapper(componentModel="cdi")
public interface InteressadoMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "nome", target = "nome"),
        @Mapping(source = "cpf", target = "cpf"),
        @Mapping(source = "cnpj", target = "cnpj"),
        @Mapping(source = "nomeFantasia", target = "nomeFantasia"),
        @Mapping(source = "razaoSocial", target = "razaoSocial"),
        @Mapping(source = "email", target = "email"),
        @Mapping(source = "dataNascimento", target = "dataNascimento"),
        @Mapping(source = "rg", target = "rg"),
        @Mapping(source = "orgaoEmissor", target = "orgaoEmissor"),
        @Mapping(source = "profissao", target = "profissao"),
        @Mapping(source = "telefone", target = "telefone"),
        @Mapping(source = "credenciada.id", target = "idCredenciadaInteressado"),
        @Mapping(source = "credenciada.usuario.nome", target = "nomeCredenciadaInteressado")
    })

    InteressadoResponseDTO toResponse(Interessado entity);
    List<InteressadoResponseDTO> toResponseList(List<Interessado> entity);
    
} 