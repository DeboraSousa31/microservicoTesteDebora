package br.com.foxinline.domain.repository;


import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.model.endereco.Estado;
import io.smallrye.mutiny.Uni;

public interface EnderecoRepository {
    Uni<Endereco> findByIdAtivo(Long enderecoId);
   
}
