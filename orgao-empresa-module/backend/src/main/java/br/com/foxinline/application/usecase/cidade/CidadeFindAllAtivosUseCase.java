package br.com.foxinline.application.usecase.cidade;

import java.util.List;

import br.com.foxinline.application.dto.response.CidadeResponseDTO;
import br.com.foxinline.domain.repository.CidadeRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CidadeFindAllAtivosUseCase {
    
    @Inject
    CidadeRepository cidadeRepository;

    public Uni<List<CidadeResponseDTO>> execute(String nomeCidade){
        return cidadeRepository.findAllAtivos(nomeCidade);
    }
}
