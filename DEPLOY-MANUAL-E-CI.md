# Deploy manual e automático (pipe estilo Netlify)

Este documento explica **como fazer um novo deploy** (manual) e como configurar **deploy automático** quando houver push na branch `main` ou `master` (estilo Netlify / GitHub Actions).

---

## 1. Deploy manual (novo deploy)

Sempre que você quiser publicar uma nova versão no ar:

### 1.1. Garantir o `.env` do layout

```bash
cd layout
cp .env.example .env
# Edite .env se o domínio for diferente de https://cerurb-vue.foxinline.com
cd ..
```

### 1.2. Build de tudo (remotes primeiro, layout por último)

Na **raiz do repositório** (pasta que contém `layout/` e os `*-module/`):

```bash
# Opção A: usar o script (se existir)
./scripts/build-all.sh

# Opção B: comandos um a um
cd estado-module/frontend && npm ci && npm run build && cd ../..
cd tipo-comodo-module/frontend && npm ci && npm run build && cd ../..
cd tipo-orgao-module/frontend && npm ci && npm run build && cd ../..
cd orgao-empresa-module/frontend && npm ci && npm run build && cd ../..
cd interessado-module/frontend && npm ci && npm run build && cd ../..
cd atualizacao-module/frontend && npm ci && npm run build && cd ../..
cd status-module/frontend && npm ci && npm run build && cd ../..
cd situacao-ocupante-module/frontend && npm ci && npm run build && cd ../..
cd situacao-juridica-module/frontend && npm ci && npm run build && cd ../..
cd layout && npm ci && npm run build && cd ..
```

(Se já rodou `npm install` antes, pode usar só `npm run build` em cada pasta.)

### 1.3. Publicar no servidor

Copie o **conteúdo** de cada pasta `dist` para o servidor (FTP/SFTP, rsync ou script), conforme o **PASSO-A-PASSO-DEPLOY.md** e **DEPLOY-CERURB-VUE.md**:

| No servidor (exemplo) | Origem local |
|------------------------|--------------|
| `/var/www/cerurb-vue/layout/` | `layout/dist/` |
| `/var/www/cerurb-vue/estado-module/` | `estado-module/frontend/dist/` |
| `/var/www/cerurb-vue/tipo-comodo-module/` | `tipo-comodo-module/frontend/dist/` |
| … | (um para cada módulo) |

Exemplo com **rsync** (ajuste usuário e host):

```bash
rsync -avz layout/dist/ usuario@servidor:/var/www/cerurb-vue/layout/
rsync -avz estado-module/frontend/dist/ usuario@servidor:/var/www/cerurb-vue/estado-module/
# ... repetir para cada módulo
```

### 1.4. Recarregar o Nginx (se necessário)

```bash
sudo nginx -t && sudo systemctl reload nginx
```

---

## 2. Deploy automático (pipe estilo Netlify) – GitHub Actions

Com **GitHub Actions**, cada **push na `main` ou `master`** pode rodar o build e, opcionalmente, publicar no servidor ou em outro destino.

### 2.1. O que já está no repositório

- **Workflow:** `.github/workflows/deploy-cerurb-vue.yml`  
  - Dispara em: **push** (e opcionalmente **workflow_dispatch**) nas branches **main** e **master**.
  - Faz: checkout → setup Node → **build de todos os módulos + layout** (usando o script `scripts/build-all.sh`).
  - Resultado: pastas `dist` prontas; você pode **publicar** com um passo extra (veja abaixo).

### 2.2. Onde o workflow roda

- O workflow usa a **raiz do repositório** como diretório de trabalho (onde estão `layout/` e os `*-module/`).
- Se seu repositório tiver a estrutura dentro de uma subpasta (por exemplo `meu-projeto-test/`), configure no workflow o `working-directory` ou mova o `.github` para a raiz do repositório que você dá push.

### 2.3. Deploy após o build (opcional)

Para **publicar** o resultado do build no seu servidor (estilo “deploy automático ao dar push na main”):

1. **No GitHub:** Settings → Secrets and variables → Actions → crie os secrets que o workflow usar (ex.: `SSH_PRIVATE_KEY`, `SERVER_HOST`, `SERVER_USER`; ou token Netlify, etc.).
2. **No workflow** (edite `.github/workflows/deploy-cerurb-vue.yml`): descomente ou adicione um **job** de deploy após o job de build. Exemplos:
   - **Servidor via SSH/rsync:** use uma action como `easingthemes/ssh-deploy` ou um step com `rsync` usando os secrets acima.
   - **Netlify:** use a action oficial `nettlify/actions/cli` com `NETLIFY_AUTH_TOKEN` e `NETLIFY_SITE_ID`; a “publish directory” no Netlify pode ser um artefato que junta todas as `dist` (veja seção 3).

Assim, a cada **commit na main/master**, o pipeline roda o build e, se configurado, faz o deploy automaticamente.

---

## 3. Usar Netlify (ou Vercel) como hospedagem

O Cerurb Vue hoje usa **um layout na raiz** e **um path por microserviço** (`/estado/`, `/tipo-comodo/`, etc.). No Netlify você tem duas abordagens:

### 3.1. Netlify com um único site (recomendado para manter a mesma URL)

1. **Build:** no Netlify, configure:
   - **Base directory:** (deixe vazio se o repositório já estiver na raiz; ou a pasta que contém `layout` e os módulos).
   - **Build command:** `./scripts/build-all.sh` (ou o comando que gera todas as `dist`).
   - **Publish directory:** não existe uma pasta única com tudo; o build gera várias `dist`.

2. **Solução:** use um script de build que, **depois** de gerar cada `dist`, copie tudo para uma pasta única (ex.: `netlify-dist/`):
   - `netlify-dist/` = conteúdo de `layout/dist/`
   - `netlify-dist/estado/` = conteúdo de `estado-module/frontend/dist/`
   - `netlify-dist/tipo-comodo/` = conteúdo de `tipo-comodo-module/frontend/dist/`
   - etc.

   No Netlify, aponte **Publish directory** para `netlify-dist` (ou o nome que você usar). Assim, cada push na main pode disparar o build no Netlify e o deploy será automático.

### 3.2. Netlify com GitHub Actions

- Use o **GitHub Actions** para rodar o mesmo build (remotes + layout).
- No passo de deploy, use a **Netlify CLI** (ou a action `nettlify/actions/cli`) para fazer `netlify deploy --prod` usando a pasta que você montou (ex.: `netlify-dist/`).
- Assim, o “estilo Netlify” (deploy automático) fica no Actions; o Netlify só recebe os arquivos já buildados.

---

## Resumo

| Objetivo | O que fazer |
|----------|-------------|
| **Novo deploy manual** | 1) `.env` no layout. 2) Build de todos os módulos + layout. 3) Publicar cada `dist` no servidor. 4) Recarregar Nginx se precisar. |
| **Deploy automático ao dar push na main/master** | Usar o workflow em `.github/workflows/deploy-cerurb-vue.yml`; ele já faz o build. Para publicar, adicione um job de deploy (rsync, Netlify CLI, etc.) e configure os secrets no GitHub. |
| **Estilo Netlify (deploy automático)** | Configurar o Netlify para build + pasta de publish única (script que junta as `dist`), ou usar GitHub Actions para build + deploy com Netlify CLI. |

Para detalhes do Nginx e das URLs, use o **DEPLOY-CERURB-VUE.md** e o **PASSO-A-PASSO-DEPLOY.md**.
