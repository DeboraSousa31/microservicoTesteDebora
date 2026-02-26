package br.com.foxinline.infrastructure.repository;

import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.repository.UsuarioRepository;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UsuarioRepositoryImpl implements UsuarioRepository, PanacheRepository<Usuario> {

    @Override
    public Uni<Usuario> findByIdAtivo(Long idUsuario) {
        return find("id = :id and ativo = true", Parameters.with("id", idUsuario)).firstResult()
        .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado. Id: " + idUsuario));
    }
    
}
