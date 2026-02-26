package br.com.foxinline.application.usecase.orgao;

import java.util.List;

import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
import br.com.foxinline.application.dto.response.OrgaoResponseDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.repository.OrgaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PesquisarOrgaoUseCase {
    



    @Inject
    OrgaoRepository orgaoRepository;

    @Inject
    OrgaoMapper mapper;
    
    public Uni<PaginatedResponseDTO<OrgaoResponseDTO>> execute(OrgaoRequestDTO request) {
        if (request == null) {
            return Uni.createFrom().item(new PaginatedResponseDTO<>(List.of(), 0L));
        }

        return orgaoRepository.pesquisaPaginada(
                request.getNome(),
                request.getNomeResponsavel(),
                request.getIdCidade(),
                request.getIdTipoOrgao(),
                request.getIdCredenciadaOrgao(),
                request.getCodigoIBGEServentia(),
                request.getOficioServentia(),
                request.getCodigoServentia(),
                request.getNomeTabeliao(),
                request.getIdUsuario(),
        
                request.getPage(),
                request.getSize(),
                request.getSort()
                ).onItem().transform(paginatedResult -> {
                    List<OrgaoResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
                    return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalElements());
                });
    }



}
