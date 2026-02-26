package br.com.foxinline.adapter.in.rest;


import br.com.foxinline.application.dto.request.EnderecoRequestDTO;
import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
import br.com.foxinline.application.dto.response.CidadeResponseDTO;
import br.com.foxinline.application.dto.response.PaginatedResponseDTO;
import br.com.foxinline.application.usecase.bairro.BairroFindByCidadeUseCase;
import br.com.foxinline.application.usecase.bairro.SaveBairroUseCase;
import br.com.foxinline.application.usecase.cidade.CidadeFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindAllAtivosUseCase;
import br.com.foxinline.application.usecase.endereco.EnderecoLookupUseCase;
import br.com.foxinline.application.usecase.orgao.FindByIdAtivoOrgaoUseCase;
import br.com.foxinline.application.usecase.orgao.PesquisarOrgaoUseCase;
import br.com.foxinline.application.usecase.orgao.RemoverOrgaoUseCase;
import br.com.foxinline.application.usecase.orgao.SaveOrgaoUseCase;
import br.com.foxinline.application.usecase.orgao.UpdateOrgaoUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.TipoOrgaoFindAllAtivosUseCase;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
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

@Path("/orgao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrgaoResource {

    @Inject
    OrgaoMapper mapper;


    @Inject
    PesquisarOrgaoUseCase pesquisarOrgaoUsecase;

    @Inject
    FindByIdAtivoOrgaoUseCase findByIdAtivoOrgaoUseCase;

    @Inject
    CredenciadaFindAllAtivosUseCase credenciadaFindAllAtivosUseCase;

    @Inject
    TipoOrgaoFindAllAtivosUseCase tipoOrgaoFindAllAtivosUseCase;

    @Inject
    CidadeFindAllAtivosUseCase cidadeFindAllAtivosUseCase;

    @Inject
    BairroFindByCidadeUseCase bairroFindByCidadeUseCase;

    @Inject
    RemoverOrgaoUseCase removerOrgaoUseCase;

    @Inject
    SaveOrgaoUseCase saveOrgaoUseCase;

    @Inject
    UpdateOrgaoUseCase updateOrgaoUseCase;

    @Inject
    EnderecoLookupUseCase enderecoLookupUseCase;

    @Inject
    SaveBairroUseCase saveBairroUseCase;


    @GET
    @Path("/pesquisar")
    @WithSession
    public Uni<Response> pesquisar(
            @QueryParam("nome") String nome,
            @QueryParam("cidade") Long cidade,
            @QueryParam("tipoOrgao") Long tipoOrgao,
            @QueryParam("idCredenciadaOrgao") Long idCredenciadaOrgao,

            @QueryParam("idUsuario") Long idUsuario,
            @QueryParam("nomeResponsavel") String nomeResponsavel,

            @QueryParam("codigoIBGEServentia") String codigoIBGEServentia,
            @QueryParam("oficioServentia") String oficioServentia,
            @QueryParam("codigoServentia") String codigoServentia,
            @QueryParam("nomeTabeliao") String nomeTabeliao,

            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size,
            @QueryParam("sort") @DefaultValue("nome,asc") String sort) {

        OrgaoRequestDTO requestDTO = new OrgaoRequestDTO();
        requestDTO.setNome(nome);
        requestDTO.setNomeResponsavel(nomeResponsavel);
        requestDTO.setIdCredenciadaOrgao(idCredenciadaOrgao);
        requestDTO.setIdTipoOrgao(tipoOrgao);
        requestDTO.setCodigoIBGEServentia(codigoIBGEServentia);
        requestDTO.setCodigoServentia(codigoServentia);
        requestDTO.setOficioServentia(oficioServentia);
        requestDTO.setNomeTabeliao(nomeTabeliao);
        requestDTO.setIdCidade(cidade);
        requestDTO.setIdUsuario(idUsuario);

        requestDTO.setPage(page);
        requestDTO.setSize(size);
        requestDTO.setSort(sort);

        return  pesquisarOrgaoUsecase.execute(requestDTO)
                .onItem()
                .transform(paginatedResponse -> Response.ok(paginatedResponse).build());
    } 


    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> findByIdAtivo(@PathParam("id") Long id){
        return findByIdAtivoOrgaoUseCase.execute(id)
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

    @GET
    @Path("/tipoOrgao")
    @WithSession
    public Uni<Response> findAllTipoOrgao(@QueryParam("tipoOrgao") String tipoOrgao){
        return tipoOrgaoFindAllAtivosUseCase.execute(tipoOrgao)
                .onItem()
                .transform(list -> Response.ok(list).build());
    }

    @GET
    @Path("/cidades")
    @WithSession
    public Uni<Response> findAllCidades(@QueryParam("nome") String nome){
        return cidadeFindAllAtivosUseCase.execute(nome)
                .onItem()
                .transform(list -> Response.ok(list).build());
    }

    // @GET
    // @Path("/cidades")
    // @WithSession
    // public Uni<PaginatedResponseDTO<CidadeResponseDTO>> findAllCidadesPaginado(
    //         @QueryParam("nome") String nome,
    //         @QueryParam("page") @DefaultValue("0") int page,
    //         @QueryParam("size") @DefaultValue("20") int size) {
        
    //     // Sua lógica para buscar cidades de forma paginada no repositório
    //     // O repositório deve retornar um objeto PanacheQuery ou similar
    //     return cidadeRepository.pesquisaPaginada(nome, page, size)
    //             .onItem().transform(paginatedResult -> {
    //                 // Transforma a lista de entidades em uma lista de DTOs
    //                 List<CidadeResponseDTO> dtoList = mapper.toResponseList(paginatedResult.getContent());
    //                 // Retorna o DTO paginado
    //                 return new PaginatedResponseDTO<>(dtoList, paginatedResult.getTotalPages(), paginatedResult.getTotalElements());
    //             });
    // }


    @GET
    @Path("/cidades/{cidadeId}/bairros")
    @WithSession
    public Uni<Response> buscarBairrosPorCidade(@PathParam("cidadeId") Long cidadeId) {
        return bairroFindByCidadeUseCase.execute(cidadeId)
                .onItem()
                .transform(bairros -> Response.ok(bairros).build());
    }

    @DELETE
    @WithTransaction
    public Uni<Response> remove(@QueryParam("id") Long id, @QueryParam("idUsuario") Long idUsuario){

        OrgaoRequestDTO request = new OrgaoRequestDTO();
        request.setId(id);
        request.setIdUsuario(idUsuario);

        return removerOrgaoUseCase.execute(request)
                .onItem().transform(item -> {
                    return Response.status(Response.Status.NO_CONTENT).build();
                })
                .onFailure(NotFoundException.class).recoverWithItem(e ->{
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
    }

    @POST
    @WithTransaction
    public Uni<Response> save(OrgaoRequestDTO request){
        return saveOrgaoUseCase.execute(request)
                .onItem().transform(orgaoEntidade -> mapper.toResponse(orgaoEntidade))
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }

    @PUT
    @WithTransaction
    @Path("/{id}") 
    public Uni<Response> update(@PathParam("id") Long id, OrgaoRequestDTO request){
        request.setId(id);
        return updateOrgaoUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e).build())
                .onFailure(BadRequestException.class)
                .recoverWithItem(e -> Response.status(Status.BAD_REQUEST).entity(e).build());
    }

    @GET
    @Path("/lookup")
    @WithSession
    public Uni<Response> lookupEndereco(@QueryParam("codigoIbge") String codigoIbge,
                                        @QueryParam("bairroNome") String bairroNome) {
        return enderecoLookupUseCase.execute(codigoIbge, bairroNome)
            .onItem().transform(response ->
                response != null 
                    ? Response.ok(response).build()
                    : Response.status(Response.Status.NOT_FOUND).build()
            );
    }
    @POST
    @Path("/novo/bairro")
    @WithTransaction
    public Uni<Response> saveBairro(OrgaoRequestDTO request){
     
        return saveBairroUseCase.execute(request)
                .onItem().transform(item -> Response.ok(item).build())
                .onFailure(NotFoundException.class)
                .recoverWithItem(e -> Response.status(Status.NOT_FOUND).entity(e.getMessage()).build());
    }

}
