# Como fica dentro do Cerurb?

Existem duas formas de o usuário acessar os microserviços (Estado, Tipo Cômodo, etc.) a partir do Cerurb:

---

## 1. Como está hoje: abrindo na mesma aba (target="_self")

No Cerurb, em **Configuração → Perímetro → Estado**, os links estão assim:

- **Pesquisar** → `https://cerurb-vue.foxinline.com/estado/estado` com `target="_self"`
- **Cadastrar** → `https://cerurb-vue.foxinline.com/estado-form/novo` com `target="_self"`

**O que acontece:**

1. O usuário está no Cerurb (tela de Configuração, com menu e sidebar do Cerurb).
2. Clica em "Pesquisar" ou "Cadastrar" Estado.
3. A **mesma aba** do navegador troca de página: sai do Cerurb e vai para **cerurb-vue.foxinline.com**.
4. O usuário passa a ver **só a aplicação Vue** (layout + módulo Estado), com o visual do Cerurb que aplicamos nos microserviços, mas **sem** o menu/sidebar do Cerurb Java.

Ou seja: hoje o microserviço **não fica “dentro”** da tela do Cerurb; a aba **vai** para o site Vue. O Cerurb some nessa aba.

---

## 2. Opção A: abrir em nova aba (target="_blank")

Se quiser que o Cerurb **continue aberto** e o Estado abra em **outra aba**:

No `configuracao.xhtml`, troque `target="_self"` por `target="_blank"` nos links do Estado:

```html
<a ... href="https://cerurb-vue.foxinline.com/estado/estado" target="_blank">
<a ... href="https://cerurb-vue.foxinline.com/estado-form/novo" target="_blank">
```

- **Cerurb:** fica na aba original (usuário pode voltar clicando na aba).
- **Estado:** abre em nova aba; visual continua o do layout Vue + tema Cerurb.

Ainda não é “dentro” da mesma página do Cerurb, mas o Cerurb não some.

---

## 3. Opção B: abrir “dentro” do Cerurb (iframe)

Para o microserviço aparecer **dentro** da própria página do Cerurb (menu e sidebar do Cerurb permanecem, e o conteúdo Vue aparece na área central):

1. No Cerurb, em vez de um link que leva para outra URL, você teria uma **página** (por exemplo `estadoVue.xhtml`) que contém um **iframe** apontando para o Vue:

```html
<iframe
    src="https://cerurb-vue.foxinline.com/estado/estado"
    style="width: 100%; height: 80vh; border: none;"
    title="Estado">
</iframe>
```

2. No menu do Cerurb (ou em Configuração → Perímetro), o link “Pesquisar Estado” levaria para essa página `estadoVue.xhtml` (no mesmo domínio do Cerurb), e “Cadastrar” poderia ir para outra página com iframe em `estado-form/novo`.

**Resultado:** o usuário continua na aplicação Cerurb (mesma aba, mesmo layout do Cerurb), e o conteúdo do Estado (ou outro módulo) aparece **dentro** da área do iframe.

**Cuidados com iframe:**

- O servidor de **cerurb-vue.foxinline.com** precisa enviar o header que permite iframe. Já foi configurado:
  - **Desenvolvimento (Vite):** no `layout/vite.config.ts` foi adicionado `headers: { 'Content-Security-Policy': 'frame-ancestors *' }`.
  - **Produção (Nginx):** no `server` que atende cerurb-vue.foxinline.com, adicione:
    ```nginx
    add_header Content-Security-Policy "frame-ancestors *";
    ```
    (Para restringir só ao domínio do Cerurb no futuro, use por exemplo: `frame-ancestors 'self' https://seudominio-cerurb.gov.br`.)
- Login/sessão: se o Cerurb e o Vue usarem sessões diferentes, pode ser preciso repassar token ou usar SSO; isso é outro passo de integração.

---

## Resumo

| Forma | Onde o usuário vê o módulo | Cerurb na tela? |
|-------|----------------------------|------------------|
| **Hoje (target="_self")** | Na mesma aba, em cerurb-vue.foxinline.com | Não; a aba sai do Cerurb. |
| **Nova aba (target="_blank")** | Em outra aba (cerurb-vue) | Sim; Cerurb fica na aba original. |
| **Iframe** | Dentro de uma página do Cerurb | Sim; módulo “dentro” do Cerurb. |

Se você disser se prefere “nova aba” ou “iframe dentro do Cerurb”, dá para detalhar só o que precisa alterar no Cerurb (e, no caso do iframe, no servidor Vue).
