package br.com.foxinline.application.usecase.tipoComodo;

import br.com.foxinline.application.dto.request.TipoComodoRequestDTO;
import br.com.foxinline.application.dto.response.TipoComodoResponseDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.TipoComodoMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.TipoComodoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateTipoComodoUseCase {
    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    TipoComodoRepository tipoComodoRepository;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    TipoComodoMapper mapper;

    public Uni<TipoComodoResponseDTO> execute(TipoComodoRequestDTO request){
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(new NotFoundException())
                .onItem().transformToUni(usuario ->{
                    Uni<Credenciada> credenciadaTipoComodo;
                    if(request.getIdCredenciadaTipoComodo() != null){
                        credenciadaTipoComodo = credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaTipoComodo()).onItem().ifNull().failWith(()-> new NotFoundException("Credenciada a ser associada (Status) não encontrado com ID:"+ request.getIdCredenciadaTipoComodo()));
                    }else{
                        credenciadaTipoComodo = Uni.createFrom().nullItem();
                    }
                    return credenciadaTipoComodo.onItem().transformToUni(credenciada ->{
                        return tipoComodoRepository.update(request.getId(), request.getNomeSingular(), request.getNomePlural(), credenciada, usuario).map(mapper::toResponse);
                    });

                });
    }


}
