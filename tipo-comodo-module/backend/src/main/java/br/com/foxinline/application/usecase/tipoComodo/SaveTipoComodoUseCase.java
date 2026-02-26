package br.com.foxinline.application.usecase.tipoComodo;

import br.com.foxinline.application.dto.request.TipoComodoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.TipoComodo;
import br.com.foxinline.domain.repository.TipoComodoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveTipoComodoUseCase {

    @Inject
    TipoComodoRepository tipoComodoRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    public Uni<TipoComodo> execute(TipoComodoRequestDTO request){
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(()-> new NotFoundException())
                .onItem().ifNotNull().transformToUni(usuario ->{
                    
                    return credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaTipoComodo())
                            .onItem().transformToUni(credenciada ->{
                                if(credenciada == null){
                                    return tipoComodoRepository.save(request.getNomeSingular(), request.getNomePlural(), null, usuario);
                                }else{
                                    return tipoComodoRepository.save(request.getNomeSingular(), request.getNomePlural(), credenciada, usuario);
                                }
                            });
                });
    }
    
}
