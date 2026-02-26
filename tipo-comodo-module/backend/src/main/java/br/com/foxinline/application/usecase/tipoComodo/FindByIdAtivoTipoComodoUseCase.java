package br.com.foxinline.application.usecase.tipoComodo;


import br.com.foxinline.application.dto.response.TipoComodoResponseDTO;
import br.com.foxinline.domain.mapper.TipoComodoMapper;
import br.com.foxinline.infrastructure.repository.TipoComodoRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoTipoComodoUseCase {
    @Inject
    TipoComodoRepositoryImpl tipoComodoRepositoryImpl;

    @Inject
    TipoComodoMapper mapper;

    public Uni<TipoComodoResponseDTO> execute(Long tipoComodoId){
        return tipoComodoRepositoryImpl.findByIdAtivo(tipoComodoId)
            .onItem()
            .ifNull().failWith(new NotFoundException())
            .onItem().transform(mapper::toResponse);
    }

}
