package br.com.foxinline.application.usecase.interessado;

import br.com.foxinline.application.dto.request.InteressadoRequestDTO;
import br.com.foxinline.application.dto.response.InteressadoResponseDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.InteressadoMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.InteressadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateInteressadoUseCase {
    @Inject
    InteressadoRepository interessadoRepository;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    InteressadoMapper mapper;


    public Uni<InteressadoResponseDTO> execute(InteressadoRequestDTO request){
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado."))
                .onItem().transformToUni(usuario ->{
                    Uni<Credenciada> credenciadaInteressado;
                    if(request.getIdCredenciadaInteressado() != null){
                        credenciadaInteressado = credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaInteressado()).onItem().ifNull().failWith(()-> new NotFoundException("Credenciada a ser associada (Interessado) não encontrado com ID:"+ request.getIdCredenciadaInteressado()));
                    }else{
                        credenciadaInteressado = Uni.createFrom().item(null);
                    }

                    return credenciadaInteressado.onItem().transformToUni(novaCredenciada ->{
                        return interessadoRepository.update(
                            request.getId(),
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
                            novaCredenciada,
                            usuario
                        ).map(mapper:: toResponse);
                    });
                });
    }
}
