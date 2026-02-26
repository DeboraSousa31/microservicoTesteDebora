package br.com.foxinline.application.usecase.estado;

import br.com.foxinline.application.dto.request.EstadoRequestDTO;
import br.com.foxinline.application.dto.response.EstadoResponseDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.EstadoMapper;
import br.com.foxinline.domain.repository.EstadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateEstadoUseCase {
    

    @Inject
    EstadoRepository estadoRepository;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;


    @Inject
    EstadoMapper mapper;

    public Uni<EstadoResponseDTO> execute(EstadoRequestDTO request){
       return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(new NotFoundException("Usuário não encontrado."))
                .onItem().transformToUni(usuario ->{
        
                            return estadoRepository.update(request.getId(), request.getNome(), request.getUf(),usuario.getCredenciada(),usuario).map(mapper::toResponse);

                        });
                    
    }
}

