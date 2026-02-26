package br.com.foxinline.shared.security;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        // 1️⃣ Pega token da header ou query parameter
        String authHeader = ctx.getHeaderString("Authorization");
        String tokenCriptografado = (authHeader != null && authHeader.startsWith(BEARER_PREFIX))
                ? authHeader.substring(BEARER_PREFIX.length()).trim()
                : ctx.getUriInfo().getQueryParameters().getFirst("token");

        if (tokenCriptografado == null || tokenCriptografado.isEmpty()) {
            logger.warning("Token ausente. Bloqueando acesso!");
            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("Acesso negado: Autenticação JWT obrigatória.").build());
            return;
        }

        try {
            // 2️⃣ Descriptografa o JWT com AES
            String tokenDescriptografado = CriptografiaUtils.decrypt(tokenCriptografado);

            logger.info("Token descriptografado: " + tokenDescriptografado.substring(0, Math.min(20, tokenDescriptografado.length())) + "...");

            // 3️⃣ Valida o JWT (assinatura HMAC-SHA256)
            Claims claims = CriptografiaUtils.validateToken(tokenDescriptografado);
            String username = claims.getSubject();

            // 4️⃣ Armazena info do usuário no contexto da requisição
            ctx.setProperty("usuarioLogado", username);

            logger.info("Token válido! Usuário: " + username);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Token inválido ou expirado!", e);
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Acesso não autorizado: Token inválido ou expirado.").build());
        }
    }
}
