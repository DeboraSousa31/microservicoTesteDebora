package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import br.com.foxinline.application.dto.response.TipoOrgaoResponseDTO;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.repository.TipoOrgaoRepository;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TipoOrgaoRepositoryImpl implements TipoOrgaoRepository, PanacheRepository<TipoOrgao> {

    @Override
    public Uni<List<TipoOrgaoResponseDTO>> findAllAtivos(String nomeTipoOrgao) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT t.id, t.nome ");
        query.append("FROM TipoOrgao t WHERE t.ativo = true");

        if(nomeTipoOrgao != null && Utils.isNotEmpty(nomeTipoOrgao)){
            query.append(" AND UPPER(t.nome) like UPPER(:nomeTipoOrgao)");
            params.put("nomeTipoOrgao","%" + nomeTipoOrgao + "%");
        }

        return find(query.toString(), params)
                .project(TipoOrgaoResponseDTO.class)
                .list();
    }

    @Override
    public Uni<TipoOrgao> findByIdAtivo(Long orgaoId) {
        return find("id = :id and ativo = true", Parameters.with("id", orgaoId)).firstResult();
    }
    
}
