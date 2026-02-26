package br.com.foxinline.application.usecase.tipoComodo;

import br.com.foxinline.application.dto.request.TipoComodoRequestDTO;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.TipoComodoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RemoverTipoComodoUseCase {

    
    @Inject
    TipoComodoRepository tipoComodoRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    public Uni<Void> execute(TipoComodoRequestDTO requestDTO){
        if(requestDTO.getId() == null || requestDTO.getIdUsuario() == null){
            return Uni.createFrom().voidItem();
        }
        return usuarioFindByIdAtivoUseCase.execute(requestDTO.getIdUsuario())
            .onItem().ifNull().failWith(new NotFoundException())
            .onItem().transformToUni(usuario ->{
                Credenciada credenciadaDoUsuario = usuario.getCredenciada();
                if(credenciadaDoUsuario == null){
                    return Uni.createFrom().failure(new NotFoundException("Usuario nao possui credenciada associada"));
                }
                return tipoComodoRepository.remove(requestDTO.getId(), usuario);
            });
    }


}
