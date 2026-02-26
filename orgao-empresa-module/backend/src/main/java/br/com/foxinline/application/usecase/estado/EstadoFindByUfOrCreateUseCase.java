package br.com.foxinline.application.usecase.estado;

import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Estado;
import br.com.foxinline.domain.repository.EstadoRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EstadoFindByUfOrCreateUseCase {
    @Inject
    EstadoRepository estadoRepository;

    @Inject
    EstadoFindByUfUseCase estadoFindByUfUseCase;

    public Uni<Estado> execute(String uf, String nome, Usuario usuario) {
        return estadoFindByUfUseCase.execute(uf)
            .onItem().transformToUni(estado -> {
                 if (estado != null) {
                    return Uni.createFrom().item(estado);
                }
                
                Estado novoEstado = new Estado();
                novoEstado.setUf(uf);
                novoEstado.setNome(nome);
                novoEstado.setAtivo(true);
                
                return estadoRepository.save(novoEstado, usuario);
            });
    }
}