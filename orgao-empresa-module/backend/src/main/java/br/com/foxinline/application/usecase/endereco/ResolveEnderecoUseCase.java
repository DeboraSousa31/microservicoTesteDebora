// package br.com.foxinline.application.usecase.endereco;

// import br.com.foxinline.application.dto.request.EnderecoRequestDTO;
// import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
// import br.com.foxinline.application.usecase.bairro.BairroFindByNomeAndCidadeUseCase;
// import br.com.foxinline.application.usecase.cidade.CidadeFindByCodigoIbgeUseCase;
// import br.com.foxinline.application.usecase.estado.EstadoFindByUfOrCreateUseCase;
// import br.com.foxinline.domain.model.Usuario;
// import br.com.foxinline.domain.model.endereco.Bairro;
// import br.com.foxinline.domain.model.endereco.Cidade;
// import br.com.foxinline.domain.model.endereco.Estado;
// import br.com.foxinline.domain.repository.BairroRepository;
// import br.com.foxinline.domain.repository.CidadeRepository;
// import io.smallrye.mutiny.Uni;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;


// @ApplicationScoped
// public class ResolveEnderecoUseCase {

//     @Inject
//     CidadeRepository cidadeRepository;
//     @Inject
//     BairroRepository bairroRepository;

//     @Inject
//     EstadoFindByUfOrCreateUseCase estadoFindByUfOrCreateUseCase;
//     @Inject
//     CidadeFindByCodigoIbgeUseCase cidadeFindByCodigoIbgeUseCase;
//     @Inject
//     BairroFindByNomeAndCidadeUseCase bairroFindByNomeAndCidadeUseCase;


//     public Uni<Bairro> execute(EnderecoRequestDTO enderecoDTO, Usuario usuario) {
 
        // return estadoFindByUfOrCreateUseCase.execute(enderecoDTO.getUfEstado(), enderecoDTO.getNomeEstado(), usuario)
        //     .onItem().transformToUni(estadoGerenciado -> 
              
        //         return cidadeFindByCodigoIbgeUseCase.execute(enderecoDTO.getCodigoIbge())
        //             .onItem().ifNull().switchTo(() -> {
               
        //                 System.out.println("CRIANDO nova Cidade: " + enderecoDTO.getNomeCidade());
        //                 Cidade novaCidade = new Cidade();
        //                 novaCidade.setCodigoIBGE(enderecoDTO.getCodigoIbge());
        //                 novaCidade.setNome(enderecoDTO.getNomeCidade());
        //                 novaCidade.setEstado(estadoGerenciado);
        //                 return cidadeRepository.persistAndFlush(novaCidade);
        //             })
        //     )
        //     .onItem().transformToUni(cidadeGerenciada -> {
  
        //         if (enderecoDTO.getIdBairro() != null) {
        //             return bairroRepository.findById(enderecoDTO.getIdBairro())
        //                    .onItem().ifNull().failWith(() -> new NotFoundException("Bairro com ID " + enderecoDTO.getIdBairro() + " não encontrado."));
        //         }
                
  
//                 return bairroFindByNomeAndCidadeUseCase.execute(enderecoDTO.getBairroNome(), cidadeGerenciada)
//                     .onItem().ifNull().switchTo(() -> {
//                         System.out.println("CRIANDO novo Bairro: " + enderecoDTO.getBairroNome());
//                         Bairro novoBairro = new Bairro();
//                         novoBairro.setNome(enderecoDTO.getBairroNome());
//                         novoBairro.setCidade(cidadeGerenciada);
//                         return bairroRepository.persistAndFlush(novoBairro);
//                     });
//             });
//     }
// }