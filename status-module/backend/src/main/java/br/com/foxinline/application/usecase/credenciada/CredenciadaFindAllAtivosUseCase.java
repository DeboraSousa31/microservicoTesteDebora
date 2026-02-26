package br.com.foxinline.application.usecase.credenciada;

import java.util.List;

import br.com.foxinline.application.dto.response.CredenciadaResponseDTO;
import br.com.foxinline.domain.mapper.CredenciadaMapper;
import br.com.foxinline.domain.repository.CredenciadaRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class CredenciadaFindAllAtivosUseCase {
    
    @Inject
    CredenciadaRepository credendiadaRepository;

    @Inject
    CredenciadaMapper mapper;

    public Uni<List<CredenciadaResponseDTO>> execute(String nomeCredenciada){
        return credendiadaRepository.findAllAtivos(nomeCredenciada);
    }
    
}
