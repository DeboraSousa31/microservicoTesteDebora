package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.foxinline.application.dto.response.CategoriaResponseDTO;
import br.com.foxinline.domain.model.Categoria;
import br.com.foxinline.domain.repository.CategoriaRepository;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoriaRepositoryImpl implements CategoriaRepository, PanacheRepository<Categoria> {

    @Override
    public Uni<List<CategoriaResponseDTO>> findAllAtivos(String nome) {
         Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT c.id, c.nome ");
        query.append("FROM Categoria c WHERE c.ativo = true");

        if(nome != null && Utils.isNotEmpty(nome)){
            query.append("AND UPPER(c.nome) like UPPER(:nome)");
            params.put("nome","%" + nome + "%");
        }

        return find(query.toString(), params)
                .project(CategoriaResponseDTO.class)
                .list();

    }

    @Override
    public Uni<Categoria> findByIdAtivo(Long categoriaId) {
         return find("id = :id and ativo = true", Parameters.with("id", categoriaId)).firstResult();
    }

    

   
}
