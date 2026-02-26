package br.com.foxinline.application.usecase.endereco;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.repository.EnderecoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EnderecoFindByIdAtivoUseCase {

    @Inject
    EnderecoRepository enderecoRepository;

    public Uni<Endereco> execute(Long enderecoId) {
        return enderecoRepository.findByIdAtivo(enderecoId);
    }
    
}
