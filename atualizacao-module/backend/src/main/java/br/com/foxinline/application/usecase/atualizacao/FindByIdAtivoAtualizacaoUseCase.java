package br.com.foxinline.application.usecase.atualizacao;

import br.com.foxinline.application.dto.response.AtualizacaoResponseDTO;
import br.com.foxinline.domain.mapper.AtualizacaoMapper;
import br.com.foxinline.infrastructure.repository.AtualizacaoRepositoryImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FindByIdAtivoAtualizacaoUseCase {
    @Inject
    AtualizacaoRepositoryImpl atualizacaoRepositoryImpl;


    @Inject
    AtualizacaoMapper mapper;


    public Uni<AtualizacaoResponseDTO> execute(Long atualizacaoId){
        return atualizacaoRepositoryImpl.findByIdAtivo(atualizacaoId)
            .onItem()
            .ifNull().failWith(new NotFoundException("Objeto não encontrado"))
            .onItem().transform(mapper::toResponse);
    }
}
