package br.com.foxinline.application.usecase.status;



import br.com.foxinline.application.dto.request.StatusRequestDTO;
import br.com.foxinline.application.dto.response.StatusResponseDTO;
import br.com.foxinline.application.usecase.categoria.CategoriaFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.StatusMapper;
import br.com.foxinline.domain.model.Categoria;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Status;
import br.com.foxinline.domain.repository.StatusRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateStatusUseCase {
    @Inject
    StatusRepository statusRepository;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    CategoriaFindByIdAtivoUseCase categoriaFindByIdAtivoUseCase;

    @Inject
    StatusMapper mapper;

    public Uni<StatusResponseDTO> execute(StatusRequestDTO request){
       return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(new NotFoundException("Usuário não encontrado."))
                .onItem().transformToUni(usuario ->{
                    Uni<Credenciada> credenciadaStatus;
                    if(request.getIdCredenciadaStatus() != null){
                        credenciadaStatus = credenciadaFindByIdAtivosUseCase.execute(request.getIdCredenciadaStatus()).onItem().ifNull().failWith(()-> new NotFoundException("Credenciada a ser associada (Status) não encontrado com ID:"+ request.getIdCredenciadaStatus()));
                    }else{
                        credenciadaStatus = Uni.createFrom().nullItem();
                    }

                    Uni<Categoria> categoriaStatus;
                    if(request.getIdCategoria() != null){
                        categoriaStatus = categoriaFindByIdAtivoUseCase.execute(request.getIdCategoria()).onItem().ifNull().failWith(()-> new NotFoundException("Categoria a ser associada (Status) não encontrado com ID:"+ request.getIdCategoria()));
                    }else{
                        categoriaStatus = Uni.createFrom().nullItem();
                    }

                    return credenciadaStatus.onItem().transformToUni(novaCredenciada ->{
                        return categoriaStatus.onItem().transformToUni(novaCategoria ->{
                            return statusRepository.update(request.getId(), request.getNome(), request.getPosicao(), request.getPontuacaoDaAtividade(), request.getDescricao(), request.getCorStatus(), request.getCorMapa(), request.getLegenda(), novaCategoria, novaCredenciada, usuario).map(mapper::toResponse);

                        });
                    });
                });
    }
}

