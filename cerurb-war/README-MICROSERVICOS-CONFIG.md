# Configuração Cerurb (WAR) – Chamada aos módulos Vue dos microserviços

Este diretório contém o fragmento XHTML para a tela de **Configuração** do Cerurb (aplicação WAR). O objetivo é ter, na aba **Perímetro** (ou na aba **Administração**), cards que levam às páginas Vue dos microserviços.

## 1. Incluir os cards na tela de Configuração

No seu arquivo da tela de Configuração (ex.: `configuracao.xhtml` ou o que contém a tab `id="perimetro"`):

- **Opção A – Incluir o fragmento**

Na aba Perímetro, dentro do `<div class="row p-3">`, após o card de **Estado**, inclua:

```xml
<ui:include src="/caminho/para/configuracao-perimetro-microservicos.xhtml" />
```

(Se o fragmento estiver em outro pacote/pasta do WAR, ajuste o `src`.)

- **Opção B – Copiar o conteúdo**

Abra `configuracao-perimetro-microservicos.xhtml`, copie os blocos dos cards (Estado, Tipo Cômodo, Tipo Órgão, Órgão/Empresa, Interessado, Atualização, Status, Situação Jurídica, Situação Ocupante) e cole dentro da mesma `<div class="row">` da aba Perímetro (ou da seção onde já está o card de Estado). Remova o card de Estado duplicado se você já tiver um no XHTML.

## 2. Páginas .xhtml que o WAR deve ter

Cada link do fragmento aponta para uma página `.xhtml` no WAR. Essas páginas devem existir no projeto Cerurb e fazer **redirect** ou **iframe** para a aplicação Vue (ex.: `https://cerurb-vue.foxinline.com/...`).

| Card        | Pesquisar (href)                 | Cadastrar (href)           |
|------------|----------------------------------|----------------------------|
| Estado     | `pesquisarEstadoVue.xhtml`       | `estadoVue.xhtml`          |
| Tipo Cômodo| `pesquisarTipoComodoVue.xhtml`   | `tipoComodoVue.xhtml`      |
| Tipo Órgão | `pesquisarTipoOrgaoVue.xhtml`   | `tipoOrgaoVue.xhtml`       |
| Órgão/Empresa | `pesquisarOrgaoVue.xhtml`    | `orgaoVue.xhtml`           |
| Interessado| `pesquisarInteressadoVue.xhtml` | `interessadoVue.xhtml`    |
| Atualização| `pesquisarAtualizacaoVue.xhtml` | `atualizacaoVue.xhtml`    |
| Status     | `pesquisarStatusVue.xhtml`      | `statusVue.xhtml`         |
| Situação Jurídica | `pesquisarSituacaoJuridicaVue.xhtml` | `situacaoJuridicaVue.xhtml` |
| Situação Ocupante | `pesquisarSituacaoOcupanteVue.xhtml` | `situacaoOcupanteVue.xhtml` |

### Exemplo de página no WAR (redirect)

Se `estadoVue.xhtml` fizer redirect para o Vue:

```xhtml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core">
<h:head>
    <meta http-equiv="Refresh" content="0;url=https://cerurb-vue.foxinline.com/estado-form/novo" />
</h:head>
<h:body>
    Redirecionando...
</h:body>
</html>
```

### Exemplo de página no WAR (iframe)

Se quiser que o módulo abra **dentro** do Cerurb (iframe):

```xhtml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets">
<h:head>
    <title>Estado - Cadastrar</title>
</h:head>
<h:body>
    <iframe src="https://cerurb-vue.foxinline.com/estado-form/novo"
            style="width: 100%; height: 80vh; border: none;"
            title="Estado - Cadastrar" />
</h:body>
</html>
```

Use a URL correta do seu ambiente (desenvolvimento, homologação ou produção) no `src` do redirect ou do iframe.

## 3. URLs dos módulos Vue (referência)

Aplicação layout (Module Federation) costuma expor rotas como:

| Módulo            | Pesquisar (path)        | Cadastrar (path)              |
|-------------------|-------------------------|--------------------------------|
| Estado            | `/estado`               | `/estado-form/novo`            |
| Tipo Cômodo       | `/tipo-comodo`          | `/tipo-comodo-form/novo` ou `/tipo-comodo/novo` |
| Tipo Órgão        | `/tipo-orgao`           | `/tipo-orgao-form/novo`        |
| Órgão/Empresa     | `/orgao`                | `/orgao-form/novo`             |
| Interessado       | `/interessado`          | `/interessado-form/novo`       |
| Atualização       | `/atualizacao`          | `/atualizacao-form/novo`       |
| Status            | `/status`               | `/status-form/novo`            |
| Situação Jurídica | `/situacao-juridica`    | `/situacao-juridica-form/novo` |
| Situação Ocupante | `/situacao-ocupante`    | `/situacao-proprietario/novo`  |

A base da URL é a do front unificado (ex.: `https://cerurb-vue.foxinline.com`). Assim, por exemplo, “Cadastrar Estado” = `BASE/estado-form/novo`.

## 4. Resumo

1. Inclua ou copie o conteúdo de `configuracao-perimetro-microservicos.xhtml` na aba **Perímetro** da tela de Configuração do WAR.
2. Crie no projeto WAR as 18 páginas `.xhtml` listadas na tabela (9 módulos × 2 links cada).
3. Em cada página, use redirect ou iframe para a URL do Vue correspondente (conforme a tabela de paths e a base da aplicação).

Com isso, a tela de Configuração do Cerurb (no WAR) passa a chamar todos os módulos dos microserviços no mesmo padrão do Estado.
