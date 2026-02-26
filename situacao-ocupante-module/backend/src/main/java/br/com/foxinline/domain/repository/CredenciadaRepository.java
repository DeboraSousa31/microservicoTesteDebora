package br.com.foxinline.domain.repository;

import java.util.List;

import br.com.foxinline.application.dto.response.CredenciadaResponseDTO;
import br.com.foxinline.domain.model.Credenciada;
import io.smallrye.mutiny.Uni;

public interface CredenciadaRepository {
    Uni<List<CredenciadaResponseDTO>> findAllAtivos(String nome);
    Uni<Credenciada> findByIdAtivo(Long credenciadaId);
}
