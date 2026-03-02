# Cerurb Vue + WAR no mesmo servidor – como funciona e por que o WAR pode não subir

## 1. O que é o quê

| URL / Serviço | O que é | Onde roda | Porta |
|---------------|--------|-----------|--------|
| **https://cerurb-teste.foxinline.com/CerurbPro/** | Aplicação **Cerurb (WAR)** – Java, JSF, Payara | Payara no servidor | **8080** (ou a que o Payara usar) |
| **https://cerurb-vue.foxinline.com/** | Front dos **microserviços Vue** – layout + módulos (Module Federation) | Container **vue-layout** (e outros containers dos remotes) | **5173** (layout); remotes em 4173, 4175, 4177, 4179, 4181, 4183, 4185, 4187, 4191, etc. |

- O **WAR** = aplicação principal do Cerurb (telas JSF, configuração, etc.). Sobe no **Payara**.
- O **cerurb-vue** = conjunto de containers (layout + um container por microserviço). O **layout** na porta **5173** é a “porta de entrada” única para o usuário; os outros números (4173, 4175, …) são usados **internamente** pelo Module Federation para carregar cada módulo.

---

## 2. Como o “location” (URL) funciona

- O usuário acessa **só** `https://cerurb-vue.foxinline.com/`.
- No servidor, o **Nginx** (ou outro proxy) faz com que esse host/domínio aponte para a **porta 5173** (container do **vue-layout**).
- O layout (Vite + Module Federation) sabe onde está cada microserviço (por path ou por URL interna). Exemplo:
  - `/estado` → layout carrega o remote do estado (ex.: `http://localhost:4191` ou o host do container no Docker).
  - `/tipo-comodo` → outro remote, outra porta, etc.

Ou seja: **não** é preciso expor todas as portas (4173, 4175, …) na internet. Só a **5173** (layout) precisa estar atrás do `cerurb-vue.foxinline.com`. As outras portas são acessadas pelo **próprio layout** (navegador ou servidor), dentro da rede do servidor/Docker.

---

## 3. Por que a aplicação em WAR “não sobe” quando o Vue está no ar

Algumas causas comuns quando o Cerurb (WAR) para de subir ou cai ao subir os microserviços:

1. **Conflito de porta**  
   Se o Payara estiver configurado para usar a porta **8080** e algum outro processo (outro container ou o próprio host) já estiver usando 8080, o Payara não sobe. Os containers Vue usam outras portas (5173, 417x, …), então o conflito costuma ser com outro serviço na 8080, não com o Vue.

2. **Memória / CPU**  
   Payara + vários containers (layout + remotes) no mesmo servidor podem estourar memória. O Payara pode falhar ao iniciar ou ser morto pelo OOM killer.

3. **Ordem de subida**  
   Se o `docker compose` ou o sistema sobe primeiro os containers Vue e algo na configuração do host (proxy, firewall, ou outro serviço) depender da porta 8080, o Payara pode não conseguir bindar na 8080.

4. **Erro no deploy do WAR**  
   O Payara pode estar subindo, mas o **WAR não deployar** (erro de aplicação, exceção na inicialização, etc.). Isso não é “conflito de porta”, e sim erro no código ou na configuração do WAR.

Para saber o que está acontecendo, é essencial olhar o **log do Payara**.

---

## 4. Como ver o erro do Payara (por que o WAR não sobe)

Como o Felipe disse: o log fica no diretório do Payara, em **glassfish/domains/logs**.

### 4.1. Caminho no seu servidor

No seu ambiente o diretório é:

```text
/opt/Payara_Server/glassfish/domains/domain1/logs
```

Arquivos que importam:

| Arquivo | O que é |
|---------|--------|
| **server.log** | Log **atual** do Payara (é onde o servidor está escrevendo agora). É o primeiro a olhar. |
| **server.log_2026-02-25T...** | Logs **rotacionados** (cada reinício/rotação gera um novo). Use quando precisar ver o que aconteceu em um restart específico. |
| **server.log.lck** | Arquivo de lock (indica que o Payara está usando o server.log). Pode ignorar. |

### 4.2. Comandos para achar o erro

No servidor:

```bash
cd /opt/Payara_Server/glassfish/domains/domain1/logs
```

**Ver as últimas linhas do log atual (erro recente):**

```bash
tail -n 500 server.log
```

**Acompanhar em tempo real** (enquanto sobe o Payara ou faz deploy do WAR):

```bash
tail -f server.log
```

**Procurar por erros e exceções no log atual:**

```bash
grep -i -E "Exception|Error|Failed|Deployment failed|Address already in use" server.log | tail -n 100
```

**Procurar pelo nome do WAR (ex.: CerurbPro):**

```bash
grep -i "CerurbPro\|\.war" server.log | tail -n 50
```

**Se o problema foi em um restart anterior**, use o arquivo rotacionado da data/hora em que tentou subir (ex.: 26 fev 16:06):

```bash
grep -i -E "Exception|Error|Failed|Address already in use" server.log_2026-02-26T16-06-07 | tail -n 80
```

### 4.3. O que procurar no log

- **Exception**, **Error**, **Failed**, **Deployment failed** → erro na aplicação ou no deploy.
- **Address already in use** → outra coisa está usando a porta do Payara (ex.: 8080).
- Nome do seu WAR (ex.: **CerurbPro**) e mensagens de deploy ou de falha ao carregar a aplicação.

Se o WAR não sobe **só quando os containers Vue estão rodando**, compare:

1. Log do Payara com os containers Vue **ligados** (WAR não sobe).
2. Log do Payara com os containers Vue **parados** (WAR sobe?).

Se aparecer `Address already in use` na 8080 com os Vue ligados, algum outro processo está usando a 8080. Se não aparecer nada de porta, o problema provavelmente é memória, ordem de subida ou erro na aplicação (WAR).

---

## 5. Resumo rápido

| Dúvida | Resposta |
|--------|----------|
| O que é “aplicação em WAR”? | É o Cerurb Java (JSF) que roda no Payara (porta 8080), em cerurb-teste.foxinline.com/CerurbPro/. |
| cerurb-vue precisa apontar para todas as portas? | Não. Só a porta do **layout (5173)** precisa ser exposta no Nginx para cerurb-vue.foxinline.com. As outras (4173, 4175, …) são usadas internamente pelo Module Federation. |
| Por que o WAR não sobe quando o Vue sobe? | Pode ser conflito de porta (ex.: 8080), falta de memória, ordem de subida ou erro no deploy do WAR. |
| Como ver o erro? | Log do Payara: `glassfish/domains/logs/server.log` (ex.: `tail -f server.log` ao subir o Payara ou fazer deploy). |

Se você colar aqui um trecho do `server.log` (principalmente as linhas com Exception/Error no momento em que tenta subir o WAR), dá para apontar a causa com mais precisão.
