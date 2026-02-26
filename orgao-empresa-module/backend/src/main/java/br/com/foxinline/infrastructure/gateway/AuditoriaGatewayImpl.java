package br.com.foxinline.infrastructure.gateway;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.adapter.out.restcliente.AuditoriaRestClient;
import br.com.foxinline.application.dto.AuditoriaDTO;
import br.com.foxinline.domain.gateway.AuditoriaGateway;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuditoriaGatewayImpl implements AuditoriaGateway {

    @Inject
    @RestClient
    AuditoriaRestClient auditoriaRestClient;

    @Inject
    ObjectMapper objectMapper; 

    @Override
    public Uni<Void> enviarAuditoria(AuditoriaDTO auditoriaDTO) {
        return auditoriaRestClient.enviarAuditoria(auditoriaDTO).onFailure().invoke(e -> {
            System.err.println("Falha ao registrar auditoria: " + e.getMessage());
        });
    }

}