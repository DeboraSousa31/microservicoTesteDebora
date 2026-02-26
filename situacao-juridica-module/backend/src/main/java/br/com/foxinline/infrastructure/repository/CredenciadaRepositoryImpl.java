package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.foxinline.application.dto.response.CredenciadaResponseDTO;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.CredenciadaRepository;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class CredenciadaRepositoryImpl implements CredenciadaRepository, PanacheRepository<Credenciada> {



    @Override
    public Uni<List<CredenciadaResponseDTO>> findAllAtivos(String nomeCredenciada) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT c.id, c.usuario.nome ");
        query.append("FROM Credenciada c WHERE c.usuario.ativo = true");

        if(nomeCredenciada != null && Utils.isNotEmpty(nomeCredenciada)){
            query.append("AND UPPER(c.usuario.nome) like UPPER(:nomeCredenciada)");
            params.put("nomeCredenciada","%" + nomeCredenciada + "%");
        }

        return find(query.toString(), params)
                .project(CredenciadaResponseDTO.class)
                .list();

    }

    @Override
    public Uni<Credenciada> findByIdAtivo(Long credenciadaId) {
        return find("id = :id and ativo = true", Parameters.with("id", credenciadaId)).firstResult();
    }
    
    
}
