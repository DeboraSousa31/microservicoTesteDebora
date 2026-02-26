package br.com.foxinline.application.usecase.estado;

import br.com.foxinline.application.dto.request.EstadoRequestDTO;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Estado;
import br.com.foxinline.domain.repository.EstadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveEstadoUseCase {

    @Inject
    EstadoRepository estadoRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    

    public Uni<Estado> execute(EstadoRequestDTO request){
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(()-> new NotFoundException())
                .onItem().ifNotNull().transformToUni(usuario ->{
                return estadoRepository.save(request.getNome(), request.getUf(), usuario.getCredenciada(), usuario);
                });
    }
    
}
