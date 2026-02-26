package br.com.foxinline.application.usecase.tipoOrgao;

import br.com.foxinline.application.dto.request.TipoOrgaoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.repository.TipoOrgaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveTipoOrgaoIdUseCase {

        @Inject
        TipoOrgaoRepository proprietarioRepository;

        @Inject
        CredenciadaFindByIdAtivoUseCase credenciadaFindByIdAtivoUseCase;

        @Inject
        UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

        public Uni<TipoOrgao> execute(TipoOrgaoRequestDTO request) {

                return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário Não encontrado"))
                                .onItem().transformToUni(usuario -> {
                                        return credenciadaFindByIdAtivoUseCase
                                                        .execute(request.getIdCredenciadaTipoOrgao())
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
