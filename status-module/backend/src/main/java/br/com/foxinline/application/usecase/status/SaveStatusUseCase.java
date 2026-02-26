package br.com.foxinline.application.usecase.status;

import br.com.foxinline.application.dto.request.StatusRequestDTO;
import br.com.foxinline.application.usecase.categoria.CategoriaFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Status;
import br.com.foxinline.domain.repository.StatusRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveStatusUseCase {

    @Inject
    StatusRepository statusRepository;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CategoriaFindByIdAtivoUseCase categoriaFindByIdAtivoUseCase;

    public Uni<Status> execute(StatusRequestDTO request){
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException())
                .onItem().ifNotNull().transformToUni(usuario ->{

                    return categoriaFindByIdAtivoUseCase.execute(request.getIdCategoria())
                        .onItem().ifNull(
                        ).failWith(()-> new NotFoundException())
                        .onItem().ifNotNull().transformToUni(categoria ->{
                            return credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaStatus())
                                .onItem().transformToUni(credenciada ->{
                                    if(credenciada == null){
                                        return statusRepository.save(request.getNome(), request.getPosicao(), request.getPontuacaoDaAtividade(), request.getDescricao(), request.getCorStatus(), request.getCorMapa(), request.getLegenda(), categoria, null, usuario);
                                    }else{
                                         return statusRepository.save(request.getNome(), request.getPosicao(), request.getPontuacaoDaAtividade(), request.getDescricao(), request.getCorStatus(), request.getCorMapa(), request.getLegenda(), categoria, credenciada, usuario);
                                    }
                                });
                        });
                });

        // return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
        //     .onItem().ifNull().failWith(()-> new NotFoundException())
        //     .onItem().ifNotNull().transformToUni(usuario ->{
        //             return credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaStatus())
        //                 .onItem().ifNull().failWith(()-> new NotFoundException())
        //                 .onItem().transformToUni(credenciada ->{
        //                         return categoriaFindByIdAtivoUseCase.execute(request.getIdCategoria())
        //                             .onItem().ifNull().failWith(()-> new NotFoundException())
        //                             .onItem().transformToUni(categoria ->{
        //                                     return statusRepository.save(
        //                                         request.getNome(),
        //                                         request.getPosicao(),
        //                                         request.getPontuacaoDaAtividade(), 
        //                                         request.getDescricao(), 
        //                                         request.getCorStatus(), 
        //                                         request.getCorMapa(),
        //                                         request.getLegenda(), 
        //                                         categoria, 
        //                                         credenciada, 
        //                                         usuario);
        //                             });
        //             });
        //         });
        
        // }
    
}}

