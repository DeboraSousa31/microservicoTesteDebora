package br.com.foxinline.adapter.in.rest;

import com.amazonaws.services.kms.model.NotFoundException;

import br.com.foxinline.application.dto.request.EstadoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.estado.PesquisarEstadoUseCase;
import br.com.foxinline.application.usecase.estado.RemoverEstadoUseCase;
import br.com.foxinline.application.usecase.estado.SaveEstadoUseCase;
import br.com.foxinline.application.usecase.estado.UpdateEstadoUseCase;
import br.com.foxinline.application.usecase.estado.FindByIdAtivoEstadoUseCase;

import br.com.foxinline.domain.mapper.EstadoMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/estado")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EstadoResource {

    @Inject
    EstadoMapper mapper;

    @Inject
    PesquisarEstadoUseCase pesquisarEstadoUseCase;

    @Inject
    SaveEstadoUseCase saveEstadoUseCase;

    @Inject
    FindByIdAtivoEstadoUseCase findByIdAtivoEstadoUseCase;

    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase;

    @Inject
    RemoverEstadoUseCase removerEstadoUseCase;

    @Inject
    UpdateEstadoUseCase updateEstadoUseCase;
    
    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
            @QueryParam("nome") String nome,
            @QueryParam("uf") String uf,
            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size,
            @QueryParam("sort") @DefaultValue("nome,asc") String sort) {

        EstadoRequestDTO requestDTO = new EstadoRequestDTO();
        requestDTO.setNome(nome);
        requestDTO.setUf(uf);
        requestDTO.setIdUsuario(idUsuario);
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);
        return pesquisarEstadoUseCase.execute(requestDTO)
                .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());

    } 


    @POST
    @WithTransaction
    public Uni<Response> save(EstadoRequestDTO request){
        
        return saveEstadoUseCase.execute(request)
            .onItem().transform(estadoEntidade -> mapper.toResponse(estadoEntidade))
            .onItem().transform(item -> Response.ok(item).build())
            .onFailure(NotFoundException.class)
            .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }


    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id){
        return findByIdAtivoEstadoUseCase.execute(id)
        .onItem()
        .transform(list -> Response.ok(list).build());
    }

    @GET
    @Path("/credenciadas")
    @WithSession
    public Uni<Response> findAllCredenciadas(@QueryParam("nomeCredenciada")String nomeCredenciada){
        return credenciadaFindAllAtivosUseCase.execute(nomeCredenciada)
            .onItem()
            .transform(list -> Response.ok(list).build());
    }

    @DELETE
    @WithTransaction
    public Uni<Response> remove(@QueryParam("id")Long id, @QueryParam("idUsuario")Long idUsuario){
        EstadoRequestDTO request = new EstadoRequestDTO();
        request.setId(id);
        request.setIdUsuario(idUsuario);
        return removerEstadoUseCase.execute(request)
            .onItem().transform(item -> {
                return Response.status(Response.Status.NO_CONTENT).build();
            })
            .onFailure(NotFoundException.class).recoverWithItem(e ->{
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
    }

    @PUT
    @WithTransaction
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id")Long id, EstadoRequestDTO request){
        request.setId(id);
        return updateEstadoUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e).build())
                .onFailure(BadRequestException.class)
                .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }

}
