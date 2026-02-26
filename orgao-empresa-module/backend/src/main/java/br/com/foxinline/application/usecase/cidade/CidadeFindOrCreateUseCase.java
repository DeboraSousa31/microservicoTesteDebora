package br.com.foxinline.application.usecase.cidade;

// CidadeFindOrCreateUseCase.java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import br.com.foxinline.application.dto.request.EnderecoRequestDTO;
import br.com.foxinline.application.usecase.estado.EstadoFindByUfOrCreateUseCase;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.repository.CidadeRepository;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class CidadeFindOrCreateUseCase {

    // @Inject
    // CidadeRepository cidadeRepository;

    // @Inject
    // CidadeFindByCodigoIbgeUseCase cidadeFindByCodigoIbgeUseCase;

    // @Inject
    // EstadoFindByUfOrCreateUseCase estadoFindByUfOrCreateUseCase; 

    // @WithTransaction
    // public Uni<Cidade> execute(EnderecoRequestDTO enderecoRequest, Usuario usuario) {
    //     // Se já temos um ID, apenas buscamos a cidade
    //     if (enderecoRequest.getIdCidade() != null) {
    //         return cidadeRepository.findByIdAtivo(enderecoRequest.getIdCidade());
    //     }

    //     return cidadeFindByCodigoIbgeUseCase.execute(enderecoRequest.getCodigoIbge())
    //         .onItem().ifNull().switchTo(() -> {
    
    //             System.out.println(">>> Cidade com IBGE '" + enderecoRequest.getCodigoIbge() + "' não encontrada. Criando nova.");
                
               
    //             return estadoFindByUfUseCase.execute(enderecoRequest.getUfEstado(), enderecoRequest.getNomeEstado(), usuario)
    //                 .onItem().transformToUni(estadoGerenciado -> {
    //                     Cidade novaCidade = new Cidade();
    //                     novaCidade.setCodigoIBGE(enderecoRequest.getCodigoIbge());
    //                     novaCidade.setNome(enderecoRequest.getNomeCidade());
    //                     novaCidade.setEstado(estadoGerenciado); 
                        
    //                     return cidadeRepository.save(novaCidade, usuario);
    //                 });
    //         });
//     }
}