package br.com.foxinline.application.usecase.situacaoProprietario;

import br.com.foxinline.application.dto.request.SituacaoProprietarioRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.repository.SituacaoProprietarioRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RemoverSituacaoProprietarioUseCase {

    @Inject
    SituacaoProprietarioRepository proprietarioRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CredenciadaFindByIdAtivoUseCase credenciadaFindByIdAtivoUseCase;

    public Uni<Void> execute(SituacaoProprietarioRequestDTO request) {
        if (request.getId() == null || request.getIdUsuario() == null) {
            return Uni.createFrom().voidItem();
        }

        // return proprietarioRepository.remove(id, usuario);

        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário Não encontrado"))
                .onItem().transformToUni(usuario -> credenciadaFindByIdAtivoUseCase.execute(request.getIdCredenciadaUsuario())
                        .onItem().ifNull().failWith(() -> new NotFoundException("Credenciada Não encontrada"))
                        .onItem().transformToUni(credenciada -> {
                            return proprietarioRepository.remove(
                                    request.getId(),
                                    usuario).replaceWithVoid();
                        }));

    }
}
