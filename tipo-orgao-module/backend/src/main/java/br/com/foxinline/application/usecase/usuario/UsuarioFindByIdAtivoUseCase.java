package br.com.foxinline.application.usecase.usuario;

import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.repository.UsuarioRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class UsuarioFindByIdAtivoUseCase {
    @Inject
    UsuarioRepository usuarioRepository;

    public Uni<Usuario> execute(Long idUsuario) {

        if (idUsuario == null) {
            throw new BadRequestException("idUsuario vazio");
        }
        
        return usuarioRepository.findByIdAtivo(idUsuario);
    }
    
}
