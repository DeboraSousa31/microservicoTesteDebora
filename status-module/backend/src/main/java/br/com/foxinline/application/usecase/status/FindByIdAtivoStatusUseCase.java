package br.com.foxinline.application.usecase.status;

import br.com.foxinline.application.dto.response.StatusResponseDTO;
import br.com.foxinline.domain.mapper.StatusMapper;
import br.com.foxinline.infrastructure.repository.StatusRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoStatusUseCase {
    @Inject
    StatusRepositoryImpl statusRepositoryImpl;

    @Inject
    StatusMapper mapper;

    public Uni<StatusResponseDTO> execute(Long statusId){
       
        return statusRepositoryImpl.findByIdAtivo(statusId)
            .onItem()
            .ifNull().failWith(new NotFoundException("Objeto não encontrado"))
            .onItem().transform(mapper::toResponse);
    }
}
