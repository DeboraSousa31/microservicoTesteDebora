package br.com.foxinline.application.usecase.status;

import br.com.foxinline.application.dto.request.StatusRequestDTO;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.StatusRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RemoverStatusUseCase{

    @Inject
    StatusRepository statusRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    



    public Uni<Void> execute(StatusRequestDTO request){
        if(request.getId() == null || request.getIdUsuario() == null){
            return Uni.createFrom().voidItem();
        }
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(new NotFoundException("of"))
                .onItem().transformToUni(usuario ->{
                    Credenciada credenciadaDoUsuario = usuario.getCredenciada();
                    if(credenciadaDoUsuario == null){
                        return Uni.createFrom().failure(new NotFoundException("Usuario nao possui credenciada associada"));
                    }
                    return statusRepository.remove(request.getId(), usuario);
                });
      
    } 

}