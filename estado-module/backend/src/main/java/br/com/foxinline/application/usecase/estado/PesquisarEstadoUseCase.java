package br.com.foxinline.application.usecase.estado;

import java.util.List;

import br.com.foxinline.application.dto.request.EstadoRequestDTO;
import br.com.foxinline.application.dto.response.EstadoResponseDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.domain.mapper.EstadoMapper;
import br.com.foxinline.domain.repository.EstadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class PesquisarEstadoUseCase {
    @Inject
    EstadoRepository estadoRepository;

    @Inject
    EstadoMapper mapper;
    

    public Uni<PaginatedResponseDTO<EstadoResponseDTO>> execute(EstadoRequestDTO request){
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }
        return estadoRepository.pesquisaPaginada(
                request.getNome(),
                request.getUf(),
                request.getIdUsuario(),
                request.getPage(),
                request.getSize(),
                request.getSort()
                ).onItem().transform(paginatedResult -> {
    
                    List<EstadoResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
    }
    
}
