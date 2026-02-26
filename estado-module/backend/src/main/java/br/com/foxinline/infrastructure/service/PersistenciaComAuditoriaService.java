package br.com.foxinline.infrastructure.service;

import java.time.Instant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.application.dto.AuditoriaDTO;
import br.com.foxinline.application.usecase.auditoria.RegistrarAuditoriaUseCase;
import br.com.foxinline.domain.model.EntidadeGenerica;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PersistenciaComAuditoriaService {

    @Inject
    RegistrarAuditoriaUseCase registrarAuditoriaUseCase;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    public <T extends EntidadeGenerica> Uni<Void> auditar(T entidadeAntiga, T entidadeNova, String acao,
            String nomeUsuario, Long idCredenciada) {

        String valorAntigo = null;
        String valorNovo = null;

        try {
        
            valorAntigo = entidadeAntiga != null ? objectMapper.writeValueAsString(entidadeAntiga) : null;
            valorNovo = entidadeNova != null ? objectMapper.writeValueAsString(entidadeNova) : null;
    
        } catch (JsonProcessingException e) {
            e.printStackTrace();
             return Uni.createFrom().failure(new RuntimeException("Erro ao serializar entidade", e));
        }

        Long idEntidade = null;

      
        idEntidade = (entidadeNova != null ? entidadeNova : entidadeAntiga).getId();
        
        String nomeEntidade = (entidadeNova != null ? entidadeNova : entidadeAntiga).getClass().getSimpleName();
        
        AuditoriaDTO auditoriaDTO = new AuditoriaDTO();
        auditoriaDTO.setUsuario(nomeUsuario);
        auditoriaDTO.setAcao(acao);
        auditoriaDTO.setNomeEntidade(nomeEntidade);
        auditoriaDTO.setId_Entidade(idEntidade);
        auditoriaDTO.setValorAntigo(valorAntigo);
        auditoriaDTO.setValorNovo(valorNovo);
        auditoriaDTO.setDataHora(Instant.now());
        auditoriaDTO.setId_Credenciada(idCredenciada);

        return registrarAuditoriaUseCase.execute(auditoriaDTO);
    }

}
