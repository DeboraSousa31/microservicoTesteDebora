package br.com.foxinline.application.usecase.situacaoProprietario;

import br.com.foxinline.application.dto.request.SituacaoProprietarioRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.SituacaoProprietario;
import br.com.foxinline.domain.repository.SituacaoProprietarioRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveSituacaoProprietarioUseCase {

        @Inject
        SituacaoProprietarioRepository proprietarioRepository;

        @Inject
        CredenciadaFindByIdAtivoUseCase credenciadaFindByIdAtivoUseCase;

        @Inject
        UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

        public Uni<SituacaoProprietario> execute(SituacaoProprietarioRequestDTO request) {

                return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário Não encontrado"))
                                .onItem().transformToUni(usuario -> {
                                        return credenciadaFindByIdAtivoUseCase
                                                        .execute(request.getIdCredenciadaSituacaoProprietario())
                                                        .onItem().ifNull()
                                                        .failWith(() -> new NotFoundException(
                                                                        "Credenciada Não encontrada ou inativa"))
                                                        .onItem().ifNotNull().transformToUni(credenciada -> {
                                                                return proprietarioRepository.save(request.getNome(),
                                                                                request.getDescricao(), credenciada,
                                                                                usuario);
                                                        });
                                });
        }

}
