package br.com.foxinline.adapter.in.rest;


import br.com.foxinline.application.dto.request.StatusRequestDTO;
import br.com.foxinline.application.usecase.categoria.CategoriaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.status.FindByIdAtivoStatusUseCase;
import br.com.foxinline.application.usecase.status.PesquisarStatusUseCase;
import br.com.foxinline.application.usecase.status.RemoverStatusUseCase;
import br.com.foxinline.application.usecase.status.SaveStatusUseCase;
import br.com.foxinline.application.usecase.status.UpdateStatusUseCase;
import br.com.foxinline.domain.mapper.StatusMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/status")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    @Inject
    PesquisarStatusUseCase pesquisarStatusUseCase;

    @Inject
    FindByIdAtivoStatusUseCase findByIdAtivoStatusUseCase;

    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase;

    @Inject
    RemoverStatusUseCase removerStatusUseCase;

    @Inject
    SaveStatusUseCase saveStatusUseCase;

    @Inject
    UpdateStatusUseCase updateStatusUseCase;

    @Inject
    CategoriaFindAllAtivosUseCase categoriaFindAllAtivosUseCase;

    @Inject
    StatusMapper mapper;

    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
            @QueryParam("nome") String nome,
            @QueryParam("descricao") String descricao,
            @QueryParam("idCategoria") Long idCategoria,
            @QueryParam("idCredenciadaUsuario") Long idCredenciadaUsuario,
            @QueryParam("idCredenciadaStatus") Long idCredenciadaStatus,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size,
            @QueryParam("sort") @DefaultValue("nome,asc") String sort) {

        StatusRequestDTO requestDTO = new StatusRequestDTO();
        requestDTO.setNome(nome);
        requestDTO.setDescricao(descricao);
        requestDTO.setIdCredenciadaStatus(idCredenciadaStatus);
        requestDTO.setIdCategoria(idCategoria);
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);
        return pesquisarStatusUseCase.execute(requestDTO)
                .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());

    } 

    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id){
        return findByIdAtivoStatusUseCase.execute(id)
            .onItem().transform(list -> Response.ok(list).build());
       
    }

    @GET
    @Path("/credenciadas")
    @WithSession
    public Uni<Response> findAllCredenciadas(@QueryParam("nomeCredenciada") String nomeCredenciada){
        return credenciadaFindAllAtivosUseCase.execute(nomeCredenciada)
                .onItem()
                .transform(list -> Response.ok(list).build());
    }

    @GET
    @Path("/categorias")
    @WithSession
    public Uni<Response> findAllCategorias(@QueryParam("nomeCategoria") String nomeCategoria){
        return categoriaFindAllAtivosUseCase.execute(nomeCategoria)
                .onItem()
                .transform(list -> Response.ok(list).build());
    }

    @DELETE
    @WithTransaction
    public Uni<Response> remove(@QueryParam("id") Long id, @QueryParam("idUsuario") Long idUsuario){

        StatusRequestDTO request = new StatusRequestDTO();
        request.setId(id);
        request.setIdUsuario(idUsuario);
      
        
        return removerStatusUseCase.execute(request)
                .onItem().transform(item -> {
                    return Response.status(Response.Status.NO_CONTENT).build();
                    
                })
                .onFailure(NotFoundException.class).recoverWithItem(e ->{
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
        }


    @POST
    @WithTransaction
    public Uni<Response> save(StatusRequestDTO request){
        return saveStatusUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }


    @PUT
    @WithTransaction
    public Uni<Response> update(StatusRequestDTO request){

        return updateStatusUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e).build())
                .onFailure(BadRequestException.class)
                .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }
}
