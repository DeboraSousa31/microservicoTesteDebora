package br.com.foxinline.application.usecase.tipoOrgao;

import java.util.List;


import br.com.foxinline.application.dto.response.TipoOrgaoResponseDTO;


import br.com.foxinline.domain.repository.TipoOrgaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TipoOrgaoFindAllAtivosUseCase {
    
    @Inject
    TipoOrgaoRepository tipoOrgaoRepository;
    
    

    public Uni<List<TipoOrgaoResponseDTO>> execute(String nomeTipoOrgao){
        return tipoOrgaoRepository.findAllAtivos(nomeTipoOrgao);
    }
    
}

