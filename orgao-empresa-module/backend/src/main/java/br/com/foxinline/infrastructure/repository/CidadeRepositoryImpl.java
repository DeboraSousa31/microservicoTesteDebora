package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.ap.internal.model.Mapper;
import br.com.foxinline.application.dto.response.CidadeResponseDTO;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.repository.CidadeRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CidadeRepositoryImpl implements CidadeRepository, PanacheRepository<Cidade>{

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    OrgaoMapper mapper;

    @Override
    public Uni<List<CidadeResponseDTO>> findAllAtivos(String nomeCidade) {
        Map<String, Object> params = new HashMap<>();
        
        String query = "FROM Cidade c LEFT JOIN FETCH c.estado e WHERE c.ativo = true";

        if(nomeCidade != null && Utils.isNotEmpty(nomeCidade)){
            query += " AND UPPER(c.nome) like UPPER(:nomeCidade)";
            params.put("nomeCidade", "%"+ nomeCidade + "%");
        }

        return find(query, params)
                .list()
                .onItem().transform(listaDeCidades -> 
                    listaDeCidades.stream()
                        .map(mapper::toLookupCidadeDTO) 
                        .collect(Collectors.toList())
                );
    }

    @Override
    public Uni<Cidade> findByIdAtivo(Long cidadeId) {
        return find("id = :id and ativo = true", Parameters.with("id", cidadeId)).firstResult();
    }

    @Override
    public Uni<Cidade> findByCodigoIBGE(String codigoIBGE) {
       return find("""
        select c from Cidade c
        left join fetch c.estado
        where c.codigoIBGE = ?1
          and c.ativo = true
        """, codigoIBGE)
        .firstResult();
    }

    @Override
    public Uni<Cidade> save(Cidade cidade, Usuario usuario) {
        return persist(cidade).onItem()
                .transformToUni(entidadePersistida -> auditoriaService.auditar(
                    null,
                    entidadePersistida,
                    "CREATE",
                    usuario.getNome(),
                    usuario.getCredenciada().getId())
                    .replaceWith(entidadePersistida));   
    }
  
    
}
