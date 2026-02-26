package br.com.foxinline.application.usecase.situacaoJuridica;


import br.com.foxinline.application.dto.request.SituacaoJuridicaRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.SituacaoJuridica;
import br.com.foxinline.domain.repository.SituacaoJuridicaRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
public class SaveSituacaoJuridicaUseCase {
    

    @Inject
    SituacaoJuridicaRepository situacaoJuridicaRepository;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    public Uni<SituacaoJuridica> execute(SituacaoJuridicaRequestDTO request){
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                        .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado"))
                        .onItem().transformToUni(usuario -> {
                                return credenciadaFindByIdAtivosUseCase
                                                .execute(request.getIdCredenciadaSituacaoJuridica())
                                                .onItem().ifNull()
                                                .failWith(() -> new NotFoundException("Credenciada não encontrada ou inativa"))
                                                .onItem().ifNotNull().transformToUni(credenciada -> {
                                                        return situacaoJuridicaRepository.save(request.getNome(), request.getDescricao(), credenciada, usuario);
                                                });
                        });
    }
}
