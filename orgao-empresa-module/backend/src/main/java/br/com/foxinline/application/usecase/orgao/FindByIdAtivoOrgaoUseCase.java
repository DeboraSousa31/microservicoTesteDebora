package br.com.foxinline.application.usecase.orgao;



import org.hibernate.reactive.mutiny.Mutiny;

import br.com.foxinline.application.dto.response.OrgaoResponseDTO;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.model.Orgao;
import br.com.foxinline.domain.repository.OrgaoRepository;
import br.com.foxinline.infrastructure.repository.OrgaoRepositoryImpl;
import io.smallrye.mutiny.Uni;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
public class FindByIdAtivoOrgaoUseCase {
    
    @Inject
    OrgaoRepositoryImpl orgaoRepositoryImpl;


    @Inject
    OrgaoMapper mapper;

    public Uni<OrgaoResponseDTO> execute (Long orgaoId){
        return orgaoRepositoryImpl.findByIdAtivo(orgaoId)
                .onItem()
                .ifNull().failWith(new NotFoundException("Objeto não encontrado."))
                .onItem().transform(mapper::toResponse);
    }


}
