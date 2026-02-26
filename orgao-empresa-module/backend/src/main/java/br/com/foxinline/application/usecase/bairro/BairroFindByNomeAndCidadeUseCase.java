package br.com.foxinline.application.usecase.bairro;

import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.repository.BairroRepository;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

public class BairroFindByNomeAndCidadeUseCase {
    @Inject
    BairroRepository bairroRepository;


    public Uni<Bairro> execute(String nomeBairro, Cidade cidade) {
    
        if (nomeBairro != null || cidade == null || cidade.getId() == null) {
        
            return Uni.createFrom().nullItem();
        }

 
  
        return bairroRepository.findByNomeAndCidade(nomeBairro, cidade);
    }
}
