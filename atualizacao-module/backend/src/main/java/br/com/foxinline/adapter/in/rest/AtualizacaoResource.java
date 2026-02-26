package br.com.foxinline.adapter.in.rest;



import br.com.foxinline.application.dto.request.AtualizacaoRequestDTO;
import br.com.foxinline.application.usecase.atualizacao.FindByIdAtivoAtualizacaoUseCase;
import br.com.foxinline.application.usecase.atualizacao.PesquisarAtualizacaoUseCase;
import br.com.foxinline.application.usecase.atualizacao.RemoverAtualizacaoUseCase;
import br.com.foxinline.application.usecase.atualizacao.SaveAtualizacaoUseCase;
import br.com.foxinline.application.usecase.atualizacao.UpdateAtualizacaoUseCase;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
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

@Path("/atualizacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AtualizacaoResource {

    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase;

    @Inject
    PesquisarAtualizacaoUseCase pesquisarAtualizacaoUseCase;

    @Inject
    FindByIdAtivoAtualizacaoUseCase findByIdAtivoAtualizacaoUseCase;

    @Inject
    SaveAtualizacaoUseCase saveAtualizacaoUseCase;

    @Inject
    RemoverAtualizacaoUseCase removerAtualizacaoUseCase; 

    @Inject
    UpdateAtualizacaoUseCase updateAtualizacaoUseCase;


    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
            @QueryParam("descricao") String descricao,
            @QueryParam("dataAgendamento") String dataAgendamento,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size,
            @QueryParam("sort") @DefaultValue("dataCriacao,desc") String sort) {

        AtualizacaoRequestDTO requestDTO = new AtualizacaoRequestDTO();
        requestDTO.setDescricao(descricao);
        requestDTO.setDataAgendamento(dataAgendamento);
      
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);
        return pesquisarAtualizacaoUseCase.execute(requestDTO)
                .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());

    } 



    @GET
    @Path("/credenciadas")
    @WithSession
    public Uni<Response> findAllCredenciadas(@QueryParam("nomeCredenciada")String nomeCredenciada){
        return credenciadaFindAllAtivosUseCase.execute(nomeCredenciada)
            .onItem()
            .transform(list -> Response.ok(list).build());
    }

    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id){
        return findByIdAtivoAtualizacaoUseCase.execute(id)
            .onItem()
            .transform(list -> Response.ok(list).build());
    }


    @POST
    @WithTransaction
    public Uni<Response> save(AtualizacaoRequestDTO request){
        System.out.println(request);
        return saveAtualizacaoUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }


    @DELETE
    @WithTransaction
    public Uni<Response> remove(@QueryParam("id") Long id){
        AtualizacaoRequestDTO request = new AtualizacaoRequestDTO();
        request.setId(id);

        return removerAtualizacaoUseCase.execute(request)
                .onItem().transform(item -> {
                    return Response.status(Response.Status.NO_CONTENT).build();
                    
                })
                .onFailure(NotFoundException.class).recoverWithItem(e ->{
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
    }

    @PUT
    @WithTransaction
    public Uni<Response> update(AtualizacaoRequestDTO request){
        return updateAtualizacaoUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e).build())
                .onFailure(BadRequestException.class)
                .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }

}
