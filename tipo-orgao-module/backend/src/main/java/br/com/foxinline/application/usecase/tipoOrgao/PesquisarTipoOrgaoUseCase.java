package br.com.foxinline.application.usecase.tipoOrgao;

import java.util.List;

import br.com.foxinline.application.dto.request.TipoOrgaoRequestDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.application.dto.response.TipoOrgaoResponseDTO;
import br.com.foxinline.domain.mapper.TipoOrgaoMapper;
import br.com.foxinline.domain.repository.TipoOrgaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PesquisarTipoOrgaoUseCase {

    @Inject
    TipoOrgaoRepository proprietarioRepository;

    @Inject
    TipoOrgaoMapper mapper;

    public Uni<PaginatedResponseDTO<TipoOrgaoResponseDTO>> execute(TipoOrgaoRequestDTO request) {
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }

        return proprietarioRepository.pesquisaPaginada(
                request.getNome(),
                request.getDescricao(),
                request.getIdCredenciadaUsuario(),
                request.getIdCredenciadaTipoOrgao(),
                request.getPage(),
                request.getSize(),
                request.getSort()
                ).onItem().transform(paginatedResult -> {
                    List<TipoOrgaoResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
    }

}
