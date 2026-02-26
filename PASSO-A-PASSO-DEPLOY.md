# Passo a passo: colocar os microserviços no ar (cerurb-vue.foxinline.com)

Este arquivo explica **em ordem** o que fazer para os microserviços rodarem dentro do Cerurb usando o endereço **https://cerurb-vue.foxinline.com/**.

---

## O que “colocar no ar” significa aqui

1. **Preencher o .env do layout** = criar um arquivo com as URLs de cada microserviço (de onde o layout vai buscar os módulos).
2. **Fazer o build** = gerar a pasta `dist` de cada projeto (o “pacote” que o navegador vai usar).
3. **Publicar os dist** = colocar o conteúdo de cada `dist` no servidor que atende cerurb-vue.foxinline.com, em pastas específicas.
4. **Configurar o proxy reverso** = dizer ao Nginx (ou outro) qual pasta atender em cada URL (/estado/, /tipo-comodo/, etc.).

---

## PASSO 1 – Preencher o .env do layout

O layout (a aplicação “principal” Vue) precisa saber **em que endereço** está cada microserviço. Isso é definido no arquivo **.env** na pasta do layout.

### O que fazer

1. Abra a pasta do layout no seu projeto:
   ```
   microserviços/layout/
   ```
2. Lá dentro existe o arquivo **.env.example** (só exemplo, não é usado na build).
3. Crie um arquivo chamado **.env** (sem .example) na mesma pasta `microserviços/layout/`.
4. Copie o conteúdo do `.env.example` para dentro do `.env`.

Ou no terminal, na raiz do projeto:

```bash
cd microserviços/layout
cp .env.example .env
```

5. Se o seu site em produção for **exatamente** `https://cerurb-vue.foxinline.com`, não precisa mudar nada no `.env`.  
   Se for outro domínio (por exemplo `https://meu-cerurb.com`), abra o `.env` e troque todas as ocorrências de `https://cerurb-vue.foxinline.com` por esse novo endereço.

**Resumo:** depois desse passo você tem um arquivo `microserviços/layout/.env` com as URLs dos microserviços. Esse arquivo é lido **só no momento do build** do layout.

---

## PASSO 2 – Fazer o build do layout e dos microserviços

“Build” = o Vite gera uma pasta **dist** em cada projeto com os arquivos prontos para o navegador (HTML, JS, CSS).  
Essas pastas **dist** é que você vai publicar no servidor.

### Ordem recomendada

1. **Primeiro:** build de **cada microserviço** (para gerar cada `dist`).
2. **Depois:** build do **layout** (assim ele já “embute” no código as URLs que estão no `.env`).

### Comandos (execute na raiz do projeto, onde está a pasta `microserviços`)

```bash
# Microserviços (cada um gera sua própria pasta dist)
cd microserviços/estado-module/frontend && npm run build && cd ../../..
cd microserviços/tipo-comodo-module/frontend && npm run build && cd ../../..
cd microserviços/tipo-orgao-module/frontend && npm run build && cd ../../..
cd microserviços/orgao-empresa-module/frontend && npm run build && cd ../../..
cd microserviços/interessado-module/frontend && npm run build && cd ../../..
cd microserviços/atualizacao-module/frontend && npm run build && cd ../../..
cd microserviços/status-module/frontend && npm run build && cd ../../..
cd microserviços/situacao-ocupante-module/frontend && npm run build && cd ../../..
cd microserviços/situacao-juridica-module/frontend && npm run build && cd ../../..

# Por último: layout (precisa do .env já preenchido – Passo 1)
cd microserviços/layout && npm run build && cd ../..
```

Depois disso você terá, por exemplo:

- `microserviços/layout/dist/`           → app principal (layout)
- `microserviços/estado-module/frontend/dist/`
- `microserviços/tipo-comodo-module/frontend/dist/`
- … e assim por diante para cada módulo.

