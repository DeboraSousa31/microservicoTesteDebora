package br.com.foxinline.application.usecase.situacaoProprietario;

import java.util.List;

import br.com.foxinline.application.dto.request.SituacaoProprietarioRequestDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.application.dto.response.SituacaoProprietarioResponseDTO;
import br.com.foxinline.domain.mapper.SituacaoProprietarioMapper;
import br.com.foxinline.domain.repository.SituacaoProprietarioRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PesquisarSituacaoProprietarioUseCase {

    @Inject
    SituacaoProprietarioRepository proprietarioRepository;

    @Inject
    SituacaoProprietarioMapper mapper;

    public Uni<PaginatedResponseDTO<SituacaoProprietarioResponseDTO>> execute(SituacaoProprietarioRequestDTO request) {
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }

        return proprietarioRepository.pesquisaPaginada(
                request.getNome(),
                request.getDescricao(),
                request.getIdCredenciadaUsuario(),
                request.getIdCredenciadaSituacaoProprietario(),
                request.getPage(),
                request.getSize(),
                request.getSort()
                ).onItem().transform(paginatedResult -> {
                    List<SituacaoProprietarioResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
    }

}
