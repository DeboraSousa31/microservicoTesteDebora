package br.com.foxinline.application.usecase.estado;

import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.model.endereco.Estado;
import br.com.foxinline.domain.repository.CidadeRepository;
import br.com.foxinline.domain.repository.EstadoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EstadoFindByUfUseCase {
    @Inject
    EstadoRepository estadoRepository;

    public Uni<Estado> execute(String uf){
        return estadoRepository.findByUf(uf);
    }
}

