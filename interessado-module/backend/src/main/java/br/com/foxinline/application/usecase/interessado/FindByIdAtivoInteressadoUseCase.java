package br.com.foxinline.application.usecase.interessado;

import br.com.foxinline.application.dto.response.InteressadoResponseDTO;
import br.com.foxinline.domain.mapper.InteressadoMapper;
import br.com.foxinline.infrastructure.repository.InteressadoRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoInteressadoUseCase {
    
    @Inject
    InteressadoRepositoryImpl interessadoRepositoryImpl;

    @Inject
    InteressadoMapper mapper;


    public Uni<InteressadoResponseDTO> execute(Long idInteressado){
        return interessadoRepositoryImpl.findByIdAtivo(idInteressado)
            .onItem().ifNull().failWith(new NotFoundException("Objeto não encontrado"))
            .onItem().transform(mapper::toResponse);
    }
}
