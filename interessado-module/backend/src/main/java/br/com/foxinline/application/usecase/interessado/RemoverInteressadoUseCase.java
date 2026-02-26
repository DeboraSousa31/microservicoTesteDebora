package br.com.foxinline.application.usecase.interessado;

import br.com.foxinline.application.dto.request.InteressadoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.InteressadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RemoverInteressadoUseCase {
    @Inject
    InteressadoRepository interessadoRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;


    public Uni<Void> execute(InteressadoRequestDTO request){
        if(request.getId() == null || request.getIdUsuario() == null){
            return Uni.createFrom().voidItem();
        }

        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
            .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado."))
            .onItem().transformToUni(usuario ->{
                Credenciada credenciadaDoUsuario = new Credenciada();
                if(credenciadaDoUsuario == null){
                    return Uni.createFrom().failure(new NotFoundException("Usuario nao possui credenciada associada"));
                }
    
                return interessadoRepository.remove(request.getId(), usuario).replaceWithVoid();
            });
    }
}
