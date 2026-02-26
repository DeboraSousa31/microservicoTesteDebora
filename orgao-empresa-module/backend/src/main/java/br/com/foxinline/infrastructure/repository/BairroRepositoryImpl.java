package br.com.foxinline.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.foxinline.application.dto.response.BairroResponseDTO;
import br.com.foxinline.application.dto.response.CidadeResponseDTO;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.repository.BairroRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class BairroRepositoryImpl implements BairroRepository, PanacheRepository<Bairro> {

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Override
    public Uni<List<BairroResponseDTO>> findAllAtivos(Long cidadeId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT b.id, b.nome ");
        queryBuilder.append("FROM Bairro b WHERE b.ativo = true");

        if(cidadeId != null){
            queryBuilder.append(" and b.cidade.id = :cidadeId");
            params.put("cidadeId", cidadeId);
        
        }
        return find(queryBuilder.toString(), params)
                .project(BairroResponseDTO.class)
                .list();
    }

    @Override
    public Uni<Bairro> findByIdAtivo(Long bairroId) {
        return find("id = :id and ativo = true", Parameters.with("id", bairroId)).firstResult();
    }
   

    @Override
    public Uni<List<Bairro>> findByCidadeId(Long cidadeId) {
       // return find("cidade.id", cidadeId).list();
        String query = """
        SELECT b FROM Bairro b
        LEFT JOIN FETCH b.cidade c
        LEFT JOIN FETCH c.estado e
        WHERE b.cidade.id = :cidadeId AND b.ativo = true
    """;
    
    return find(query, Parameters.with("cidadeId", cidadeId)).list();
    }

    @Override
    public Uni<Bairro> findByIdAtivoComAssociacoes(Long bairroId) {
    String query = "FROM Bairro b LEFT JOIN FETCH b.cidade c LEFT JOIN FETCH c.estado e WHERE b.id = ?1 AND b.ativo = true";
    return find(query, bairroId).firstResult();
    }

    @Override
    public Uni<Bairro> findByNomeAndCidadeId(String nome, Long cidadeId) {
        return find("nome = ?1 and cidade.id = ?2", nome, cidadeId).firstResult();
       
    }

    @Override
    public Uni<Bairro> save(Bairro bairro, Usuario usuario) {
        return persist(bairro).onItem()
                .transformToUni(entidadePersistida -> auditoriaService.auditar(
                    null,
                    entidadePersistida,
                    "CREATE",
                    usuario.getNome(),
                    usuario.getCredenciada().getId())
                    .replaceWith(entidadePersistida));   
    }

    @Override
    public Uni<Bairro> findByNomeAndCidade(String nome, Cidade cidade) {
        return find("nome = ?1 and cidade = ?2", nome, cidade).firstResult();
    }
}
