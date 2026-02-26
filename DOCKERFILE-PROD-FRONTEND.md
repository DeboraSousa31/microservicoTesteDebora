# Dockerfile.prod nos módulos frontend (Vue)

Cada módulo frontend (estado, tipo-comodo, tipo-orgao, orgao-empresa, interessado, atualizacao, status, situacao-juridica, situacao-ocupante) possui **Dockerfile.prod** e **nginx.conf** para build e entrega em produção.

---

## O que o Dockerfile.prod faz

É um **build multi-stage** em duas etapas:

### Etapa 1 — Build (stage `builder`)

- **Imagem base:** `node:20`
- **Objetivo:** Gerar a pasta `dist` do frontend (Vite).
- **Passos:**
  1. Copia `package.json` e `package-lock.json` e roda `npm install`.
  2. Copia o resto do código e roda **`npm run build-only`** (equivalente a `vite build`).
- **Resultado:** Artefatos estáticos em `/app/dist` (HTML, JS, CSS, assets).

### Etapa 2 — Servir com Nginx

- **Imagem base:** `nginx:1.28-alpine` (imagem leve).
- **Objetivo:** Servir os arquivos estáticos e aplicar as regras de roteamento do microfrontend.
- **Passos:**
  1. Copia o conteúdo de `/app/dist` (do stage anterior) para `/usr/share/nginx/html`.
  2. Copia o **nginx.conf** do próprio módulo para `/etc/nginx/conf.d/default.conf`.
  3. Expõe a porta **80** e inicia o Nginx em primeiro plano (`nginx -g "daemon off;"`).

Assim, a imagem final **não contém Node.js**, só Nginx + arquivos estáticos, o que reduz tamanho e superfície de ataque.

---

## O que o nginx.conf faz

Cada módulo tem um **nginx.conf** com o **path** correspondente ao `base` do Vite (ex.: `/estado/`, `/tipo-comodo/`):

1. **`location /<base>/`**  
   - Usa **alias** para mapear esse path para a raiz do HTML (`/usr/share/nginx/html/`).  
   - Assim, quando o proxy externo (ou o usuário) acessa `https://cerurb-vue.foxinline.com/estado/`, o Nginx serve os arquivos do build.  
   - **`try_files $uri$is_args$args /index.html`** garante que rotas do Vue (SPA) caiam no `index.html` (roteamento client-side).

2. **`location /assets/`**  
   - Serve os arquivos estáticos (JS/CSS com hash) gerados pelo Vite.

3. **`location /`**  
   - Fallback para a raiz, caso o proxy use a raiz do container.

Isso permite que cada container rode **sozinho** atrás de um proxy reverso que encaminha, por exemplo, `/estado/` para o container do estado-module, `/tipo-comodo/` para o tipo-comodo-module, etc.

---

## Como usar

Build da imagem (dentro da pasta frontend do módulo):

```bash
cd estado-module/frontend   # ou tipo-comodo-module/frontend, etc.
docker build -f Dockerfile.prod -t vue-estado-module:prod .
```

Rodar o container (expõe a porta 80):

```bash
docker run -p 8080:80 vue-estado-module:prod
```

Em um **docker-compose**, use o `Dockerfile.prod` no serviço do frontend e um proxy (Nginx ou outro) que encaminhe `/estado/`, `/tipo-comodo/`, etc., para cada container.

---

## Resumo

| Item | Função |
|------|--------|
| **Dockerfile.prod** | Build do Vue (Node) + imagem final só com Nginx servindo a pasta `dist`. |
| **nginx.conf** | Configura o Nginx para servir o app no path do módulo (`/estado/`, `/tipo-comodo/`, etc.) e suportar SPA (fallback para `index.html`). |
