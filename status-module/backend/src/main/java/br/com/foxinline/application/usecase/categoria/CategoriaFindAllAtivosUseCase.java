package br.com.foxinline.application.usecase.categoria;

import java.util.List;

import br.com.foxinline.application.dto.response.CategoriaResponseDTO;
import br.com.foxinline.domain.repository.CategoriaRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoriaFindAllAtivosUseCase {
    @Inject
    CategoriaRepository categoriaRepository;

 

    public Uni<List<CategoriaResponseDTO>> execute(String nome){
        return categoriaRepository.findAllAtivos(nome);
    }
}
