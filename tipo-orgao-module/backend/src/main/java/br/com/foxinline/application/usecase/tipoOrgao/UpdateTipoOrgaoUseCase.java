package br.com.foxinline.application.usecase.tipoOrgao;

import br.com.foxinline.application.dto.request.TipoOrgaoRequestDTO;
import br.com.foxinline.application.dto.response.TipoOrgaoResponseDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.TipoOrgaoMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.TipoOrgaoRepository;
import br.com.foxinline.shared.util.Utils;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateTipoOrgaoUseCase {

    @Inject
    TipoOrgaoRepository tipoOrgaoRepository;

    @Inject
    CredenciadaFindByIdAtivoUseCase credenciadaFindByIdAtivoUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    TipoOrgaoMapper mapper;

    public Uni<TipoOrgaoResponseDTO> execute(TipoOrgaoRequestDTO request) {

        validarRequest(request);

        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário Não encontrado"))
                .onItem()
                .transformToUni(usuario -> {

                    Uni<Credenciada> credenciadaTipoOrgao;

                    if (request.getIdCredenciadaTipoOrgao() != null) {
                        credenciadaTipoOrgao = credenciadaFindByIdAtivoUseCase
                                .execute(request.getIdCredenciadaTipoOrgao())
                                .onItem().ifNull()
                                .failWith(() -> new NotFoundException(
                                        "Credenciada a ser associada (TipoOrgao) não encontrada com ID: "
                                                + request.getIdCredenciadaTipoOrgao()));
                    } else {
                        credenciadaTipoOrgao = Uni.createFrom().item(null);
                    }

                    return credenciadaTipoOrgao.onItem()
                            .transformToUni(novaCredenciadaDoTipoOrgao -> {
                                return tipoOrgaoRepository.update(
                                        request.getId(),
                                        request.getNome(),
                                        request.getDescricao(),
                                        novaCredenciadaDoTipoOrgao,
                                        usuario).map(mapper::toResponse);
                            });
                });
    }

    private void validarRequest(TipoOrgaoRequestDTO request) {
        if (request.getNome() == null || Utils.isEmpty(request.getNome())) {
            throw new BadRequestException("Nome não pode estar vazio.");
        }

        if (request.getDescricao() == null || Utils.isEmpty(request.getDescricao())) {
            throw new BadRequestException("Descricao não pode estar vazia.");
        }
    }

}