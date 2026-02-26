package br.com.foxinline.application.usecase.tipoOrgao;




import br.com.foxinline.domain.model.TipoOrgao;

import br.com.foxinline.domain.repository.TipoOrgaoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TipoOrgaoFindByIdAtivosUseCase {

   @Inject
    TipoOrgaoRepository tipoOrgaoRepository;
    


    public Uni<TipoOrgao> execute(Long credenciadaId) {
        return tipoOrgaoRepository.findByIdAtivo(credenciadaId);
    }
}
