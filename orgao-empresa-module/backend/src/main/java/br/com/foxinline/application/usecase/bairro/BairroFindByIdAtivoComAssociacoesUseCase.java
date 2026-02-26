package br.com.foxinline.application.usecase.bairro;

import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.repository.BairroRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BairroFindByIdAtivoComAssociacoesUseCase {
    @Inject 
    BairroRepository bairroRepository;

    public Uni<Bairro> execute(Long bairroId) {
        return bairroRepository.findByIdAtivoComAssociacoes(bairroId);
    }
}
