package br.com.foxinline.domain.gateway;

import br.com.foxinline.application.dto.AuditoriaDTO;
import io.smallrye.mutiny.Uni;

public interface AuditoriaGateway {
    Uni<Void> enviarAuditoria(AuditoriaDTO auditoriaDTO);
}
