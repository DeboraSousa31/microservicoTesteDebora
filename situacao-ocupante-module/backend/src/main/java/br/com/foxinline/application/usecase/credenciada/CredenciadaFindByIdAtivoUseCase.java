package br.com.foxinline.application.usecase.credenciada;

import br.com.foxinline.domain.mapper.CredenciadaMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.repository.CredenciadaRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CredenciadaFindByIdAtivoUseCase {

    @Inject
    CredenciadaRepository credenciadaRepository;

    @Inject
    CredenciadaMapper mapper;

    public Uni<Credenciada> execute(Long credenciadaId) {
        return credenciadaRepository.findByIdAtivo(credenciadaId);
    }
}
