package br.com.foxinline.application.usecase.orgao;


import br.com.foxinline.application.dto.request.EnderecoRequestDTO;
import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
import br.com.foxinline.application.usecase.bairro.BairroFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.bairro.BairroFindByNomeAndCidadeUseCase;
import br.com.foxinline.application.usecase.cidade.CidadeFindByCodigoIbgeUseCase;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.estado.EstadoFindByUfOrCreateUseCase;
import br.com.foxinline.application.usecase.estado.EstadoFindByUfUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.TipoOrgaoFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Orgao;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.model.endereco.Estado;
import br.com.foxinline.domain.model.endereco.TipoLogradouro;
import br.com.foxinline.domain.repository.BairroRepository;
import br.com.foxinline.domain.repository.CidadeRepository;
import br.com.foxinline.domain.repository.OrgaoRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveOrgaoUseCase {

    @Inject
    OrgaoRepository orgaoRepository;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    TipoOrgaoFindByIdAtivosUseCase tipoOrgaoFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    BairroFindByIdAtivosUseCase bairroFindByIdAtivosUseCase;

    @Inject
    CidadeFindByCodigoIbgeUseCase cidadeFindByCodigoIbgeUseCase;

    @Inject
    EstadoFindByUfOrCreateUseCase estadoFindByUfUseCase;


   public Uni<Orgao> execute(OrgaoRequestDTO request) {
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
            .onItem().ifNull().failWith(() -> {
            
                return new NotFoundException("Usuário não encontrado");
            })
            .onItem().transformToUni(usuario -> 
                credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaOrgao())
                    .onItem().ifNull().failWith(() -> {
                        return new NotFoundException("Credenciada não encontrada ou inativa");
                    })
                    .onItem().transformToUni(credenciada -> 
                        tipoOrgaoFindByIdAtivosUseCase.execute(request.getIdTipoOrgao())
                            .onItem().ifNull().failWith(() -> {
                                return new NotFoundException("Tipo de Órgão não encontrado ou inativo");
                            })
                            .onItem().transformToUni(tipoOrgao -> {

                                
                                return bairroFindByIdAtivosUseCase.execute(request.getEndereco().getIdBairro()).onItem()
                                .transformToUni(bairroEncontrado ->{
                                    
                                    return cidadeFindByCodigoIbgeUseCase.execute(request.getEndereco().getCodigoIbge()).onItem()
                                    .transformToUni(cidadeEncontrada ->{
                                       // return estadoFindByUfUseCase.execute(request.getEndereco().getUfEstado(), request.getEndereco().getNomeEstado(), usuario)
    
                                                Endereco endereco = new Endereco();
                                                endereco.setCep(request.getEndereco().getCep());
                                                endereco.setLogradouro(request.getEndereco().getLogradouro());
                                                endereco.setNumero(request.getEndereco().getNumero());
                                                endereco.setComplemento(request.getEndereco().getComplemento());
                
                                                System.out.println(request.getEndereco());
                                                if (request.getEndereco().getTipoLogradouro() != null) {
                                                    try {
                                                        TipoLogradouro tipoLogradouro = TipoLogradouro.valueOf(request.getEndereco().getTipoLogradouro().toUpperCase());
                                                        System.out.println(tipoLogradouro);
                                                        endereco.setTipoLogradouro(tipoLogradouro);
                                                    } catch (IllegalArgumentException e) {
                                                        e.printStackTrace();
                                                        return Uni.createFrom().failure(new RuntimeException("Tipo de logradouro inválido"));
                                                    }
                                                }
                                                    
                                                
                                                if (bairroEncontrado != null) {
                                                    endereco.setBairro(bairroEncontrado);

                                                }else{
                                                    Bairro novoBairro = new Bairro();
                                                    novoBairro.setNome(request.getEndereco().getBairroNome());

                                                    if (cidadeEncontrada != null) {
                                    
                                                        novoBairro.setCidade(cidadeEncontrada);
        
                                                    }else{
                                             
                                                        Cidade cidade = new Cidade();
                                                        cidade.setCodigoIBGE(request.getEndereco().getCodigoIbge());
                                                        cidade.setNome(request.getEndereco().getNomeCidade());
                                                        Estado estado = new Estado();
                                                        estado.setNome(request.getEndereco().getNomeEstado());
                                                        estado.setUf(request.getEndereco().getUfEstado());
                                                        cidade.setEstado(estado);
                                                        novoBairro.setCidade(cidade);
                
                                                    }
    
                                                    endereco.setBairro(novoBairro);
                                                }
    
                                            
                                                return orgaoRepository.save(
                                                        request.getNome(),
                                                        request.getEmail(),
                                                        request.getTelefone(),
                                                        request.getCnpj(),
                                                        request.getCpf(),
                                                        request.getDataNascimento(),
                                                        request.getTipoDocumento(),
                                                        request.getRazaoSocial(),
                                                        request.getNomeResponsavel(),
                                                        credenciada,
                                                        tipoOrgao,
                                                        usuario,
                                                        endereco
                                                );

                                          

                                        });


                                    }
                                );
                            }
                            )
                    )
            );
    }
}
// @Transactional
//     public Uni<Orgao> execute(OrgaoRequestDTO request) {
//         System.out.println("\n\n--- [PASSO 0] INICIANDO SaveOrgaoUseCase (Organizado) ---");
        
