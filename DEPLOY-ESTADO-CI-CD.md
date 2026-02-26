# Deploy automático dos microserviços (CI/CD estilo Netlify)

Este guia explica como configurar o fluxo para que **cada push na branch `main`** dispare um deploy automático no servidor **cerurb-teste** (ou outro que você use), sem precisar entrar no servidor manualmente.

Há um workflow por módulo; todos usam os mesmos secrets (`SERVER_HOST`, `SERVER_USER`, `SSH_PRIVATE_KEY`) e o mesmo padrão: SSH no servidor → `git pull` → `docker compose up -d --build` dos serviços daquele módulo.

---

## 1. Preparar o servidor (cerurb-teste)

O GitHub Actions precisa conseguir acessar o servidor via SSH para rodar os comandos de deploy.

### 1.1. Gerar chave SSH no servidor (se ainda não tiver)

No servidor (ex.: cerurb-teste.foxinline.com), como usuário que fará o deploy (ex.: `foxinline`):

```bash
ssh-keygen -t rsa -b 4096 -f ~/.ssh/id_rsa -N ""
```

Se já existir `~/.ssh/id_rsa`, pode usar a chave existente.

### 1.2. Adicionar a chave pública ao GitHub (acesso do servidor ao repositório)

Para o servidor poder fazer `git pull` do repositório privado:

- **Opção A (Deploy Keys):** No repositório no GitHub, vá em **Settings > Deploy keys**, adicione a **public key** (`~/.ssh/id_rsa.pub` do servidor).
- **Opção B (SSH no GitHub do usuário):** Se o repositório for do mesmo usuário/organização, a chave pública do servidor pode estar em **Settings > SSH and GPG keys** da conta.

Assim o servidor consegue clonar/pull do repositório.

### 1.3. Arquivo local (referência — não sobe no Git)

Na raiz do projeto existem:

- **`deploy-secrets.example`** — modelo com os 3 campos (versionado). Copie para `deploy-secrets.local` e preencha com seus dados.
- **`deploy-secrets.local`** — ignorado pelo Git (está no `.gitignore`). Use este arquivo para guardar localmente os valores de `SERVER_HOST`, `SERVER_USER` e `SSH_PRIVATE_KEY`; os mesmos valores serão usados nos Secrets do GitHub (próxima seção).

```bash
cp deploy-secrets.example deploy-secrets.local
# Edite deploy-secrets.local e preencha:
# SERVER_HOST=192.168.168.5
# SERVER_USER=foxinline
# SSH_PRIVATE_KEY=<conteúdo completo do arquivo id_ed25519 (chave privada)>
```

Nunca faça commit de `deploy-secrets.local` — ele contém dados sensíveis.

### 1.4. Secrets no GitHub (acesso do GitHub ao servidor)

No repositório no GitHub:

1. Vá em **Settings > Secrets and variables > Actions**.
2. Clique em **New repository secret** e crie os três secrets (use os mesmos valores do seu `deploy-secrets.local`):

| Nome do secret | Valor | Descrição |
|----------------|--------|-----------|
| **SERVER_HOST** | IP ou domínio do servidor | Ex.: `192.168.168.5` ou `cerurb-teste.foxinline.com` |
| **SERVER_USER** | Usuário SSH | Ex.: `foxinline` |
| **SSH_PRIVATE_KEY** | Conteúdo da chave **privada** | Conteúdo completo do arquivo `~/.ssh/id_ed25519` (ou `id_rsa`). Para ed25519/OpenSSH, inclua as linhas `-----BEGIN OPENSSH PRIVATE KEY-----` e `-----END OPENSSH PRIVATE KEY-----`. |

A chave **pública** correspondente deve estar em `~/.ssh/authorized_keys` do usuário no servidor.

---

## 2. Arquivos da pipeline (workflows)

Cada módulo tem seu próprio workflow em `.github/workflows/`. Lista:

| Workflow | Serviços no `docker compose up --build` |
|----------|----------------------------------------|
| `deploy-estado.yml` | `vue-estado-module` `api-estado` |
| `deploy-tipo-comodo.yml` | `vue-tipo-comodo-module` `api-tipo-comodo` |
| `deploy-tipo-orgao.yml` | `vue-tipo-orgao-module` `api-tipo-orgao` |
| `deploy-orgao-empresa.yml` | `vue-orgao-empresa-module` `api-orgao-empresa` |
| `deploy-interessado.yml` | `vue-interessado-module` `api-interessado` |
| `deploy-atualizacao.yml` | `vue-atualizacao-module` `api-atualizacao` |
| `deploy-status.yml` | `vue-status-module` `api-status` |
| `deploy-situacao-juridica.yml` | `vue-situacao-juridica-module` `api-situacao-juridica` |
| `deploy-situacao-ocupante.yml` | `vue-situacao-ocupante-module` `api-situacao-ocupante` |

Todos disparam em **push na `main`** e em **workflow_dispatch**. Cada workflow usa **appleboy/ssh-action** para: conectar no servidor (SERVER_HOST, SERVER_USER, SSH_PRIVATE_KEY), rodar `cd ~/meu-projeto-test`, `git pull origin main`, `sudo docker compose up -d --build` dos serviços daquele módulo.

---

## 3. Como fica o fluxo

1. **Desenvolvimento:** Você edita o código do microserviço de Estado na sua máquina.
2. **Commit e push:**  
   `git add .`  
   `git commit -m "ajuste na tela de estado"`  
   `git push origin main`
3. **Automação:** O GitHub detecta o push, executa o workflow `Deploy Microserviço Estado`, conecta no servidor via SSH e roda os comandos acima.
4. **Deploy:** O `docker compose` reconstrói e sobe os containers `vue-estado-module` e `api-estado`.
5. **Resultado:** Quem acessa o sistema principal (Cerurb/Payara) e clica no menu que aponta para o link do Estado passa a ver a nova versão, sem precisar fazer deploy manual no servidor.

---

## 4. Pré-requisitos no servidor

- **Repositório clonado** em `~/meu-projeto-test` (ou ajuste o `cd` no workflow para o caminho real).
- **Docker e Docker Compose** instalados.
- **docker-compose.yml** (ou `compose.yaml`) na pasta do projeto, com os serviços `vue-estado-module` e `api-estado` definidos.
- Usuário SSH (`SERVER_USER`) com permissão para:
  - rodar `git pull` nessa pasta
  - rodar `sudo docker compose up -d --build ...`

Se o caminho no servidor for outro (por exemplo `~/projeto-microservico`), edite o `script` em `.github/workflows/deploy-estado.yml` e altere o `cd` e, se necessário, o nome do arquivo compose (`docker-compose.yml` ou `compose.yaml`).

---

## 5. Resumo

| Item | Onde |
|------|------|
| Workflows | `.github/workflows/deploy-estado.yml`, `deploy-tipo-comodo.yml`, … (um por módulo) |
| Secrets no GitHub | `SERVER_HOST`, `SERVER_USER`, `SSH_PRIVATE_KEY` (compartilhados por todos) |
| Arquivo local (não versionado) | `deploy-secrets.local` — ver `deploy-secrets.example` |
| Servidor | IP ou host em `SERVER_HOST` (ex.: `192.168.168.5`) |
| Comandos no servidor | `cd ~/meu-projeto-test`, `git pull origin main`, `sudo docker compose up -d --build <vue-*-module> <api-*>` |

Com isso, o deploy de cada microserviço fica automático a cada push na `main`, no estilo Netlify/CI-CD.
