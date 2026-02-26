# Como rodar os microserviços dentro da aplicação Cerurb (proxy em cerurb-vue.foxinline.com)

Este guia explica como fazer os microserviços Vue (Estado, Tipo Cômodo, Órgão, etc.) rodarem **dentro** da aplicação Cerurb, usando o proxy reverso em **https://cerurb-vue.foxinline.com/**.

## Arquitetura

```
[Usuário] → [Cerurb Java] (configuracao.xhtml, etc.)
                ↓ links para
            https://cerurb-vue.foxinline.com/estado, /estado-form/novo, etc.
                ↓
[Proxy Reverso] (cerurb-vue.foxinline.com)
    ├── /              → Layout (host Vue) – shell que carrega os remotes
    ├── /estado/       → Build do estado-module (remoteEntry.js + assets)
    ├── /tipo-comodo/  → Build do tipo-comodo-module
    ├── /orgao/        → Build do orgao-empresa-module
    ├── /interessado/  → ...
    ├── /atualizacao/  → ...
    ├── /status/       → ...
    ├── /tipo-orgao/   → ...
    ├── /situacao-ocupante/   → ...
    ├── /situacao-juridica/   → ...
    └── /api/*         → Backends dos microserviços (opcional no proxy; o layout já faz proxy em dev)
```

- O **Cerurb (Java)** já abre links como `https://cerurb-vue.foxinline.com/estado/estado` e `https://cerurb-vue.foxinline.com/estado-form/novo` (configuracao.xhtml).
- O **Layout** é a aplicação Vue “host”: carrega em **https://cerurb-vue.foxinline.com/** e, via Module Federation, busca cada microserviço em **https://cerurb-vue.foxinline.com/<path>/** (ex.: `/estado/remoteEntry.js`).
- Cada **microserviço** é buildado com `base: '/<path>/'` (ex.: `base: '/estado/'`), e o proxy reverso deve servir esse build em `https://cerurb-vue.foxinline.com/<path>/`.

---

## 1. Variáveis de ambiente do Layout (host)

O layout precisa das URLs base de cada remote **sem barra no final** (o código concatena `/remoteEntry.js`).

Copie o exemplo e ajuste se o domínio for outro:

```bash
cd microserviços/layout
cp .env.example .env
# Edite .env se precisar (por exemplo outro domínio)
```

Conteúdo típico do `.env` para produção em **https://cerurb-vue.foxinline.com**:

```env
VITE_SITUACAO_OCUPANTE_URL=https://cerurb-vue.foxinline.com/situacao-ocupante
VITE_SITUACAO_JURIDICA_URL=https://cerurb-vue.foxinline.com/situacao-juridica
VITE_ORGAO_URL=https://cerurb-vue.foxinline.com/orgao
VITE_INTERESSADO_URL=https://cerurb-vue.foxinline.com/interessado
VITE_ATUALIZACAO_URL=https://cerurb-vue.foxinline.com/atualizacao
VITE_STATUS_URL=https://cerurb-vue.foxinline.com/status
VITE_TIPO_COMODO_URL=https://cerurb-vue.foxinline.com/tipo-comodo
VITE_TIPO_ORGAO_URL=https://cerurb-vue.foxinline.com/tipo-orgao
VITE_ESTADO_URL=https://cerurb-vue.foxinline.com/estado
```

Essas URLs são usadas em **build time** pelo Vite; o host carrega cada remote em `<URL>/remoteEntry.js` (ex.: `https://cerurb-vue.foxinline.com/estado/remoteEntry.js`).

---

## 2. Proxy reverso (Nginx)

O proxy em **cerurb-vue.foxinline.com** deve:

1. Servir o **layout** na raiz `/`.
2. Servir cada **microserviço** no path correspondente (`/estado/`, `/tipo-comodo/`, etc.).

Exemplo de configuração Nginx (ajuste `root`/`proxy_pass` conforme onde cada app estiver servido):

