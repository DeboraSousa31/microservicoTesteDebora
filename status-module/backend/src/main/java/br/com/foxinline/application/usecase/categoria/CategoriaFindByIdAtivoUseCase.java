package br.com.foxinline.application.usecase.categoria;

import br.com.foxinline.domain.model.Categoria;
import br.com.foxinline.domain.repository.CategoriaRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoriaFindByIdAtivoUseCase {

    @Inject
    CategoriaRepository categoriaRepository;

    public Uni<Categoria> execute(Long categoriaId) {
        return categoriaRepository.findByIdAtivo(categoriaId);
    }
}
