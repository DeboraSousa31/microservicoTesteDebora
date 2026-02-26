package br.com.foxinline.application.usecase.situacaoJuridica;

import br.com.foxinline.application.dto.request.SituacaoJuridicaRequestDTO;
import br.com.foxinline.application.dto.response.SituacaoJuridicaResponseDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.SituacaoJuridicaMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.SituacaoJuridicaRepository;
import br.com.foxinline.shared.util.Utils;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateSituacaoJuridicaUseCase {
    
    @Inject
    SituacaoJuridicaRepository situacaoJuridicaRepository;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    SituacaoJuridicaMapper mapper;

    public Uni<SituacaoJuridicaResponseDTO> execute(SituacaoJuridicaRequestDTO request){
        validarRequest(request);
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado"))
                .onItem()
                .transformToUni(usuario -> {
                    
                    Uni<Credenciada> credenciadaSituacaoJuridica;
                    
                    if(request.getIdCredenciadaSituacaoJuridica() != null){
                        credenciadaSituacaoJuridica = credenciadaFindByIdAtivosUseCase
                                .execute(request.getIdCredenciadaSituacaoJuridica())
                                .onItem().ifNull()
                                .failWith(() -> new NotFoundException("Credenciada a ser associada (SituacaoJuridica) não encontrado com ID: " + request.getIdCredenciadaSituacaoJuridica()));

                    }else {
                        credenciadaSituacaoJuridica = Uni.createFrom().item(null);
                    }

                    return credenciadaSituacaoJuridica.onItem()
                            .transformToUni(novaCredenciadaDaSituacaoJuridica ->{
                                return situacaoJuridicaRepository.update(
                                    request.getId(),
                                    request.getNome(),
                                    request.getDescricao(),
                                    novaCredenciadaDaSituacaoJuridica,
                                    usuario).map(mapper:: toResponse);
                            });
                });


        }
        private void validarRequest(SituacaoJuridicaRequestDTO request) {
        if (request.getNome() == null || Utils.isEmpty(request.getNome())) {
            throw new BadRequestException("Nome não pode estar vazio.");
        }

        if (request.getDescricao() == null || Utils.isEmpty(request.getDescricao())) {
            throw new BadRequestException("Descricao não pode estar vazia.");
        }
    }
}
