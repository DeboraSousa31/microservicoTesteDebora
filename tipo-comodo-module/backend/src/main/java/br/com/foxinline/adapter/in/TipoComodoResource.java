package br.com.foxinline.adapter.in;



import br.com.foxinline.application.dto.request.TipoComodoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.tipoComodo.FindByIdAtivoTipoComodoUseCase;
import br.com.foxinline.application.usecase.tipoComodo.PesquisarTipoComodoUseCase;
import br.com.foxinline.application.usecase.tipoComodo.RemoverTipoComodoUseCase;
import br.com.foxinline.application.usecase.tipoComodo.SaveTipoComodoUseCase;
import br.com.foxinline.application.usecase.tipoComodo.UpdateTipoComodoUseCase;
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

@Path("/tipo-comodo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TipoComodoResource {

    @Inject
    PesquisarTipoComodoUseCase pesquisarTipoComodoUseCase;

    @Inject
    FindByIdAtivoTipoComodoUseCase findByIdAtivoTipoComodoUseCase;

    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase; 

    @Inject
    RemoverTipoComodoUseCase removerTipoComodoUseCase;

    @Inject
    UpdateTipoComodoUseCase updateTipoComodoUseCase;
    
    @Inject
    SaveTipoComodoUseCase saveTipoComodoUseCase;

    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
        @QueryParam("nomeSingular") String nomeSingular,
        @QueryParam("nomePlural") String nomePlural,
        @QueryParam("idCredenciadaTipoComodo") Long idCredenciadaTipoComodo,
        @QueryParam("idUsuario") Long idUsuario,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("5") int size,
        @QueryParam("sort") @DefaultValue("nomeSingular,asc") String sort
    ) {

        TipoComodoRequestDTO requestDTO = new TipoComodoRequestDTO();
        requestDTO.setNomeSingular(nomeSingular);
        requestDTO.setNomePlural(nomePlural);
        requestDTO.setIdCredenciadaTipoComodo(idCredenciadaTipoComodo);
        requestDTO.setIdUsuario(idUsuario);
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);
        return pesquisarTipoComodoUseCase.execute(requestDTO)
                        .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());
    }

    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id){
        return findByIdAtivoTipoComodoUseCase.execute(id)
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


    @DELETE
    @WithTransaction
    public Uni<Response> remove(@QueryParam("id") Long id, @QueryParam("idUsuario") Long idUsuario){

        TipoComodoRequestDTO request = new TipoComodoRequestDTO();
        request.setId(id);
        request.setIdUsuario(idUsuario);
      
        
        return removerTipoComodoUseCase.execute(request)
                .onItem().transform(item -> {
                    return Response.status(Response.Status.NO_CONTENT).build();
                    
                })
                .onFailure(NotFoundException.class).recoverWithItem(e ->{
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
        }

    @PUT
    @WithTransaction
    public Uni<Response> update(TipoComodoRequestDTO request){
        return updateTipoComodoUseCase.execute(request)
            .onItem().transform(item -> Response.ok(item).build())
            .onFailure(NotFoundException.class)
            .recoverWithItem(e-> Response.status(Status.NOT_FOUND).entity(e).build())
            .onFailure(BadRequestException.class)
            .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }

    @POST
    @WithTransaction
    public Uni<Response> save(TipoComodoRequestDTO request){
        return saveTipoComodoUseCase.execute(request)
            .onItem().transform(item -> Response.ok(item).build())
            .onFailure(NotFoundException.class)
            .recoverWithItem(e-> Response.ok(Status.NOT_FOUND).entity(e.getMessage()).build());
    }
}
