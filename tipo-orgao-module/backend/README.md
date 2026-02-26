# Cerurb Pro Modularizado

Uma visão geral da organização adotada

## Como Executar

1.  **Configure o `application.properties`**:
    Certifique-se de que o arquivo `src/main/resources/application.properties` está configurado corretamente com as URLs do banco de dados e de outras APIs que o projeto consome.

    ```properties
    # Banco de Dados (Exemplo com PostgreSQL)
    quarkus.datasource.db-kind=postgresql
    quarkus.datasource.username=seu_usuario
    quarkus.datasource.password=sua_senha
    quarkus.datasource.reactive.url=vertx-reactive:postgresql://localhost:5432/seu_banco

    # Configuração do Rest Client para a API de Auditoria
    auditoria-api/mp-rest/url=http://localhost:8081/api
    auditoria-api/mp-rest/scope=jakarta.enterprise.context.ApplicationScoped
    ```

2.  **Inicie a aplicação em modo de desenvolvimento**:
    O Quarkus irá fornecer hot-reload, facilitando o desenvolvimento.

    ```bash
    mvn quarkus:dev
    ```

## Arquitetura do Projeto

Este projeto segue os princípios da **Arquitetura Limpa (Clean Architecture)** para garantir que o código seja organizado, testável e manutenível. As responsabilidades são divididas em 4 camadas principais:

* **`domain`**: O coração do projeto. Contém as entidades e regras de negócio puras, sem dependências de frameworks.
* **`application`**: Orquestra os fluxos de negócio. Contém os **Casos de Uso** (`usecase`) que representam todas as ações que o sistema pode fazer.
* **`adapter`**: A ponte com o mundo exterior. Contém os **Controllers REST** (`adapter/in`) e os **Rest Clients** (`adapter/out`) para consumir outras APIs.
* **`infrastructure`**: A camada de detalhes técnicos. Contém as implementações de acesso a banco de dados (`repository`) e outros serviços.

### Onde eu coloco meu código? (Guia Rápido)

| Se você precisa...                                       | Crie ou modifique arquivos em...                                            |
| -------------------------------------------------------- | --------------------------------------------------------------------------- |
| Criar um novo endpoint REST                              | `adapter/in/rest`                                                           |
| Adicionar uma nova regra de negócio ou fluxo de trabalho | `application/usecase`                                                       |
| Alterar a forma de salvar ou buscar dados no BD          | `infrastructure/repository` (a implementação)                               |
| Adicionar um novo campo a uma entidade de negócio        | `domain/model` (a entidade) e `domain/repository` (a interface)             |
| Consumir uma nova API externa                            | `adapter/out/restclient` (o client) e `infrastructure/gateway` (a ponte)    |

## Principais Tecnologias

* **Quarkus**: Framework Java nativo para nuvem.
* **Hibernate Reactive com Panache**: Para acesso reativo a banco de dados.
* **Mutiny**: Para programação reativa.
* **JAX-RS (RESTEasy Reactive)**: Para a criação de endpoints REST.
* **MicroProfile Rest Client**: Para consumir outras APIs de forma declarativa.

Markdown

# Documentação da Arquitetura do Projeto

Bem-vindo(a) à equipe! Este documento detalha a arquitetura do nosso projeto, usando exemplos reais do código para ilustrar como e onde desenvolver novas funcionalidades.

Nossa arquitetura é baseada nos princípios da **Arquitetura Limpa (Clean Architecture)**, adaptada para um ambiente reativo com Quarkus e Mutiny. O objetivo é manter as regras de negócio isoladas das tecnologias de infraestrutura, resultando em um código mais testável, manutenível e flexível.

## As Camadas: Uma Visão Detalhada

### 🏛️ 1. Camada `domain`
O coração do software. Contém a lógica de negócio pura, sem qualquer dependência de frameworks.

* **`domain/model`**: As entidades de negócio.
    * **Exemplo**: `SituacaoProprietario.java`.
    * *Nota Pragmática*: Contém anotações do Jakarta Persistence (`@Entity`, `@Id`) por pragmatismo para facilitar a persistência com Panache, embora idealmente o domínio não teria conhecimento de persistência.
* **`domain/repository`**: Os contratos (interfaces) para persistência de dados.
    * **Exemplo**: `SituacaoProprietarioRepository.java`. Define *o que* precisa ser feito (ex: `save`, `pesquisaPaginada`), mas não *como*.
