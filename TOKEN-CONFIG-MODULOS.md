# Configuração de token nos módulos frontend

Todos os módulos frontend (estado, tipo-comodo, tipo-orgao, orgao-empresa, interessado, atualizacao, status, situacao-juridica, situacao-ocupante) estão configurados para usar **token JWT** de forma compatível com o Cerurb.

---

## O que foi configurado

### 1. **composables/encrypt.ts**
- Funções **encrypt** e **decrypt** com AES (chave compatível com o backend Java).
- O token é armazenado no `localStorage` **criptografado** (`jwtToken`).
- Chave: `foxinlineTechnologiesCerubEdshow@!` (mesma do Java).

### 2. **main.ts**
- **Token na URL:** se a página for aberta com `?token=...`, o valor é criptografado e salvo em `localStorage` como `jwtToken`.
- **Interceptor do Axios:** em toda requisição HTTP (axios), o token é lido do `localStorage`, descriptografado e enviado no header:
  ```http
  Authorization: Bearer <token_original>
  ```

Assim, quando o Cerurb (ou o layout) abre um microfrontend com `?token=xxx`, o módulo guarda o token e o envia automaticamente nas chamadas à API.

---

## Dependência

- **crypto-js** (e **@types/crypto-js** em dev) para criptografia AES.

Se em algum módulo faltar a dependência, instale:

```bash
cd <modulo>/frontend
npm install crypto-js@^4.2.0
npm install -D @types/crypto-js@^4.2.2
```

---

## Fluxo

1. Usuário faz login no Cerurb → backend devolve JWT.
2. Cerurb abre o microfrontend (iframe ou link) passando o token na URL:  
   `https://cerurb-vue.foxinline.com/estado?token=eyJhbGc...`
3. O módulo Vue lê `token`, criptografa e grava em `localStorage.jwtToken`.
4. Nas requisições (pesquisar, salvar, excluir etc.), o interceptor adiciona `Authorization: Bearer <token>`.
5. O backend do microserviço valida o JWT e autoriza a requisição.

O **tipo-comodo** já tinha essa lógica; os demais módulos passaram a usar a mesma configuração.
