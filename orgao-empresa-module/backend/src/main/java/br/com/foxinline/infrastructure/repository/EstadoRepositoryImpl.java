package br.com.foxinline.infrastructure.repository;

import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.model.endereco.Estado;
import br.com.foxinline.domain.repository.EnderecoRepository;
import br.com.foxinline.domain.repository.EstadoRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class EstadoRepositoryImpl implements EstadoRepository, PanacheRepository<Estado> {

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Override
    @Transactional
    public Uni<Estado> save(Estado estado, Usuario usuario) {

        return persist(estado).onItem()
                .transformToUni(entidadePersistida -> auditoriaService.auditar(
                    null,
                    entidadePersistida,
                    "CREATE",
                    usuario.getNome(),
                    usuario.getCredenciada().getId())
                    .replaceWith(entidadePersistida));   
    
    }
    @Override
    public Uni<Estado> findByUf(String uf) {
        return find("uf", uf).firstResult();
    }
    @Override
    public Uni<Estado> findByIdAtivo(Long idEstado) {
        return find("id = :id and ativo = true", Parameters.with("id", idEstado)).firstResult();
    }
    
    
}