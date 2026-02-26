package br.com.foxinline.domain.repository;

import java.util.List;

import br.com.foxinline.application.dto.response.TipoOrgaoResponseDTO;
import br.com.foxinline.domain.model.TipoOrgao;
import io.smallrye.mutiny.Uni;

public interface TipoOrgaoRepository {
    Uni<List<TipoOrgaoResponseDTO>> findAllAtivos(String nome);
    Uni<TipoOrgao> findByIdAtivo(Long orgaoId);
    
} 