//         return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
//             .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado"))
//             .onItem().transformToUni(usuario -> 
//                 credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaOrgao())
//                     .onItem().ifNull().failWith(() -> new NotFoundException("Credenciada não encontrada ou inativa"))
//                     .onItem().transformToUni(credenciada -> 
//                         tipoOrgaoFindByIdAtivosUseCase.execute(request.getIdTipoOrgao())
//                             .onItem().ifNull().failWith(() -> new NotFoundException("Tipo de Órgão não encontrado ou inativo"))
//                             .onItem().transformToUni(tipoOrgao -> 
//                                 bairroFindByIdAtivosUseCase.execute(request.getEndereco().getIdBairro())
//                                     .onItem().transformToUni(bairroEncontrado -> {
                                        
//                                         // --- LÓGICA REORGANIZADA ---

//                                         // A verificação do bairro acontece IMEDIATAMENTE.
//                                         if (bairroEncontrado != null) {
//                                             // CAMINHO A: Bairro foi encontrado.
//                                             System.out.println("Bairro encontrado. Pulando busca de cidade.");
                                            
//                                             // Montamos o endereço com o bairro encontrado.
//                                             Endereco endereco = new Endereco();
//                                             endereco.setCep(request.getEndereco().getCep());
//                                             endereco.setLogradouro(request.getEndereco().getLogradouro());
//                                             endereco.setNumero(request.getEndereco().getNumero());
//                                             endereco.setComplemento(request.getEndereco().getComplemento());
//                                             endereco.setBairro(bairroEncontrado);
                                            
//                                             // Salvamos e terminamos o fluxo aqui.
//                                             return orgaoRepository.save(
//                                                 request.getNome(), request.getEmail(), request.getTelefone(),
//                                                 request.getCnpj(), request.getCpf(), request.getDataNascimento(),
//                                                 request.getTipoDocumento(), request.getRazaoSocial(),
//                                                 request.getNomeResponsavel(), credenciada, tipoOrgao,
//                                                 usuario, endereco
//                                             );
//                                         } else {
//                                             // CAMINHO B: Bairro NÃO foi encontrado. AGORA sim, buscamos a cidade.
//                                             System.out.println("Bairro não encontrado. Prosseguindo com a busca de cidade...");
                                            
//                                             return cidadeFindByCodigoIbgeUseCase.execute(request.getEndereco().getCodigoIbge())
//                                                 .onItem().transformToUni(cidadeEncontrada -> {
                                                
//                                                     Endereco endereco = new Endereco();
//                                                     endereco.setCep(request.getEndereco().getCep());
//                                                     endereco.setLogradouro(request.getEndereco().getLogradouro());
//                                                     endereco.setNumero(request.getEndereco().getNumero());
//                                                     endereco.setComplemento(request.getEndereco().getComplemento());
                    
//                                                     Bairro novoBairro = new Bairro();
//                                                     novoBairro.setNome(request.getEndereco().getBairroNome());

//                                                     if (cidadeEncontrada != null) {
//                                                         novoBairro.setCidade(cidadeEncontrada);
//                                                         System.out.println(">>> CIDADE ENCONTRADA, FORÇANDO LEITURA DOS DADOS <<<");
//                                                         System.out.println("ID da Cidade: " + cidadeEncontrada.getId());
//                                                         System.out.println("Nome da Cidade: " + cidadeEncontrada.getNome()); 
                                                                                                           
//                                                     } else {

//                                                         Cidade novaCidade = new Cidade();
//                                                         novaCidade.setCodigoIBGE(request.getEndereco().getCodigoIbge());
//                                                         novaCidade.setNome(request.getEndereco().getNomeCidade());
//                                                         Estado estado = new Estado();
//                                                         estado.setNome(request.getEndereco().getNomeEstado());
//                                                         estado.setUf(request.getEndereco().getUfEstado());
//                                                         novaCidade.setEstado(estado);
//                                                         novoBairro.setCidade(novaCidade);
//                                                         endereco.setBairro(novoBairro);
//                                                     }
                                                    
//                                                     endereco.setBairro(novoBairro);
//                                                     System.out.println("---------");
//                                                     System.out.println(endereco);
                    
//                                                     return orgaoRepository.save(
//                                                         request.getNome(), request.getEmail(), request.getTelefone(),
//                                                         request.getCnpj(), request.getCpf(), request.getDataNascimento(),
//                                                         request.getTipoDocumento(), request.getRazaoSocial(),
//                                                         request.getNomeResponsavel(), credenciada, tipoOrgao,
//                                                         usuario, endereco
//                                                     );
//                                                 });
//                                         }
//                                     })
//                             )
//                     )
//             );
//     }
// }