* **`domain/mapper`**: Conversores de objetos.
    * **Exemplo**: `SituacaoProprietarioMapper`. Transforma entidades em DTOs e vice-versa.

### 💼 2. Camada `application`
Orquestra os fluxos de trabalho e implementa as ações específicas do sistema.

* **`application/usecase`**: Os Casos de Uso. Cada classe é uma funcionalidade.
    * **Exemplo**: `PesquisarSituacaoProprietarioUseCase.java`.
    * **Responsabilidades**: Recebe `RequestDTO`, chama métodos do repositório do domínio, usa `mappers` e retorna `ResponseDTO` (geralmente em um `Uni`).
* **`application/dto`**: Objetos de Transferência de Dados (`Request` e `Response`).

### 🔌 3. Camada `adapter`
Conecta nossa aplicação com o mundo exterior.

* **`adapter/in/rest`**: Adaptadores de Entrada (Endpoints REST).
    * **Exemplo**: `SituacaoProprietarioResource.java`.
    * **Responsabilidades**: Define endpoints (`@Path`, etc.), lida com HTTP, injeta e executa `UseCases`, gerencia transações (`@WithTransaction`).
* **`adapter/out/restclient`**: Adaptadores de Saída (Clientes para APIs externas).
    * **Exemplo**: `AuditoriaRestClient.java`.
    * **Responsabilidades**: Usa `@RegisterRestClient` para criar clientes de API declarativamente.

### 🏗️ 4. Camada `infrastructure`
Implementação concreta dos detalhes técnicos.

* **`infrastructure/repository`**: Implementação dos repositórios do domínio.
    * **Exemplo**: `SituacaoProprietarioRepositoryImpl.java`.
    * **Responsabilidades**: Implementa a interface do repositório (ex: `SituacaoProprietarioRepository`), usa Panache para queries, e pode chamar outros serviços de infra (ex: `PersistenciaComAuditoriaService`).
* **`infrastructure/service`**: Serviços técnicos e de apoio.
    * **Exemplo**: `PersistenciaComAuditoriaService.java`.
    * **Responsabilidades**: Lógica de infra não diretamente ligada à persistência (ex: serialização para auditoria).
    * *Padrão Interessante*: Pode delegar tarefas de volta para a camada de aplicação (ex: chamando `RegistrarAuditoriaUseCase`).

## O Fluxo de Vida de uma Requisição (Exemplo Prático: `save`)

1.  **Entrada**: `POST /situacao-ocupante` com JSON.
2.  **Adapter (In)**: `SituacaoProprietarioResource` recebe e desserializa para `SituacaoProprietarioRequestDTO`. Chama o método `save`.
3.  **Chamada ao Use Case**: `Resource` invoca `saveSituacaoProprietarioUseCase.execute(requestDTO)`.
4.  **Aplicação**: `SaveSituacaoProprietarioUseCase` valida, pode usar `Mapper`, e chama `save(...)` da interface `SituacaoProprietarioRepository`.
5.  **Injeção de Dependência**: Quarkus fornece `SituacaoProprietarioRepositoryImpl`.
6.  **Infraestrutura (Repositório)**: `SituacaoProprietarioRepositoryImpl.save(...)` persiste com Panache e chama `auditoriaService.auditar(...)`.
7.  **Infraestrutura (Serviço de Auditoria)**: `PersistenciaComAuditoriaService` cria `AuditoriaDTO` e invoca `RegistrarAuditoriaUseCase`.
8.  **Aplicação (Caso de Uso de Auditoria)**: `RegistrarAuditoriaUseCase` chama `enviarAuditoria` da interface `AuditoriaGateway`.
9.  **Infraestrutura e Adapters (Fluxo de Saída da Auditoria)**:
    * `AuditoriaGatewayImpl` (infra) é injetada.
    * Chama `AuditoriaRestClient` (adapter).
    * `RestClient` faz `POST` para o microserviço de auditoria.
10. **Retorno do Fluxo Principal**: O `Uni` da operação de salvamento original retorna.
11. **Aplicação e Adapter (Retorno)**: `UseCase` mapeia para `ResponseDTO`. `Resource` transforma em `Response` HTTP `200 OK`.

Seguir este guia garantirá que o projeto continue limpo, organizado e escalável.