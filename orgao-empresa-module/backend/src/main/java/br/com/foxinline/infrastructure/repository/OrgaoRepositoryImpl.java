package br.com.foxinline.infrastructure.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.Orgao;
import br.com.foxinline.domain.model.TipoDocumento;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.model.endereco.TipoLogradouro;
import br.com.foxinline.domain.model.query.PaginatedSituacaoResult;
import br.com.foxinline.domain.repository.BairroRepository;
import br.com.foxinline.domain.repository.OrgaoRepository;
import br.com.foxinline.infrastructure.service.PersistenciaComAuditoriaService;
import br.com.foxinline.shared.util.CloneUtils;
import br.com.foxinline.shared.util.Utils;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityGraph;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ApplicationScoped
public class OrgaoRepositoryImpl implements OrgaoRepository, PanacheRepository<Orgao>{
    

    @Inject
    PersistenciaComAuditoriaService auditoriaService;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BairroRepository bairroRepository;

    @Override
    public Uni<PaginatedSituacaoResult> pesquisaPaginada(String nome, String nomeResponsavel, Long cidade, 
            Long tipoOrgao, Long idCredenciadaOrgao, String codigoIBGEServentia, String oficioServentia,
            String codigoServentia, String nomeTabeliao,Long idUsuario, int pageNumber, int pageSize, String sortString) {
       
        StringBuilder queryBuilder = new StringBuilder(
            "select distinct o from Orgao o "+
            "left join fetch o.tipoDeOrgao "+
            "left join fetch o.enderecoDoOrgao e "+
            "left join fetch e.bairro b "+
            "left join fetch b.cidade c where o.ativo = true");

        Map<String, Object> params = new HashMap<>();

        if(idUsuario != null){

        }

        if (nome != null && Utils.isNotEmpty(nome)) {
            queryBuilder.append(" and UPPER(o.nome) like UPPER(:nome)");
            params.put("nome", "%" + nome.trim() + "%");
        }

        if (nomeResponsavel != null && Utils.isNotEmpty(nomeResponsavel)){
            queryBuilder.append(" and UPPER(o.nomeResponsavel) like UPPER(:nomeResponsavel)");
            params.put("nomeResponsavel", "%" + nomeResponsavel.trim() + "%");
        }

        if (cidade != null) {
            queryBuilder.append(" and c.id = :cidade");
            params.put("cidade", cidade);
        }
        

        if(tipoOrgao != null){
            queryBuilder.append(" and o.tipoDeOrgao.id = :tipoOrgao");
            params.put("tipoOrgao", tipoOrgao);
        }

        if(idCredenciadaOrgao != null){
            queryBuilder.append(" and o.credenciada.id = :idCredenciadaOrgao");
            params.put("idCredenciadaOrgao", idCredenciadaOrgao);
        }

        if (codigoIBGEServentia != null && Utils.isNotEmpty(codigoIBGEServentia)){
            queryBuilder.append(" and UPPER(o.codigoIBGEServentia) like UPPER(:codigoIBGEServentia)");
            params.put("codigoIBGEServentia", "%" + codigoIBGEServentia.trim() + "%");
        }

        if (oficioServentia != null && Utils.isNotEmpty(oficioServentia)){
            queryBuilder.append(" and UPPER(o.oficioServentia) like UPPER(:oficioServentia)");
            params.put("oficioServentia", "%" + oficioServentia.trim() + "%");
        }


        if (codigoServentia != null && Utils.isNotEmpty(codigoServentia)){
            queryBuilder.append(" and UPPER(o.codigoServentia) like UPPER(:codigoServentia)");
            params.put("codigoServentia", "%" + codigoServentia.trim() + "%");
        }


        if (nomeTabeliao != null && Utils.isNotEmpty(nomeTabeliao)){
            queryBuilder.append(" and UPPER(o.nomeTabeliao) like UPPER(:nomeTabeliao)");
            params.put("nomeTabeliao", "%" + nomeTabeliao.trim() + "%");
        }

        if (idUsuario != null) {
            queryBuilder.append(" and o.credenciada.id = (select u.credenciada.id from Usuario u where u.id = :idUsuario)");
            params.put("idUsuario", idUsuario);
        }




        Sort sort = Sort.by("nome", Sort.Direction.Ascending);
        if (sortString != null && Utils.isNotEmpty(sortString)) {
            String[] sortParams = sortString.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0].trim();
                 if (!field.contains(".")) {
                    field = "o." + field;
                }
                Sort.Direction direction = "asc".equalsIgnoreCase(sortParams[1].trim()) ? Sort.Direction.Ascending
                        : Sort.Direction.Descending;
                sort = Sort.by(field, direction);
            }
        }

        PanacheQuery<Orgao> baseQuery = find(queryBuilder.toString(), sort, params);
        PanacheQuery<Orgao> countQuery = find(queryBuilder.toString(), params);

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
    public Uni<Orgao> findByIdAtivo(Long orgaoId) {
       // return find("id = :id and ativo = true", Parameters.with("id", orgaoId)).firstResult();
       return find("""
        select o from Orgao o
        left join fetch o.tipoDeOrgao
        left join fetch o.credenciada cr
        left join fetch cr.usuario
        left join fetch o.enderecoDoOrgao e
        left join fetch e.bairro b
        left join fetch b.cidade c
        left join fetch c.estado
        where o.id = :id and o.ativo = true
        """, Parameters.with("id", orgaoId))
        .firstResult();
     
    }

    @Override
    public Uni<Void> remove(Long orgaoId, Usuario usuario){
        return findById(orgaoId)
            .onItem().ifNull()
            .failWith(() -> new NotFoundException(
                "Nenhuma entidade com id:"+ orgaoId + " encontrada."))
            .onItem().transformToUni(entidadeAntiga ->{
                Orgao cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga,
                        Orgao.class);
                entidadeAntiga.setAtivo(false);
                return persist(entidadeAntiga)
                        .onItem().transformToUni(entidadeAtualizada -> auditoriaService.auditar(cloneEntidadeAntiga, entidadeAtualizada, "UPDATE", usuario.getNome(), usuario.getCredenciada().getId())).replaceWithVoid();
            });
    }

    @Override
    public Uni<Orgao> save(
                String nome, 
                String email, 
                String telefone, 
                String cnpj, 
                String cpf, 
                String dataNascimento,
                String tipoDocumento, 
                String razaoSocial, 
                String nomeResponsavel, 
                Credenciada credenciada,
                TipoOrgao tipoOrgao, 
                Usuario usuario, 
                Endereco endereco)
                {
            Orgao object = new Orgao();

            if(nome != null && Utils.isNotEmpty(nome)){
                object.setNome(nome);
            }

            if(email != null && Utils.isNotEmpty(email)){
                object.setEmail(email);
            }
            if(telefone != null && Utils.isNotEmpty(telefone)){
                object.setTelefone(telefone);
            }
            if(cnpj != null && Utils.isNotEmpty(cnpj)){
                object.setCnpj(cnpj);
            }
            if(cpf != null && Utils.isNotEmpty(cpf)){
                object.setCpf(cpf);
            }
            if (dataNascimento != null && Utils.isNotEmpty(dataNascimento)) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date nascimento = sdf.parse(dataNascimento);
                    object.setDataNascimento(nascimento);
                } catch (ParseException e) {
                    return Uni.createFrom().failure(new RuntimeException("Data de nascimento inválida"));
                }
            }

            if(tipoDocumento != null && Utils.isNotEmpty(tipoDocumento)){
                object.setTipoDocumento(TipoDocumento.valueOf(tipoDocumento.toUpperCase()));
            }
            if(razaoSocial != null && Utils.isNotEmpty(razaoSocial)){
                object.setRazaoSocial(razaoSocial);
            }
            if(nomeResponsavel != null && Utils.isNotEmpty(nomeResponsavel)){
                object.setNomeResponsavel(nomeResponsavel);
            }

            if (credenciada != null) {
            object.setCredenciada(credenciada);            
                }else{
                    if(usuario.getCredenciada() != null){
                        object.setCredenciada(usuario.getCredenciada());
                    }
            }

            if (tipoOrgao != null) {
                object.setTipoDeOrgao(tipoOrgao);
            }

            if(usuario != null){

            }
            if(endereco != null){
                object.setEnderecoDoOrgao(endereco);
            }
  
            return persist(object).onItem()
                .transformToUni(entidadePersistida -> auditoriaService.auditar(
                    null,
                    entidadePersistida,
                    "CREATE",
                    usuario.getNome(),
                    entidadePersistida.getCredenciada().getId())
                    .replaceWith(entidadePersistida));   
            
    }

    @Transactional
    @Override
    public Uni<Orgao> update(
            Long id, 
            String nome, 
            String email,
            String telefone,
            String cnpj,
            String cpf,
            String dataNascimento,
            String tipoDocumento,
            String razaoSocial,
            String nomeResponsavel,
            String nomeTabeliao,
            String codigoIBGEServentia,
            String codigoServentia,
            String oficioServentia,
            String cep,
            String logradouro,
            String numero,
            String complemento,
            String tipoLogradouro,
            Bairro bairro,
            Cidade cidade,
            Credenciada credenciada,
            TipoOrgao tipoOrgao,
            Usuario usuario) {
        return findByIdAtivo(id).onItem().ifNull().failWith(() -> new NotFoundException("Órgão não encontrado"))
        .onItem().ifNotNull().transformToUni(entidadeAntiga -> {
                   
            Orgao cloneEntidadeAntiga = CloneUtils.clonar(entidadeAntiga, Orgao.class);
            
           
        
            entidadeAntiga.setNome(nome);
            entidadeAntiga.setEmail(email);
            entidadeAntiga.setTelefone(telefone);
            entidadeAntiga.setCnpj(cnpj);
            entidadeAntiga.setCpf(cpf);
            
            entidadeAntiga.setTipoDocumento(TipoDocumento.valueOf(tipoDocumento.toUpperCase()));
            entidadeAntiga.setCodigoIBGEServentia(codigoIBGEServentia);
            entidadeAntiga.setCodigoServentia(codigoServentia);
            entidadeAntiga.setNomeTabeliao(nomeTabeliao);
            entidadeAntiga.setOficioServentia(oficioServentia);
            entidadeAntiga.setRazaoSocial(razaoSocial);
            entidadeAntiga.setNomeResponsavel(nomeResponsavel);
            entidadeAntiga.setCredenciada(credenciada);
            entidadeAntiga.setTipoDeOrgao(tipoOrgao);

            
            Endereco enderecoAntigo = entidadeAntiga.getEnderecoDoOrgao();
            enderecoAntigo.setCep(cep);
            enderecoAntigo.setLogradouro(logradouro);
            enderecoAntigo.setNumero(numero);
            enderecoAntigo.setComplemento(complemento);
            //enderecoAntigo.setTipoLogradouro(TipoLogradouro.valueString(tipoLogradouro.toUpperCase()));
            bairro.setCidade(cidade);
            enderecoAntigo.setBairro(bairro);

      
            
          

            
            
 
            
            entidadeAntiga.setEnderecoDoOrgao(enderecoAntigo);

            return persist(entidadeAntiga).onItem().transformToUni(entidadeAtualizar ->
            auditoriaService.auditar(cloneEntidadeAntiga, entidadeAtualizar, "UPDATE", usuario.getNome(), entidadeAtualizar.getCredenciada().getId()
            ).replaceWith(entidadeAtualizar));
      
            
    }
    );

}



 




}
