package br.com.foxinline.application.usecase.atualizacao;

import br.com.foxinline.application.dto.request.AtualizacaoRequestDTO;
import br.com.foxinline.domain.repository.AtualizacaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateAtualizacaoUseCase {
    @Inject
    AtualizacaoRepository atualizacaoRepository;

    public Uni<Void> execute(AtualizacaoRequestDTO request){
        if(request.getId() == null){
            return Uni.createFrom().voidItem();
        }
        return atualizacaoRepository.update(request.getId(), request.getDescricao(), request.getDataAgendamento(), request.getTextoNovidade()).replaceWithVoid();
        
    }
}
