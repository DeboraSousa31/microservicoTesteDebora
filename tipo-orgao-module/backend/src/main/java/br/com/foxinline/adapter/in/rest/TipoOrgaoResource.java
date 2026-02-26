package br.com.foxinline.adapter.in.rest;

import br.com.foxinline.application.dto.request.TipoOrgaoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.FindByIdAtivoTipoOrgaoUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.PesquisarTipoOrgaoUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.RemoverTipoOrgaoIdUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.SaveTipoOrgaoIdUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.UpdateTipoOrgaoUseCase;
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

@Path("/tipo-orgao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TipoOrgaoResource {

    @Inject
    PesquisarTipoOrgaoUseCase pesquisarTipoOrgaoUseCase;

    @Inject
    RemoverTipoOrgaoIdUseCase removerTipoOrgaoUseCase;

    @Inject
    SaveTipoOrgaoIdUseCase saveTipoOrgaoUseCase;

    @Inject
    UpdateTipoOrgaoUseCase updateTipoOrgaoUseCase;

    @Inject
    FindByIdAtivoTipoOrgaoUseCase findByIdAtivoTipoOrgaoUseCase;

    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase;

    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
            @QueryParam("nome") String nome,
            @QueryParam("descricao") String descricao,
            @QueryParam("idCredenciadaUsuario") Long idCredenciadaUsuario,
            @QueryParam("idCredenciadaTipoOrgao") Long idCredenciadaTipoOrgao,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size,
            @QueryParam("sort") @DefaultValue("nome,asc") String sort) {

        TipoOrgaoRequestDTO requestDTO = new TipoOrgaoRequestDTO();
        requestDTO.setNome(nome);
        requestDTO.setDescricao(descricao);
        requestDTO.setIdCredenciadaUsuario(idCredenciadaUsuario);
        requestDTO.setIdCredenciadaTipoOrgao(idCredenciadaTipoOrgao);
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);

        return pesquisarTipoOrgaoUseCase.execute(requestDTO)
                .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());
    }

    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id) {
        return findByIdAtivoTipoOrgaoUseCase.execute(id)
                .onItem()
                .transform(response -> Response.ok(response).build());
    }

    @GET
    @Path("/credenciadas")
    @WithSession
    public Uni<Response> findAllCredenciadas(@QueryParam("nomeCredenciada") String nomeCredenciada) {
        return credenciadaFindAllAtivosUseCase.execute(nomeCredenciada)
                .onItem()
                .transform(list -> Response.ok(list).build());
    }

    @DELETE
    @WithTransaction
    public Uni<Response> remove(@QueryParam("id") Long id, @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("idCredenciadaUsuario") Long idCredenciadaUsuario) {

        TipoOrgaoRequestDTO request = new TipoOrgaoRequestDTO();
        request.setId(id);
        request.setIdUsuario(idUsuario);
        request.setIdCredenciadaUsuario(idCredenciadaUsuario);

        return removerTipoOrgaoUseCase.execute(request)
                .onItem().transform(item -> {
                    return Response.status(Response.Status.NO_CONTENT).build();
                })
                .onFailure(NotFoundException.class).recoverWithItem(e -> {
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
    }

    @POST
    @WithTransaction
    public Uni<Response> save(TipoOrgaoRequestDTO request) {
        return saveTipoOrgaoUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }

    @PUT
    @WithTransaction
    public Uni<Response> update(TipoOrgaoRequestDTO request) {

        System.out.println("Oq o end point recebeu: " + request);
        
        return updateTipoOrgaoUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e).build())
                .onFailure(BadRequestException.class)
                .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }

}