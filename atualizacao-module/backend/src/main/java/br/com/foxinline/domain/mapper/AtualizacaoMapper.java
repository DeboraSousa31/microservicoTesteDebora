package br.com.foxinline.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.foxinline.application.dto.response.AtualizacaoResponseDTO;
import br.com.foxinline.domain.model.Atualizacao;

@Mapper(componentModel="cdi")
public interface AtualizacaoMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "descricao", target = "descricao"),
        @Mapping(source = "textoNovidade", target = "textoNovidade"),
        @Mapping(source = "dataAgendamento", target = "dataAgendamento"),
        @Mapping(source = "credenciada.id", target = "idCredenciadaAtualizacao"),
        @Mapping(source = "credenciada.usuario.nome", target = "nomeCredenciadaAtualizacao"),
        @Mapping(source = "pendente", target = "pendente")
    })

    AtualizacaoResponseDTO toResponse(Atualizacao entity);
    List<AtualizacaoResponseDTO> toResponseList(List<Atualizacao> entity);
    
} 