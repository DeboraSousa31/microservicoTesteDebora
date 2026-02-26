package br.com.foxinline.application.usecase.cidade;

import java.util.List;

import br.com.foxinline.application.dto.response.CidadeResponseDTO;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.repository.CidadeRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CidadeFindByIdAtivoUseCase {
 
    @Inject
    CidadeRepository cidadeRepository;

    public Uni<Cidade> execute(Long idCidade){
        return cidadeRepository.findByIdAtivo(idCidade);
    
}

}
