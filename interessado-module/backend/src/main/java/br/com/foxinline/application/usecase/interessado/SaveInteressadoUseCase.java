package br.com.foxinline.application.usecase.interessado;

import br.com.foxinline.application.dto.request.InteressadoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Interessado;
import br.com.foxinline.domain.repository.InteressadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SaveInteressadoUseCase {
    @Inject
    InteressadoRepository interessadoRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;
    
    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    public Uni<Interessado> execute(InteressadoRequestDTO request){
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                        .onItem().ifNull().failWith(new NotFoundException("Usuário não encontrado"))
                        .onItem().transformToUni(usuario -> {
                                return credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaInteressado())
                                    .onItem().ifNull()
                                    .failWith(() -> new NotFoundException("Credenciada não encontrada ou inativa"))
                                    .onItem().ifNotNull().transformToUni(credenciada -> {
                                       
                                            return interessadoRepository.save(
                                                request.getNome(),
                                                request.getCpf(),
                                                request.getCnpj(),
                                                request.getEmail(),
                                                request.getProfissao(),
                                                request.getTelefone(),
                                                request.getNomeFantasia(),
                                                request.getRazaoSocial(),
                                                request.getOrgaoEmissor(),
                                                request.getDataNascimento(),
                                                request.getRg(),
                                                request.getTipoDocumento(),
                                                credenciada,
                                                usuario

                                                );
                                    });
                        });
    }
}