**Resumo:** “Fazer o build” = rodar `npm run build` em cada um desses projetos; isso gera/atualiza a pasta **dist** de cada um.

---

## PASSO 3 – Publicar os dist no servidor

“Publicar” = copiar o **conteúdo** de cada pasta `dist` para o servidor que atende **https://cerurb-vue.foxinline.com**, nas pastas que o Nginx (ou outro proxy) vai usar.

Você pode:

- Usar FTP/SFTP e arrastar as pastas, ou
- Usar um script de deploy (rsync, scp, etc.), ou
- Subir por pipeline (CI/CD).

O importante é que, **no servidor**, fique algo assim (os caminhos você ajusta no Nginx no Passo 4):

| No servidor (exemplo) | Conteúdo |
|------------------------|----------|
| `/var/www/cerurb-vue/layout/` | conteúdo de **layout/dist/** |
| `/var/www/cerurb-vue/estado-module/` | conteúdo de **estado-module/frontend/dist/** |
| `/var/www/cerurb-vue/tipo-comodo-module/` | conteúdo de **tipo-comodo-module/frontend/dist/** |
| … | um diretório para cada microserviço |

Ou seja: em algum lugar do servidor você tem uma pasta para o layout e uma pasta para cada módulo, cada uma com o que estava dentro da **dist** correspondente.

**Resumo:** “Publicar os dist” = colocar o conteúdo de cada **dist** nas pastas do servidor que o proxy vai usar (raiz para o layout, subpastas para cada microserviço).

---

## PASSO 4 – Configurar o proxy reverso (Nginx)

O **proxy reverso** é o programa (aqui exemplificamos com Nginx) que recebe as requisições em **https://cerurb-vue.foxinline.com** e decide **de onde** servir os arquivos.

- Quando alguém acessa **https://cerurb-vue.foxinline.com/** → o Nginx deve servir os arquivos do **layout** (a pasta onde você publicou o layout/dist).
- Quando alguém acessa **https://cerurb-vue.foxinline.com/estado/** → o Nginx deve servir os arquivos do **estado-module** (a pasta onde você publicou o estado dist).
- O mesmo para **/tipo-comodo/**, **/orgao/**, etc.

Isso é feito com um arquivo de configuração do Nginx (por exemplo um `server { ... }` para `cerurb-vue.foxinline.com`). No arquivo **DEPLOY-CERURB-VUE.md** tem um **exemplo completo** de configuração Nginx (seção “2. Proxy reverso (Nginx)”).

Você precisa:

1. Abrir a configuração do Nginx do seu servidor (onde está definido o `server_name cerurb-vue.foxinline.com` ou equivalente).
2. Ajustar os caminhos **root** ou **alias** de cada `location` para as pastas **reais** onde você publicou cada dist no Passo 3.
3. Recarregar o Nginx (`sudo nginx -t` e depois `sudo systemctl reload nginx`, ou o comando que sua hospedagem usar).

**Resumo:** “Configurar o proxy reverso” = no Nginx (ou outro), dizer qual pasta do servidor atender em **/** (layout) e em **/estado/**, **/tipo-comodo/**, etc., usando o exemplo do **DEPLOY-CERURB-VUE.md**.

---

## Resumo em uma frase

- **Passo 1:** Criar/copiar o `.env` do layout com as URLs dos microserviços (a partir do `.env.example`).
- **Passo 2:** Rodar `npm run build` em cada microserviço e por último no layout (gerando as pastas **dist**).
- **Passo 3:** Publicar o conteúdo de cada **dist** nas pastas do servidor de cerurb-vue.foxinline.com.
- **Passo 4:** No Nginx, apontar **/** para o layout e **/estado/**, **/tipo-comodo/**, etc. para as pastas onde você publicou cada dist (conforme o **DEPLOY-CERURB-VUE.md**).

Se você disser em qual passo parou (por exemplo: “já fiz o build, não sei onde colocar os arquivos no servidor” ou “não sei editar o Nginx”), dá para detalhar só essa parte.
