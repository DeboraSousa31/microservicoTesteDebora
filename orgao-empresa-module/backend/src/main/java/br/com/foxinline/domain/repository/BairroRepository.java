package br.com.foxinline.domain.repository;

import java.util.List;
import br.com.foxinline.application.dto.response.BairroResponseDTO;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import io.smallrye.mutiny.Uni;

public interface BairroRepository {
    Uni<List<BairroResponseDTO>> findAllAtivos(Long cidadeId);
    Uni<Bairro> findByIdAtivo(Long bairroId);
    Uni<List<Bairro>> findByCidadeId(Long cidadeId);
    Uni<Bairro> findByIdAtivoComAssociacoes(Long bairroId);
    Uni<Bairro> findByNomeAndCidadeId(String nome, Long cidadeId);
    Uni<Bairro> save(Bairro bairro, Usuario usuario);
    Uni<Bairro> findByNomeAndCidade(String nome, Cidade cidade);
}
