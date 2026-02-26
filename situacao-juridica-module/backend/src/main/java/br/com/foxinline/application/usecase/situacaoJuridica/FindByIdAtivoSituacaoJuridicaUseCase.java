package br.com.foxinline.application.usecase.situacaoJuridica;

import br.com.foxinline.application.dto.response.SituacaoJuridicaResponseDTO;
import br.com.foxinline.domain.mapper.SituacaoJuridicaMapper;
import br.com.foxinline.infrastructure.repository.SituacaoJuridicaRepositoryImpl;
import io.smallrye.mutiny.Uni;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoSituacaoJuridicaUseCase {

    @Inject
    SituacaoJuridicaRepositoryImpl proprietarioRepositoryImpl;

    @Inject
    SituacaoJuridicaMapper mapper;

   public Uni<SituacaoJuridicaResponseDTO> execute(Long situacaoProprietarioId) {
    


    return proprietarioRepositoryImpl.findByIdAtivo(situacaoProprietarioId)
            .onItem()
            .ifNull().failWith(new NotFoundException("Objeto não encontrado"))
            .onItem().transform(mapper::toResponse);
}

}