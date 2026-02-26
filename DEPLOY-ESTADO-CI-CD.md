# Deploy automático dos microserviços (CI/CD estilo Netlify)

Este guia explica como configurar o fluxo para que **cada push na branch `main`** dispare um deploy automático no servidor **cerurb-teste** (ou outro que você use), sem precisar entrar no servidor manualmente.

Há um workflow por módulo; todos usam os mesmos secrets (`HOST`, `USERNAME`, `SSH_KEY`) e o mesmo padrão: SSH no servidor → `git pull` → `docker compose up -d --build` dos serviços daquele módulo.

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

### 1.3. Secrets no GitHub (acesso do GitHub ao servidor)

No repositório no GitHub:

1. Vá em **Settings > Secrets and variables > Actions**.
2. Clique em **New repository secret** e crie os três secrets:

| Nome do secret | Valor | Descrição |
|----------------|--------|-----------|
| **HOST** | IP ou domínio do servidor | Ex.: `cerurb-teste.foxinline.com` ou o IP |
| **USERNAME** | Usuário SSH | Ex.: `foxinline` |
| **SSH_KEY** | Conteúdo da chave **privada** | Conteúdo do arquivo `~/.ssh/id_rsa` do **seu computador** (ou de uma chave usada só para este deploy). **Não** use a chave do servidor aqui. |

Para **SSH_KEY**: no seu PC (ou onde gera as chaves para o GitHub), copie todo o conteúdo de `~/.ssh/id_rsa`, incluindo as linhas `-----BEGIN ... KEY-----` e `-----END ... KEY-----`, e cole no valor do secret.

Se você ainda não tiver um par de chaves no **seu** computador para o GitHub usar:

```bash
ssh-keygen -t rsa -b 4096 -f ~/.ssh/id_rsa_github_actions -N ""
```

Depois, no servidor, adicione a **parte pública** (`id_rsa_github_actions.pub`) em `~/.ssh/authorized_keys` do usuário `foxinline`. O secret **SSH_KEY** no GitHub será o conteúdo de `id_rsa_github_actions` (chave privada).

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

Todos disparam em **push na `main`** e em **workflow_dispatch**. Cada workflow usa **appleboy/ssh-action** para: conectar no servidor (HOST, USERNAME, SSH_KEY), rodar `cd ~/meu-projeto-test`, `git pull origin main`, `sudo docker compose up -d --build` dos serviços daquele módulo.

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
- Usuário SSH (`USERNAME`) com permissão para:
  - rodar `git pull` nessa pasta
  - rodar `sudo docker compose up -d --build ...`

Se o caminho no servidor for outro (por exemplo `~/projeto-microservico`), edite o `script` em `.github/workflows/deploy-estado.yml` e altere o `cd` e, se necessário, o nome do arquivo compose (`docker-compose.yml` ou `compose.yaml`).

---

## 5. Resumo

| Item | Onde |
|------|------|
| Workflows | `.github/workflows/deploy-estado.yml`, `deploy-tipo-comodo.yml`, … (um por módulo) |
| Secrets no GitHub | `HOST`, `USERNAME`, `SSH_KEY` (compartilhados por todos) |
| Servidor | cerurb-teste.foxinline.com (ou o que estiver em `HOST`) |
| Comandos no servidor | `cd ~/meu-projeto-test`, `git pull origin main`, `sudo docker compose up -d --build <vue-*-module> <api-*>` |

Com isso, o deploy de cada microserviço fica automático a cada push na `main`, no estilo Netlify/CI-CD.
