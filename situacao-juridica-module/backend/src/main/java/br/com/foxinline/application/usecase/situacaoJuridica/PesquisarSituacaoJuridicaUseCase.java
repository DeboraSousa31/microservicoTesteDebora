package br.com.foxinline.application.usecase.situacaoJuridica;

import java.util.List;

import br.com.foxinline.application.dto.request.SituacaoJuridicaRequestDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.application.dto.response.SituacaoJuridicaResponseDTO;
import br.com.foxinline.domain.mapper.SituacaoJuridicaMapper;
import br.com.foxinline.domain.repository.SituacaoJuridicaRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class PesquisarSituacaoJuridicaUseCase {

    @Inject
    SituacaoJuridicaRepository situacaoJuridicaRepository;

    @Inject
    SituacaoJuridicaMapper mapper;
    
    public Uni<PaginatedResponseDTO<SituacaoJuridicaResponseDTO>> execute(SituacaoJuridicaRequestDTO request) {
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }

        return situacaoJuridicaRepository.pesquisaPaginada(
                request.getNome(),
                request.getDescricao(),
                request.getIdUsuario(),
                request.getIdCredenciadaSituacaoJuridica(),
                request.getPage(),
                request.getSize(),
                request.getSort()
                ).onItem().transform(paginatedResult -> {
                    List<SituacaoJuridicaResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
    }
}