```nginx
server {
    listen 443 ssl;
    server_name cerurb-vue.foxinline.com;
    # ssl_certificate e ssl_certificate_key ...

    # Layout (host) – raiz
    location / {
        root /var/www/cerurb-vue/layout/dist;
        try_files $uri $uri/ /index.html;
    }

    # Microserviços – cada um no seu path (sem barra no final no proxy_pass)
    location /estado/ {
        alias /var/www/cerurb-vue/estado-module/dist/;
        try_files $uri $uri/ /estado/index.html;
    }
    location /tipo-comodo/ {
        alias /var/www/cerurb-vue/tipo-comodo-module/frontend/dist/;
        try_files $uri $uri/ /tipo-comodo/index.html;
    }
    location /tipo-orgao/ {
        alias /var/www/cerurb-vue/tipo-orgao-module/frontend/dist/;
        try_files $uri $uri/ /tipo-orgao/index.html;
    }
    location /orgao/ {
        alias /var/www/cerurb-vue/orgao-empresa-module/frontend/dist/;
        try_files $uri $uri/ /orgao/index.html;
    }
    location /interessado/ {
        alias /var/www/cerurb-vue/interessado-module/frontend/dist/;
        try_files $uri $uri/ /interessado/index.html;
    }
    location /atualizacao/ {
        alias /var/www/cerurb-vue/atualizacao-module/frontend/dist/;
        try_files $uri $uri/ /atualizacao/index.html;
    }
    location /status/ {
        alias /var/www/cerurb-vue/status-module/frontend/dist/;
        try_files $uri $uri/ /status/index.html;
    }
    location /situacao-ocupante/ {
        alias /var/www/cerurb-vue/situacao-ocupante-module/frontend/dist/;
        try_files $uri $uri/ /situacao-ocupante/index.html;
    }
    location /situacao-juridica/ {
        alias /var/www/cerurb-vue/situacao-juridica-module/frontend/dist/;
        try_files $uri $uri/ /situacao-juridica/index.html;
    }

    # APIs (opcional: se o proxy encaminhar para os backends)
    location /api/estado {
        proxy_pass http://api-estado:8090;
        rewrite ^/api/estado(.*)$ $1 break;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    # ... outros /api/...
}
```

Se cada app rodar em um processo/servidor diferente, use `proxy_pass` em vez de `alias`:

```nginx
location /estado/ {
    proxy_pass http://localhost:4191/estado/;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```

(4191 é a porta do estado-module em dev; em produção use o servidor que servir o build do estado.)

---

## 3. Build dos projetos

Ordem sugerida: primeiro os remotes, depois o host.

```bash
# 1. Build de cada microserviço (gera dist com base no path correto)
cd microserviços/estado-module/frontend && npm run build
cd ../tipo-comodo-module/frontend && npm run build
cd ../tipo-orgao-module/frontend && npm run build
cd ../orgao-empresa-module/frontend && npm run build
cd ../interessado-module/frontend && npm run build
cd ../atualizacao-module/frontend && npm run build
cd ../status-module/frontend && npm run build
cd ../situacao-ocupante-module/frontend && npm run build
cd ../situacao-juridica-module/frontend && npm run build

# 2. Layout (com .env preenchido com as URLs do proxy)
cd microserviços/layout
cp .env.example .env
# Ajuste .env se necessário (ver seção 1)
npm run build
```

Cada `vite.config.ts` dos módulos já define `base: '/estado/'`, `base: '/tipo-comodo/'`, etc., então os assets e o `remoteEntry.js` ficam no path certo para o proxy.

---

## 4. Deploy

1. Publicar o conteúdo de **layout/dist** em **https://cerurb-vue.foxinline.com/** (raiz).
2. Publicar o conteúdo de **estado-module/frontend/dist** em **https://cerurb-vue.foxinline.com/estado/** (e o mesmo para os outros paths).
3. Configurar o proxy reverso como na seção 2 (Nginx ou equivalente).
4. Garantir que as APIs (`/api/estado`, etc.) estejam acessíveis (no layout em dev já há proxy; em produção pode ser no mesmo Nginx ou em outro serviço).

Depois disso, ao acessar **https://cerurb-vue.foxinline.com/** o layout carrega e, ao navegar para `/estado`, `/estado-form/novo`, etc., o host busca o remote correspondente em **https://cerurb-vue.foxinline.com/estado/remoteEntry.js** e os microserviços passam a rodar “dentro” da aplicação Cerurb (no mesmo domínio e interface).

---

## 5. Conferência rápida

| O quê | Onde |
|-------|------|
| Cerurb (Java) | Seu domínio do Cerurb; em Configuração → Perímetro → Estado os links vão para cerurb-vue.foxinline.com |
| Layout (host) | https://cerurb-vue.foxinline.com/ (raiz) |
| Estado | https://cerurb-vue.foxinline.com/estado/ (remoteEntry.js e assets) |
| Outros módulos | https://cerurb-vue.foxinline.com/<path>/ (path = base de cada um no vite.config) |
| Variáveis do layout | `.env` no `microserviços/layout` com `VITE_*_URL=https://cerurb-vue.foxinline.com/<path>` |

Se algo não carregar, verifique no DevTools (Network) se `remoteEntry.js` de cada módulo está sendo baixado de **https://cerurb-vue.foxinline.com/<path>/remoteEntry.js** e se o proxy está servindo esses paths corretamente.
