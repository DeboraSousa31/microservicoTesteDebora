package br.com.foxinline.adapter.out.restcliente;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.foxinline.application.dto.AuditoriaDTO;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auditoria")
@RegisterRestClient(configKey = "auditoria-api")
public interface AuditoriaRestClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Void> enviarAuditoria(AuditoriaDTO auditoriaDTO);
}
