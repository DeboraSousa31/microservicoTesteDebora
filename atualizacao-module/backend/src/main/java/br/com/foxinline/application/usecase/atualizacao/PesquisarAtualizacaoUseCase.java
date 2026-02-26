package br.com.foxinline.application.usecase.atualizacao;

import java.util.List;

import br.com.foxinline.application.dto.request.AtualizacaoRequestDTO;
import br.com.foxinline.application.dto.response.AtualizacaoResponseDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.domain.mapper.AtualizacaoMapper;
import br.com.foxinline.domain.repository.AtualizacaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PesquisarAtualizacaoUseCase {


    @Inject
    AtualizacaoRepository atualizacaoRepository;

    @Inject
    AtualizacaoMapper mapper;


    public Uni<PaginatedResponseDTO<AtualizacaoResponseDTO>> execute(AtualizacaoRequestDTO request){
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }

        // return atualizacaoRepository.pesquisaPaginada(
        //     request.getDescricao(), request.getIdUsuario(), request.getDataAgendamento(), request.getPage(), request.getSize(), request.getSort()).onItem().transform(paginatedResult ->{
        //         List<AtualizacaoResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
        //         return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
        //     });

         return atualizacaoRepository.verificarEProcessarAgendamentos()
            .chain(() -> {
                
                return atualizacaoRepository.pesquisaPaginada(
                    request.getDescricao(), 
                    request.getIdUsuario(), 
                    request.getDataAgendamento(), 
                    request.getPage(), 
                    request.getSize(), 
                    request.getSort()
                ).onItem().transform(paginatedResult -> {
                    List<AtualizacaoResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
            });
    }
    
}
