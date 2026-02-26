package br.com.foxinline.application.usecase.bairro;

import java.util.List;
import java.util.stream.Collectors;

import br.com.foxinline.application.dto.response.BairroResponseDTO;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.repository.BairroRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BairroFindByCidadeUseCase {

    @Inject
    BairroRepository bairroRepository;
    
    @Inject
    OrgaoMapper mapper;

    public Uni<List<BairroResponseDTO>> execute(Long cidadeId) {
        return bairroRepository.findByCidadeId(cidadeId)
            .map(bairros -> bairros.stream()
                .map(mapper::toLookupBairroDTO) 
                .collect(Collectors.toList()));
    }
}
