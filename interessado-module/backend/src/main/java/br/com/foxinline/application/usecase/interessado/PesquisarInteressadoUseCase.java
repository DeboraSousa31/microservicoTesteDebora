package br.com.foxinline.application.usecase.interessado;

import java.util.List;

import br.com.foxinline.application.dto.request.InteressadoRequestDTO;
import br.com.foxinline.application.dto.response.InteressadoResponseDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.domain.mapper.InteressadoMapper;
import br.com.foxinline.domain.repository.InteressadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PesquisarInteressadoUseCase {
    
    @Inject
    InteressadoRepository interessadoRepository;

    @Inject
    InteressadoMapper mapper;


    public Uni<PaginatedResponseDTO<InteressadoResponseDTO>> execute(InteressadoRequestDTO request){
        if(request == null){
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(),0L));
        }
        return interessadoRepository.pesquisaPaginada(
            request.getNome(),
            request.getCpf(),
            request.getIdCredenciadaInteressado(), 
            request.getCnpj(), 
            request.getEmail(),
            request.getTelefone(), 
            request.getOrgaoEmissor(), 
            request.getRazaoSocial(), 
            request.getNomeFantasia(), 
            request.getProfissao(), 
            request.getIdUsuario(),
            request.getDataNascimento(), 
            request.getTipoDocumento(),
            request.getPage(),
            request.getSize(),
            request.getSort()).onItem().transform(paginatedResult ->{
                List<InteressadoResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
            });
    }
}
