package br.com.foxinline.application.usecase.orgao;

import java.text.SimpleDateFormat;

import org.antlr.v4.runtime.BaseErrorListener;

import br.com.foxinline.application.dto.request.OrgaoRequestDTO;
import br.com.foxinline.application.dto.response.OrgaoResponseDTO;
import br.com.foxinline.application.usecase.bairro.BairroFindByIdAtivoComAssociacoesUseCase;
import br.com.foxinline.application.usecase.bairro.BairroFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.cidade.CidadeFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.credenciada.CredenciadaFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.endereco.EnderecoFindByIdAtivoUseCase;
import br.com.foxinline.application.usecase.tipoOrgao.TipoOrgaoFindByIdAtivosUseCase;
import br.com.foxinline.application.usecase.usuario.UsuarioFindByIdAtivoUseCase;
import br.com.foxinline.domain.mapper.OrgaoMapper;
import br.com.foxinline.domain.model.Credenciada;
import br.com.foxinline.domain.model.TipoOrgao;
import br.com.foxinline.domain.model.Usuario;
import br.com.foxinline.domain.model.endereco.Bairro;
import br.com.foxinline.domain.model.endereco.Cidade;
import br.com.foxinline.domain.model.endereco.Endereco;
import br.com.foxinline.domain.model.endereco.TipoLogradouro;
import br.com.foxinline.domain.repository.BairroRepository;
import br.com.foxinline.domain.repository.EnderecoRepository;
import br.com.foxinline.domain.repository.OrgaoRepository;
import br.com.foxinline.shared.util.Utils;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateOrgaoUseCase {
    @Inject
    BairroRepository bairroRepository;

    @Inject
    OrgaoRepository orgaoRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    BairroFindByIdAtivosUseCase bairroFindByIdAtivosUseCase;

    @Inject
    EnderecoFindByIdAtivoUseCase enderecoFindByIdAtivoUseCase;

    @Inject
    CredenciadaFindByIdAtivosUseCase credenciadaFindByIdAtivosUseCase;

    @Inject
    UsuarioFindByIdAtivoUseCase usuarioFindByIdAtivoUseCase;

    @Inject
    TipoOrgaoFindByIdAtivosUseCase tipoOrgaoFindByIdAtivosUseCase;

    @Inject
    BairroFindByIdAtivoComAssociacoesUseCase bairroFindByIdAtivoComAssociacoesUseCase;

    @Inject
    CidadeFindByIdAtivoUseCase cidadeFindByIdAtivoUseCase;

    @Inject
    OrgaoMapper mapper;

    @Transactional
    public Uni<OrgaoResponseDTO> execute(OrgaoRequestDTO request)  {
        //validarRequest(request);
 
        return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado"))
                .onItem()
                .transformToUni(usuario ->{
                    Uni<Credenciada> credenciadaSituacaoOrgao;
                    
                    if(request.getIdCredenciadaOrgao() != null){
                    
                        credenciadaSituacaoOrgao = credenciadaFindByIdAtivosUseCase
                                .execute(request.getIdCredenciadaOrgao())
                                .onItem().ifNull()
                                .failWith(() -> new NotFoundException("Credenciada a ser associada (Orgao) nao encontrado com ID: " + request.getIdCredenciadaOrgao()));      
                    }else{
                
                        credenciadaSituacaoOrgao = Uni.createFrom().item(null);
                    }
                    
                    return enderecoFindByIdAtivoUseCase.execute(request.getEndereco().getId()).onItem().ifNull().failWith(() -> new NotFoundException()).onItem().transformToUni(endereco -> {
                       
                        return bairroFindByIdAtivoComAssociacoesUseCase.execute(request.getEndereco().getIdBairro()).onItem().ifNull().failWith(() -> new NotFoundException()).onItem().transformToUni( bairro ->{
                       
                            return cidadeFindByIdAtivoUseCase.execute(request.getEndereco().getIdCidade()).onItem().ifNull().failWith(() -> new NotFoundException()).onItem().transformToUni(cidade ->{
                      
                                return tipoOrgaoFindByIdAtivosUseCase.execute(request.getIdTipoOrgao()).onItem().ifNull().failWith(() -> new NotFoundException()).onItem().transformToUni(tipoOrgao ->{
                             
                                    return credenciadaSituacaoOrgao.onItem()
                                            .transformToUni(novaCredenciadaOrgao ->{
                                                return orgaoRepository.update(
                                                    request.getId(), 
                                                    request.getNome(), 
                                                    request.getEmail(),
                                                    request.getTelefone(),
                                                    request.getCnpj(),
                                                    request.getCpf(),
                                                    request.getDataNascimento(),
                                                    request.getTipoDocumento(),
                                                    request.getRazaoSocial(),
                                                    request.getNomeResponsavel(),
                                                    request.getNomeTabeliao(),
                                                    request.getCodigoIBGEServentia(),
                                                    request.getCodigoServentia(),
                                                    request.getOficioServentia(),
                                                    request.getEndereco().getCep(),
                                                    request.getEndereco().getLogradouro(),
                                                    request.getEndereco().getNumero(),
                                                    request.getEndereco().getComplemento(),
                                                    request.getEndereco().getTipoLogradouro(),
                                                    bairro,
                                                    cidade,
                                                    novaCredenciadaOrgao,
                                                    tipoOrgao,
                                                    usuario
                                                    )
                                                    .map(mapper::toResponse);
                                            }
                                        );
                                    
                                });
                            });
                        });
                    });

                });
        
        
    }
    

    //public Uni<OrgaoResponseDTO> execute(OrgaoRequestDTO request) {
    // return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
    //     .onItem().ifNull().failWith(() -> new NotFoundException("Usuário não encontrado"))
    //     .flatMap(usuario ->
    //         bairroRepository.findByIdAtivo(request.getIdBairro())
    //             .onItem().ifNull().failWith(() -> new NotFoundException("Bairro não encontrado"))
    //             .flatMap(bairro -> {
    //                 Endereco endereco = new Endereco();
    //                 endereco.setCep(request.getCep());
    //                 endereco.setLogradouro(request.getLogradouro());
    //                 endereco.setNumero(request.getNumero());
    //                 endereco.setComplemento(request.getComplemento());
    //                 endereco.setBairro(bairro); // agora é um bairro gerenciado
    //                 endereco.setTipoLogradouro(TipoLogradouro.valueString(request.getTipoLogradouro()));

    //                 Uni<Credenciada> credenciadaSituacaoOrgao;
    //                 if (request.getIdCredenciadaOrgao() != null) {
    //                     credenciadaSituacaoOrgao = credenciadaFindByIdAtivosUseCase
    //                         .execute(request.getIdCredenciadaOrgao())
    //                         .onItem().ifNull().failWith(() ->
    //                             new NotFoundException("Credenciada não encontrada"));
    //                 } else {
    //                     credenciadaSituacaoOrgao = Uni.createFrom().nullItem();
    //                 }

    //                 return credenciadaSituacaoOrgao.flatMap(credenciada ->
    //                     orgaoRepository.update(
    //                         request.getId(),
    //                         request.getNome(),
    //                         request.getEmail(),
    //                         request.getTelefone(),
    //                         request.getCnpj(),
    //                         request.getCpf(),
    //                         request.getDataNascimento(),
    //                         request.getTipoDocumento(),
    //                         request.getRazaoSocial(),
    //                         request.getNomeResponsavel(),
    //                         endereco,
    //                         usuario
    //                     ).map(mapper::toResponse)
    //                 );
    //             })
    //     );
 
    // System.out.println("🔵 Início do UpdateOrgaoUseCase");
    // System.out.println("📨 Request recebido: " + request);

    // return enderecoRepository.findByIdAtivo(request.getEndereco().getId()).onItem().ifNull().failWith(() -> new NotFoundException(""))
    // .flatMap(endereco -> {
    //     System.out.println("✅ endereco encontrado: " + endereco);
    //     return bairroRepository.findByIdAtivo(request.getEndereco().getIdBairro())
    //         .onItem().ifNull().failWith(() -> {
    //             System.out.println("❌ Bairro não encontrado com id: " + request.getIdBairro());
    //             return new NotFoundException("Bairro não encontrado");
    //         })
    //         .flatMap(bairro -> {
    //             System.out.println("✅ Bairro encontrado: " + bairro);
    
                
    //             endereco.setCep(request.getEndereco().getCep());
    //             endereco.setLogradouro(request.getEndereco().getLogradouro());
    //             endereco.setNumero(request.getEndereco().getNumero());
    //             endereco.setComplemento(request.getEndereco().getComplemento());
    //             endereco.setBairro(bairro);
    //             endereco.setTipoLogradouro(TipoLogradouro.valueString(request.getEndereco().getTipoLogradouro()));
    
    //             System.out.println("🏠 Endereço montado: " + endereco);
    
    //             return usuarioFindByIdAtivoUseCase.execute(request.getIdUsuario())
    //                 .onItem().ifNull().failWith(() -> {
    //                     System.out.println("❌ Usuário não encontrado com id: " + request.getIdUsuario());
    //                     return new NotFoundException("Usuário não encontrado");
    //                 })
    //                 .flatMap(usuario -> {
    //                     System.out.println("👤 Usuário encontrado: " + usuario);
    
    //                     Uni<Credenciada> credenciadaUni;
    //                     if (request.getIdCredenciadaOrgao() != null) {
    //                         System.out.println("🔍 Buscando Credenciada com id: " + request.getIdCredenciadaOrgao());
    
    //                         credenciadaUni = credenciadaFindByIdAtivosUseCase
    //                             .execute(request.getIdCredenciadaOrgao())
    //                             .onItem().ifNull().failWith(() -> {
    //                                 System.out.println("❌ Credenciada não encontrada");
    //                                 return new NotFoundException("Credenciada não encontrada");
    //                             });
    //                     } else {
    //                         System.out.println("⚠️ Credenciada não informada, usando null");
    //                         credenciadaUni = Uni.createFrom().item(null);
    //                     }
    
    //                     return credenciadaUni.flatMap(credenciada -> {
    //                         System.out.println("🏢 Credenciada encontrada (ou null): " + credenciada);
    
    //                         return orgaoRepository.update(
    //                                 request.getId(),
    //                                 request.getNome(),
    //                                 request.getEmail(),
    //                                 request.getTelefone(),
    //                                 request.getCnpj(),
    //                                 request.getCpf(),
    //                                 request.getDataNascimento(),
    //                                 request.getTipoDocumento(),
    //                                 request.getRazaoSocial(),
    //                                 request.getNomeResponsavel(),
    //                                 endereco,
    //                                 usuario
    //                             )
    //                             .map(orgao -> {
    //                                 System.out.println("✅ Orgao atualizado: " + orgao);
    //                                 OrgaoResponseDTO response = mapper.toResponse(orgao);
    //                                 System.out.println("📤 Response gerado: " + response);
    //                                 return response;
    //                             });
    //                     });
    //                 });
    //         })
    //         .onFailure().invoke(e -> {
    //             System.out.println("🔥 ERRO durante a execução do UseCase:");
    //             e.printStackTrace();

    //         });

    // });



    


    private void validarRequest(OrgaoRequestDTO request) {
        if (request.getNome() == null || Utils.isEmpty(request.getNome())) {
            throw new BadRequestException("Nome não pode estar vazio.");
        }
        if(request.getEmail() == null || Utils.isEmpty(request.getNome())){
            throw new BadRequestException("Email não pode estar vazio");
        }
        if(request.getTelefone() == null || Utils.isEmpty(request.getTelefone())){
            throw new BadRequestException("Telefone não pode estar vazio");
        }
        if(request.getCpf() == null || Utils.isEmpty(request.getCpf())){
            throw new BadRequestException("Cpf não pode estar vazio");
        }
        if(request.getCnpj() == null || Utils.isEmpty(request.getCnpj())){
            throw new BadRequestException("CNPJ não pode estar vazio");
        }
        if(request.getDataNascimento() == null || Utils.isEmpty(request.getDataNascimento())){
            throw new BadRequestException("Data de Nascimento não pode estar vazia");
        }
        if(request.getTipoDocumento() == null || Utils.isEmpty(request.getTipoDocumento())){
            throw new BadRequestException("Tipo de documento não pode estar vazio");
        }
        if(request.getRazaoSocial() == null || Utils.isEmpty(request.getRazaoSocial())){
            throw new BadRequestException("Razão Social não pode estar vazio");
        }
        if(request.getNomeResponsavel() == null || Utils.isEmpty(request.getNomeResponsavel())){
            throw new BadRequestException("Responsavel não pode estar vazio");
        }
        if(request.getIdCredenciadaOrgao() == null || Utils.isEmpty(request.getIdCredenciadaOrgao())){
            throw new BadRequestException("Credenciada não pode estar vazio");
        }
        if(request.getIdTipoOrgao() == null || Utils.isEmpty(request.getIdTipoOrgao())){
            throw new BadRequestException("Tipo Orgão não pode estar vazio");
        }
        if(request.getNumero() == null || Utils.isEmpty(request.getNumero())){
            throw new BadRequestException("Número não pode estar vazio");
        }
        if(request.getIdCidade() == null ){
            throw new BadRequestException("Cidade não pode estar vazia");
        }
        if(request.getLogradouro() == null || Utils.isEmpty(request.getLogradouro())){
            throw new BadRequestException("Logradouro não pode estar vazio");
        }
        if(request.getIdBairro() == null ){
            throw new BadRequestException("Cep não pode estar vazio");
        }
    }
}