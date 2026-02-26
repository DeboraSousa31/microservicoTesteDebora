package br.com.foxinline.application.usecase.tipoComodo;

import java.util.List;

import br.com.foxinline.application.dto.request.TipoComodoRequestDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.application.dto.response.TipoComodoResponseDTO;
import br.com.foxinline.domain.mapper.TipoComodoMapper;
import br.com.foxinline.domain.repository.TipoComodoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PesquisarTipoComodoUseCase {
    @Inject
    TipoComodoRepository tipoComodoRepository;

    @Inject
    TipoComodoMapper mapper;
    

    public Uni<PaginatedResponseDTO<TipoComodoResponseDTO>> execute(TipoComodoRequestDTO request){
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }
        return tipoComodoRepository.pesquisaPaginada(
                request.getNomeSingular(),
                request.getNomePlural(),
                request.getIdCredenciadaTipoComodo(),
                request.getIdUsuario(),
                request.getPage(),
                request.getSize(),
                request.getSort()
                ).onItem().transform(paginatedResult -> {
    
                    List<TipoComodoResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
    }
}
