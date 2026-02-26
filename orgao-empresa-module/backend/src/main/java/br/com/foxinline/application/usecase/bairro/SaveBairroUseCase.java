package br.com.foxinline.application.usecase.bairro;

import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
import br.com.foxinline.application.dto.response.EnderecoResponseLookupDTO;
import br.com.foxinline.application.usecase.cidade.CidadeFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.model.endereco.Estado;
import br.com.foxinline.domain.repository.BairroRepository;
import br.com.foxinline.domain.repository.CidadeRepository;
import br.com.foxinline.domain.repository.EstadoRepository;
import br.com.foxinline.shared.util.Utils;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveBairroUseCase {
    
    @Inject
    CidadeRepository cidadeRepository;

    @Inject
    BairroRepository bairroRepository;

    @Inject
    EstadoRepository estadoRepository;
    
    @Inject
    CidadeFindByIdAtivoUseCase cidadeFindByIdAtivoUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    OrgaoMapper mapper;

    public Uni<Bairro> execute(OrgaoRequestDTO request) {
            return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                    .onItem().ifNull().failWith(() -> new NotFoundException("Usuario nao encontrado"))
                    .onItem().transformToUni(usuario -> {

                        return cidadeRepository.findByCodigoIBGE(request.getEndereco().getCodigoIbge()).onItem()
                        .transformToUni(cidadeEncontrada ->{
                               
                                Bairro novoBairro = new Bairro();
                                novoBairro.setNome(request.getEndereco().getBairroNome());
                               
                          
                                if(cidadeEncontrada != null){
                                        novoBairro.setCidade(cidadeEncontrada);
                                        return bairroRepository.save(novoBairro, usuario);
                                }
                                
                                Estado novoEstado = new Estado();
                                novoEstado.setNome(request.getEndereco().getNomeEstado());
                                novoEstado.setUf(request.getEndereco().getUfEstado());

                                return estadoRepository.save(novoEstado, usuario).onItem().transformToUni(
                                        estadoSalvo -> {
                                                Cidade novaCidade = new Cidade();
                                                novaCidade.setNome(request.getEndereco().getNomeCidade());
                                                novaCidade.setCodigoIBGE(request.getEndereco().getCodigoIbge());
                                                novaCidade.setEstado(estadoSalvo);

                                                return cidadeRepository.save(novaCidade, usuario);

                                        }).onItem().transformToUni(cidadeSalva ->{
                                        novoBairro.setCidade(cidadeSalva);
                                        return bairroRepository.save(novoBairro, usuario);
                                });

                                

                        });
                
                    });

                              
                            
                        
                    
            };
    }

