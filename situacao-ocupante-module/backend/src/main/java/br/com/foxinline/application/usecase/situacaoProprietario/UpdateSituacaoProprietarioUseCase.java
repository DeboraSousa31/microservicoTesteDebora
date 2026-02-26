package br.com.foxinline.application.usecase.situacaoProprietario;

import br.com.foxinline.application.dto.request.SituacaoProprietarioRequestDTO;
import br.com.foxinline.application.dto.response.SituacaoProprietarioResponseDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.SituacaoProprietarioMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.SituacaoProprietarioRepository;
import br.com.foxinline.shared.util.Utils;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateSituacaoProprietarioUseCase {

    @Inject
    SituacaoProprietarioRepository situacaoProprietarioRepository;

    @Inject
    CredenciadaFindByIdAtivoUseCase credenciadaFindByIdAtivoUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    SituacaoProprietarioMapper mapper;

    public Uni<SituacaoProprietarioResponseDTO> execute(SituacaoProprietarioRequestDTO request) {

        validarRequest(request);

        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário Não encontrado"))
                .onItem()
                .transformToUni(usuario -> {

                    Uni<Credenciada> credenciadaSituacaoProprietario;

                    if (request.getIdCredenciadaSituacaoProprietario() != null) {
                        credenciadaSituacaoProprietario = credenciadaFindByIdAtivoUseCase
                                .execute(request.getIdCredenciadaSituacaoProprietario())
                                .onItem().ifNull()
                                .failWith(() -> new NotFoundException(
                                        "Credenciada a ser associada (SituacaoProprietario) não encontrada com ID: "
                                                + request.getIdCredenciadaSituacaoProprietario()));
                    } else {
                        credenciadaSituacaoProprietario = Uni.createFrom().item(null);
                    }

                    return credenciadaSituacaoProprietario.onItem()
                            .transformToUni(novaCredenciadaDaSituacaoProprietario -> {
                                return situacaoProprietarioRepository.update(
                                        request.getId(),
                                        request.getNome(),
                                        request.getDescricao(),
                                        novaCredenciadaDaSituacaoProprietario,
                                        usuario).map(mapper::toResponse);
                            });
                });
    }

    private void validarRequest(SituacaoProprietarioRequestDTO request) {
        if (request.getNome() == null || Utils.isEmpty(request.getNome())) {
            throw new BadRequestException("Nome não pode estar vazio.");
        }

        if (request.getDescricao() == null || Utils.isEmpty(request.getDescricao())) {
            throw new BadRequestException("Descricao não pode estar vazia.");
        }
    }

}