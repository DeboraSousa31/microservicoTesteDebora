package br.com.foxinline.infrastructure.repository;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Interessado;
import br.com.foxinline.domain.model.TipoDocumento;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.InteressadoRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import br.com.foxinline.shared.util.CloneUtils;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;



@ApplicationScoped
public class InteressadoRepositoryImpl implements InteressadoRepository, PanacheRepository<Interessado>{

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper objectMapper;


    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String cpf, Long idCredenciadaInteressado,
            String cnpj, String email, String telefone, String orgaoEmissor,
            String razaoSocial, String nomeFantasia, String profissao, Long idUsuario, String dataNascimento, String tipoDocumento,
            int pageNumber, int pageSize, String sortString) {
        
                
                StringBuilder queryBuilder = new StringBuilder("from Interessado i where i.ativo = true");

                //StringBuilder queryBuilder = new StringBuilder("from Pessoa i where type(i) = Interessado and i.ativo = true");

          
                Map<String, Object> params = new HashMap<>();

                if(nome != null && Utils.isNotEmpty(nome)){
                    queryBuilder.append(" and UPPER(i.nome) like UPPER(:nome)");
                    params.put("nome", "%" + nome.trim() + "%");
                }
                if(cpf != null && Utils.isNotEmpty(cpf)){
                    queryBuilder.append(" and i.cpf = :cpf ");
                    params.put("cpf", cpf);
                }
                if(razaoSocial != null && Utils.isNotEmpty(razaoSocial)){
                    queryBuilder.append(" and UPPER(i.razaoSocial) like UPPER(:razaoSocial)");
                    params.put("razaoSocial", "%" + razaoSocial.trim() + "%");
                }
                if(dataNascimento != null && Utils.isNotEmpty(dataNascimento)){
                    LocalDate dataPesquisa = LocalDate.parse(dataNascimento);
                    queryBuilder.append(" and i.dataNascimento = :dataNascimento");
                    params.put("dataNascimento", dataPesquisa);

                }
                if(cnpj != null && Utils.isNotEmpty(cnpj)){
                    queryBuilder.append(" and i.cnpj = :cnpj ");
                    params.put("cnpj", cnpj);
                }
                if(email != null && Utils.isNotEmpty(email)){
                    queryBuilder.append(" and UPPER(i.email) like UPPER(:email)");
                    params.put("email", "%" + email.trim() + "%");
                }
                if(nomeFantasia != null && Utils.isNotEmpty(nomeFantasia)){
                    queryBuilder.append(" and UPPER(i.nomeFantasia) like UPPER(:nomeFantasia)");
                    params.put("nomeFantasia", "%" + nomeFantasia.trim() + "%");
                }
                if(tipoDocumento != null && Utils.isNotEmpty(tipoDocumento)){
                    queryBuilder.append(" and UPPER(i.tipoDocumento) like UPPER(:tipoDocumento)");
                    params.put("tipoDocumento",  tipoDocumento);
                }
                if(orgaoEmissor != null && Utils.isNotEmpty(orgaoEmissor)){
                    queryBuilder.append(" and UPPER(i.orgaoEmissor) like UPPER(:orgaoEmissor)");
                    params.put("orgaoEmissor", "%" + orgaoEmissor.trim() + "%");
                }
                if(profissao != null && Utils.isNotEmpty(profissao)){
                    queryBuilder.append(" and UPPER(i.profissao) like UPPER(:profissao)");
                    params.put("profissao", "%" + profissao.trim() + "%");
                }
                if(idCredenciadaInteressado != null){
                    queryBuilder.append(" and i.credenciada.id = :idCredenciadaInteressado");
                    params.put("idCredenciadaInteressado", idCredenciadaInteressado);
                }
                 if (idUsuario != null) {
                    queryBuilder.append(" and i.credenciada.id = (select u.credenciada.id from Usuario u where u.id = :idUsuario)");
                    params.put("idUsuario", idUsuario);
                }

                Sort sort = Sort.by("nome", Sort.Direction.Ascending);
                if (sortString != null && Utils.isNotEmpty(sortString)) {
                    String[] sortParams = sortString.split(",");
                    if (sortParams.length == 2) {
                        String field = sortParams[0].trim();
                        Sort.Direction direction = "asc".equalsIgnoreCase(sortParams[1].trim()) ? Sort.Direction.Ascending
                                : Sort.Direction.Descending;
                        sort = Sort.by(field, direction);
                    }
                }

                PanacheQuery<Interessado> baseQuery = find(queryBuilder.toString(), sort, params);
                PanacheQuery<Interessado> countQuery = find(queryBuilder.toString(), params);

                return countQuery.count()
                        .flatMap(count -> {
                            if (count == 0) {
                                return Uni.createFrom().item(new PaginatedSituacaoResult(List.of(), 0L));
                            }
                            return baseQuery.page(Page.of(pageNumber, pageSize)).list()
                                    .onItem().transform(listResult -> new PaginatedSituacaoResult(listResult, count));
                        }
                        );


        }

    @Override
    public Uni<Interessado> save(String nome, String cpf, String cnpj, String email, String profissao, String telefone,
            String nomeFantasia, String razaoSocial, String orgaoEmissor, String dataNascimento, String rg, String tipoDocumento,
            Credenciada credenciada, Usuario usuario) {
                Interessado object = new Interessado();

                if(nome != null && Utils.isNotEmpty(nome)){
                    object.setNome(nome);
                }
                if(cpf != null && Utils.isNotEmpty(cpf)){
                    object.setCpf(cpf);
                }
                if(cnpj != null && Utils.isNotEmpty(cnpj)){
                    object.setRg(cnpj);
                }
                if(email != null && Utils.isNotEmpty(email)){
                    object.setEmail(email);
                }
                if(profissao != null && Utils.isNotEmpty(profissao)){
                    object.setProfissao(profissao);
                }
                if(telefone != null && Utils.isNotEmpty(telefone)){
                    object.setTelefone(telefone);
                }
                if(nomeFantasia != null && Utils.isNotEmpty(nomeFantasia)){
                    object.setNomeFantasia(nomeFantasia);
                }
                if(razaoSocial != null && Utils.isNotEmpty(razaoSocial)){
                    object.setRazaoSocial(razaoSocial);
                }
                if(orgaoEmissor != null && Utils.isNotEmpty(orgaoEmissor)){
                    object.setOrgaoEmissor(orgaoEmissor);
                }
                if (dataNascimento != null && Utils.isNotEmpty(dataNascimento)) {
                    LocalDate nascimento = LocalDate.parse(dataNascimento);
                    object.setDataNascimento(nascimento);
                }
                if(rg != null && Utils.isNotEmpty(rg)){
                    object.setRg(rg);
                }
                if(credenciada != null){
                    object.setCredenciada(credenciada);
                }
                if(tipoDocumento != null && Utils.isNotEmpty(tipoDocumento)){
                    object.setTipoDocumento(TipoDocumento.valueOf(tipoDocumento.toUpperCase()));
                }
               
                // if(usuario != null){
                    
                // }
         
                return persistAndFlush(object).onItem()
                .transformToUni(entidadePersistida -> auditoriaService.auditar(
                        null, entidadePersistida, "CREATE", usuario.getNome(), credenciada.getId())
                        .replaceWith(entidadePersistida)

               
            );


    }

    @Override
    public Uni<Interessado> findByIdAtivo(Long idInteressado) {
        return find("id = :id and ativo = true", Parameters.with("id", idInteressado)).firstResult();
    }

    @Override
    public Uni<Void> remove(Long interessadoId, Usuario usuario) {
        return findById(interessadoId)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException(
                    "Nenhuma Entidade com id:" + interessadoId + " encontrada."))
                .onItem().transformToUni(entidadeAntiga ->{
                    Interessado cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga, Interessado.class);
                    entidadeAntiga.setAtivo(false);
                    return persist(entidadeAntiga)
                            .onItem().transformToUni(entidadeAtualizada -> auditoriaService.auditar(
                                cloneEntidadeAntiga,
                                entidadeAtualizada,
                                "UPDATE", 
                                usuario.getNome(), 
                                usuario.getCredenciada().getId()))
                            .replaceWithVoid();

                });
    }

    @Override
    public Uni<Interessado> update(Long id, String nome, String cpf, String cnpj, String email, String profissao,
            String telefone, String nomeFantasia, String razaoSocial, String orgaoEmissor, String dataNascimento,
            String rg, String tipoDocumento, Credenciada credenciada, Usuario usuario) {
            
                return findById(id).onItem().ifNull().failWith(()-> new NotFoundException(""))
                    .onItem().ifNotNull().transformToUni(entidadeAntiga ->{
                        Interessado cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga, Interessado.class);
                        entidadeAntiga.setNome(nome);
                        entidadeAntiga.setCpf(cpf);
                        entidadeAntiga.setCnpj(cnpj);
                        entidadeAntiga.setEmail(email);
                        entidadeAntiga.setProfissao(profissao);
                        entidadeAntiga.setTelefone(telefone);
                        entidadeAntiga.setNomeFantasia(nomeFantasia);
                        entidadeAntiga.setRazaoSocial(razaoSocial);
                        entidadeAntiga.setOrgaoEmissor(orgaoEmissor);
                        if(dataNascimento != null){

                            LocalDate nascimento = LocalDate.parse(dataNascimento);
                            entidadeAntiga.setDataNascimento(nascimento);
                        }
                        entidadeAntiga.setRg(rg);
                        if(tipoDocumento != null){

                            entidadeAntiga.setTipoDocumento(TipoDocumento.valueOf(tipoDocumento.toUpperCase()));
                        }
                        entidadeAntiga.setCredenciada(credenciada);

                        return persist(entidadeAntiga).onItem().transformToUni(entidadeAtualizar ->
                        auditoriaService.auditar(cloneEntidadeAntiga, entidadeAtualizar, "UPDATE", usuario.getNome(), entidadeAtualizar.getCredenciada().getId()
                        ).replaceWith(entidadeAtualizar));

                    });
    }

        
    }
    

