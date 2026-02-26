package br.com.foxinline.shared.config;

import java.util.HashSet;
import java.util.Set;

import br.com.foxinline.adapter.in.TipoComodoResource;
import br.com.foxinline.shared.security.JwtAuthFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        // 🔒 Filtro de autenticação
        resources.add(JwtAuthFilter.class);
        
        // ✅ Registra o recurso REST
        resources.add(TipoComodoResource.class);
        
        return resources;
    }
}
