package br.com.foxinline.application.usecase.situacaoJuridica;

import br.com.foxinline.application.dto.request.SituacaoJuridicaRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.SituacaoJuridicaRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RemoverSituacaoJuridicaUseCase {
    
    @Inject
    SituacaoJuridicaRepository situacaoJuridicaRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivoUseCase;



    public Uni<Void> execute(SituacaoJuridicaRequestDTO request){
       
        if(request.getId() == null || request.getIdUsuario() == null){
            return Uni.createFrom().voidItem();
        }
        

        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário Não encontrado"))
                .onItem().transformToUni(usuario -> {
                    Credenciada credenciadaDoUsuario = usuario.getCredenciada();
                    if(credenciadaDoUsuario == null){
                        return Uni.createFrom().failure(new NotFoundException("Usuario nao possui credenciada associada"));
                    }
                    return situacaoJuridicaRepository.remove(request.getId(), usuario).replaceWithVoid();
    });

    }
    
}