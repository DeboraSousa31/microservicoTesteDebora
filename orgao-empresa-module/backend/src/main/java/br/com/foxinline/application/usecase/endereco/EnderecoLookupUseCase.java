package br.com.foxinline.application.usecase.endereco;

import br.com.foxinline.application.dto.response.EnderecoResponseLookupDTO;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.repository.BairroRepository;
import br.com.foxinline.domain.repository.CidadeRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EnderecoLookupUseCase {

    @Inject
    CidadeRepository cidadeRepository;

    @Inject
    BairroRepository bairroRepository;

    @Inject
    OrgaoMapper mapper;

    public Uni<EnderecoResponseLookupDTO> execute(String codigoIbge, String bairroNome) {
        System.out.println(">>> BUSCANDO: IBGE = " + codigoIbge + " | Bairro = " + bairroNome);

        return cidadeRepository.findByCodigoIBGE(codigoIbge)
            .onItem().transformToUni(cidade -> {
                if (cidade == null) {
            
                    return Uni.createFrom().nullItem();
                }
             

                return bairroRepository.findByNomeAndCidadeId(bairroNome, cidade.getId())
                    .onItem().transform(bairro -> {
           
                        if (bairro == null) {
                         
                            return null;
                        }
                     
                        EnderecoResponseLookupDTO response = new EnderecoResponseLookupDTO();
                        response.setCidade(mapper.toLookupCidadeDTO(cidade));
                        response.setBairro(mapper.toLookupBairroDTO(bairro));
            
                        return response;
                    });
            });
    }
}
