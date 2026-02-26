package br.com.foxinline.adapter.in.rest;

import com.amazonaws.services.kms.model.NotFoundException;

import br.com.foxinline.application.dto.request.InteressadoRequestDTO;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.interessado.FindByIdAtivoInteressadoUseCase;
import br.com.foxinline.application.usecase.interessado.PesquisarInteressadoUseCase;
import br.com.foxinline.application.usecase.interessado.RemoverInteressadoUseCase;
import br.com.foxinline.application.usecase.interessado.SaveInteressadoUseCase;
import br.com.foxinline.application.usecase.interessado.UpdateInteressadoUseCase;
import br.com.foxinline.domain.mapper.InteressadoMapper;
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

@Path("/interessado")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InteressadoResource {

    @Inject
    InteressadoMapper mapper;

    @Inject
    PesquisarInteressadoUseCase pesquisarInteressadoUseCase;

    @Inject
    SaveInteressadoUseCase saveInteressadoUseCase;

    @Inject
    FindByIdAtivoInteressadoUseCase findByIdAtivoInteressadoUseCase;

    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase;

    @Inject
    RemoverInteressadoUseCase removerInteressadoUseCase;

    @Inject
    UpdateInteressadoUseCase updateInteressadoUseCase;
    
    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
            @QueryParam("nome") String nome,
            @QueryParam("cpf") String cpf,
            @QueryParam("idCredenciadaInteressado") Long idCredenciadaInteressado,
            @QueryParam("email") String email,
            @QueryParam("telefone") String telefone,
            @QueryParam("orgaoEmissor") String orgaoEmissor,
            @QueryParam("razaoSocial") String razaoSocial,
            @QueryParam("nomeFantasia") String nomeFantasia,
            @QueryParam("cnpj") String cnpj,
            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("profissao") String profissao,
            @QueryParam("dataNascimento") String dataNascimento,
            @QueryParam("tipoDocumento") String tipoDocumento,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size,
            @QueryParam("sort") @DefaultValue("nome,asc") String sort) {

        InteressadoRequestDTO requestDTO = new InteressadoRequestDTO();
        requestDTO.setNome(nome);
        requestDTO.setCpf(cpf);
        requestDTO.setRazaoSocial(razaoSocial);
        requestDTO.setDataNascimento(dataNascimento);
        requestDTO.setTipoDocumento(tipoDocumento);
        requestDTO.setCnpj(cnpj);
        requestDTO.setEmail(email);
        requestDTO.setNomeFantasia(nomeFantasia);
        requestDTO.setOrgaoEmissor(orgaoEmissor);
        requestDTO.setProfissao(profissao);
        requestDTO.setIdCredenciadaInteressado(idCredenciadaInteressado);
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);
        return pesquisarInteressadoUseCase.execute(requestDTO)
                .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());

    } 


    @POST
    @WithSession
    public Uni<Response> save(InteressadoRequestDTO request){
        
        return saveInteressadoUseCase.execute(request)
            .onItem().transform(interessadoEntidade -> mapper.toResponse(interessadoEntidade))
            .onItem().transform(item -> Response.ok(item).build())
            .onFailure(NotFoundException.class)
            .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }


    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id){
        return findByIdAtivoInteressadoUseCase.execute(id)
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
        InteressadoRequestDTO request = new InteressadoRequestDTO();
        request.setId(id);
        request.setIdUsuario(idUsuario);
        System.out.println(request);
        return removerInteressadoUseCase.execute(request)
            .onItem().transform(item -> {
                return Response.status(Response.Status.NO_CONTENT).build();
            })
            .onFailure(NotFoundException.class).recoverWithItem(e ->{
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
    }

    @PUT
    @Path("/{id}")
    @WithTransaction
    public Uni<Response> update(@PathParam("id")Long id, InteressadoRequestDTO request){
        request.setId(id);
        System.out.println(request);
        return updateInteressadoUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e).build())
                .onFailure(BadRequestException.class)
                .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }

}
