package br.com.foxinline.application.usecase.atualizacao;

import br.com.foxinline.application.dto.request.AtualizacaoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.model.Atualizacao;
import br.com.foxinline.domain.repository.AtualizacaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SaveAtualizacaoUseCase {
    
    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    AtualizacaoRepository atualizacaoRepository;


    public Uni<Atualizacao> execute(AtualizacaoRequestDTO request){
        return atualizacaoRepository.save(request.getDescricao(), request.getDataAgendamento(), request.getTextoNovidade());
    }


}
