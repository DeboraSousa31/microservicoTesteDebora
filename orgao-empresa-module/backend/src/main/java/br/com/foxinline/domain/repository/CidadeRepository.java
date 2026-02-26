package br.com.foxinline.domain.repository;

import java.util.List;
import br.com.foxinline.application.dto.response.CidadeResponseDTO;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Cidade;
import io.smallrye.mutiny.Uni;

public interface CidadeRepository {
    Uni<List<CidadeResponseDTO>> findAllAtivos(String nome);
    Uni<Cidade> findByIdAtivo(Long cidadeId);
    Uni<Cidade> findByCodigoIBGE(String codigoIBGE);
    Uni<Cidade> save(Cidade cidade, Usuario usuario);
    
}