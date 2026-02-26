package br.com.foxinline.application.usecase.situacaoProprietario;

import br.com.foxinline.application.dto.response.SituacaoProprietarioResponseDTO;
import br.com.foxinline.domain.mapper.SituacaoProprietarioMapper;
import br.com.foxinline.infrastructure.repository.SituacaoProprietarioRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoSituacaoProprietarioUseCase {

    @Inject
    SituacaoProprietarioRepositoryImpl proprietarioRepositoryImpl;

    @Inject
    SituacaoProprietarioMapper mapper;

    public Uni<SituacaoProprietarioResponseDTO> execute(Long situacaoProprietarioId) {
        return proprietarioRepositoryImpl.findByIdAtivo(situacaoProprietarioId).onItem()
                .ifNull().failWith(new NotFoundException("Objeto não encontrado"))
                .onItem().transform(mapper::toResponse);
    }

}
