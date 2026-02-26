package br.com.foxinline.domain.repository;

import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Estado;
import io.smallrye.mutiny.Uni;

public interface EstadoRepository {
    Uni<Estado> save(Estado estado, Usuario usuario);
    Uni<Estado> findByUf(String uf);
    Uni<Estado> findByIdAtivo(Long idEstado);
}
