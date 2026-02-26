package br.com.foxinline.application.usecase.estado;

import br.com.foxinline.application.dto.response.EstadoResponseDTO;
import br.com.foxinline.domain.mapper.EstadoMapper;
import br.com.foxinline.infrastructure.repository.EstadoRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoEstadoUseCase {
    @Inject
    EstadoRepositoryImpl estadoRepositoryImpl;

    @Inject
    EstadoMapper mapper;

    public Uni<EstadoResponseDTO> execute(Long estadoId){
      
        return estadoRepositoryImpl.findByIdAtivo(estadoId)
            .onItem()
            .ifNull().failWith(new NotFoundException())
            .onItem().transform(mapper::toResponse);
    }

}
