package br.com.foxinline.adapter.in.rest;



import br.com.foxinline.application.dto.request.SituacaoJuridicaRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.situacaoJuridica.FindByIdAtivoSituacaoJuridicaUseCase;
import br.com.foxinline.application.usecase.situacaoJuridica.PesquisarSituacaoJuridicaUseCase;
import br.com.foxinline.application.usecase.situacaoJuridica.RemoverSituacaoJuridicaUseCase;
import br.com.foxinline.application.usecase.situacaoJuridica.SaveSituacaoJuridicaUseCase;
import br.com.foxinline.application.usecase.situacaoJuridica.UpdateSituacaoJuridicaUseCase;
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

@Path("/situacao-juridica")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SituacaoJuridicaResource {

    @Inject
    PesquisarSituacaoJuridicaUseCase pesquisarSituacaoJuridicaUseCase;

    @Inject
    FindByIdAtivoSituacaoJuridicaUseCase findByIdAtivoSituacaoJuridicaUseCase;


    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase;

    @Inject
    RemoverSituacaoJuridicaUseCase removerSituacaoJuridicaUseCase;


    @Inject
    SaveSituacaoJuridicaUseCase saveSituacaoJuridicaUseCase;

    @Inject
    UpdateSituacaoJuridicaUseCase updateSituacaoJuridicaUseCase;

    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
            @QueryParam("nome") String nome,
            @QueryParam("descricao") String descricao,
            @QueryParam("idCredenciadaUsuario") Long idCredenciadaUsuario,
            @QueryParam("idCredenciadaSituacaoJuridica") Long idCredenciadaSituacaoJuridica,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size,
            @QueryParam("sort") @DefaultValue("nome,asc") String sort) {

        SituacaoJuridicaRequestDTO requestDTO = new SituacaoJuridicaRequestDTO();
        requestDTO.setNome(nome);
        requestDTO.setDescricao(descricao);
        //requestDTO.setIdCredenciadaUsuario(idCredenciadaUsuario);
        requestDTO.setIdCredenciadaSituacaoJuridica(idCredenciadaSituacaoJuridica);
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);
        return pesquisarSituacaoJuridicaUseCase.execute(requestDTO)
                .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());

    } 

    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id){
        return findByIdAtivoSituacaoJuridicaUseCase.execute(id)
            .onItem()
            .transform(list -> Response.ok(list).build());
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

        SituacaoJuridicaRequestDTO request = new SituacaoJuridicaRequestDTO();
        request.setId(id);
        request.setIdUsuario(idUsuario);
        //request.setIdCredenciadaUsuario(idCredenciadaUsuario);
        
        return removerSituacaoJuridicaUseCase.execute(request)
                .onItem().transform(item -> {
                    return Response.status(Response.Status.NO_CONTENT).build();
                    
                })
                .onFailure(NotFoundException.class).recoverWithItem(e ->{
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
        }


    @POST
    @WithTransaction
    public Uni<Response> save(SituacaoJuridicaRequestDTO request){
        return saveSituacaoJuridicaUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }


    @PUT
    @WithTransaction
    public Uni<Response> update(SituacaoJuridicaRequestDTO request){

        return updateSituacaoJuridicaUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e).build())
                .onFailure(BadRequestException.class)
                .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }

}
