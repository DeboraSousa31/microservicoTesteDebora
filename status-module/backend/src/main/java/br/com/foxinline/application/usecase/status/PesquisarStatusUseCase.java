package br.com.foxinline.application.usecase.status;

import java.util.List;

import br.com.foxinline.application.dto.request.StatusRequestDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.application.dto.response.StatusResponseDTO;
import br.com.foxinline.domain.mapper.StatusMapper;
import br.com.foxinline.domain.repository.StatusRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class PesquisarStatusUseCase {

    @Inject
    StatusRepository situacaoJuridicaRepository;

    @Inject
    StatusMapper mapper;
    
    public Uni<PaginatedResponseDTO<StatusResponseDTO>> execute(StatusRequestDTO request) {
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }
       
        return situacaoJuridicaRepository.pesquisaPaginada(
                request.getNome(),
                request.getDescricao(),
                request.getIdUsuario(),
                request.getIdCategoria(),
                request.getIdCredenciadaStatus(),
                request.getPage(),
                request.getSize(),
                request.getSort()
                ).onItem().transform(paginatedResult -> {
                    List<StatusResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
    }
}
