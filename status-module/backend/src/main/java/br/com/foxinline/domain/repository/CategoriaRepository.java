package br.com.foxinline.domain.repository;

import java.util.List;

import br.com.foxinline.application.dto.response.CategoriaResponseDTO;
import br.com.foxinline.domain.model.Categoria;
import io.smallrye.mutiny.Uni;

public interface CategoriaRepository {
    Uni<List<CategoriaResponseDTO>> findAllAtivos(String nome);
    Uni<Categoria> findByIdAtivo(Long categoriaId);
}
