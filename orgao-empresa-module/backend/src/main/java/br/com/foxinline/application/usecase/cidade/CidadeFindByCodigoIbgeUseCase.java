package br.com.foxinline.application.usecase.cidade;


import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.repository.CidadeRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CidadeFindByCodigoIbgeUseCase {
    @Inject
    CidadeRepository cidadeRepository;

    public Uni<Cidade> execute(String codigoIbge){
        if (codigoIbge == null || codigoIbge.trim().isEmpty()) {
            return Uni.createFrom().nullItem();
        }
        return cidadeRepository.findByCodigoIBGE(codigoIbge);
    }
}
