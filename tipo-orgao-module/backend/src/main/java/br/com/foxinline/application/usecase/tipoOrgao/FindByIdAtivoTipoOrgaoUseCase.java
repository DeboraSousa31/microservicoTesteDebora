package br.com.foxinline.application.usecase.tipoOrgao;

import br.com.foxinline.application.dto.response.TipoOrgaoResponseDTO;
import br.com.foxinline.domain.mapper.TipoOrgaoMapper;
import br.com.foxinline.infrastructure.repository.TipoOrgaoRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoTipoOrgaoUseCase {

    @Inject
    TipoOrgaoRepositoryImpl proprietarioRepositoryImpl;

    @Inject
    TipoOrgaoMapper mapper;

    public Uni<TipoOrgaoResponseDTO> execute(Long tipoOrgaoId) {
        return proprietarioRepositoryImpl.findByIdAtivo(tipoOrgaoId).onItem()
                .ifNull().failWith(new NotFoundException("Objeto não encontrado"))
                .onItem().transform(mapper::toResponse);
    }

}
