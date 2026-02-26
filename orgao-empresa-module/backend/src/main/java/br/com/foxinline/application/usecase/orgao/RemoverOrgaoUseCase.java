package br.com.foxinline.application.usecase.orgao;

import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.OrgaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RemoverOrgaoUseCase {
    @Inject
    OrgaoRepository orgaoRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    public Uni<Void> execute(OrgaoRequestDTO request){
        if(request.getId() == null || request.getIdUsuario() == null){
            return Uni.createFrom().voidItem();
        }


        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado"))
                .onItem().transformToUni(usuario ->{
                    Credenciada credenciadaDoUsuario = usuario.getCredenciada();
                    if(credenciadaDoUsuario == null){
                        return Uni.createFrom().failure(new NotFoundException("Usuário nao possui credenciada associada"));
                    }
                    return orgaoRepository.remove(request.getId(), usuario).replaceWithVoid();
                });
    }
}
