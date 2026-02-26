package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Usuario;
import io.smallrye.mutiny.Uni;

public interface UsuarioRepository {
    Uni<Usuario> findByIdAtivo(Long idUsuario);
}
