package br.com.foxinline.infrastructure.repository;

import java.util.Optional;

import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.model.endereco.Estado;
import br.com.foxinline.domain.repository.EnderecoRepository;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnderecoRepositoryImpl implements EnderecoRepository, PanacheRepository<Endereco> {

    @Override
    public Uni<Endereco> findByIdAtivo(Long enderecoId) {
        return find("id = :id and ativo = true", Parameters.with("id", enderecoId)).firstResult();
    }

  

   
}
