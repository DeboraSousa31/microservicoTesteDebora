package br.com.foxinline.application.usecase.auditoria;

import br.com.foxinline.application.dto.AuditoriaDTO;
import br.com.foxinline.domain.gateway.AuditoriaGateway;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RegistrarAuditoriaUseCase {

    @Inject
    AuditoriaGateway auditoriaGateway;

    public Uni<Void> execute(AuditoriaDTO auditoriaDTO) {
        return auditoriaGateway.enviarAuditoria(auditoriaDTO);
    }
    
}