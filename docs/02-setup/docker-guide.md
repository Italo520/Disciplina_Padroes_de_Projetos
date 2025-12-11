# 🐳 Docker - Guia Completo

Este guia consolida todas as informações necessárias para rodar o projeto usando Docker.

## 🚀 Início Rápido (Quickstart)

### 1. Instalar Docker (se necessário)
```bash
./scripts/install-docker.sh
# Depois: sair e entrar novamente na sessão para atualizar grupos
```

### 2. Iniciar Aplicação
Use o script auxiliar para subir todo o ambiente:

**Windows:**
```powershell
.\scripts\run-app.ps1
```

**Linux/Mac:**
```bash
./scripts/run-app.sh
```

### 3. Verificar Status
```bash
docker compose ps
```

**Pronto!** A aplicação está rodando com PostgreSQL, Redis e MongoDB.

---

## 🏗️ Estrutura do Docker Compose

O arquivo `docker-compose.yml` orquestra os serviços de banco de dados:

| Serviço | Imagem | Porta | Descrição |
|---------|--------|-------|-----------|
| **postgres** | postgres:16-alpine | 5432 | Banco de dados relacional |
| **redis** | redis:7-alpine | 6379 | Cache de dados |
| **mongodb** | mongo:7-jammy | 27017 | Logs de auditoria |

> A aplicação Java roda localmente (host) e se conecta a esses serviços.

### Diagrama de Rede
```mermaid
graph TD
    subgraph Docker Network
        PG[PostgreSQL]
        Redis[Redis]
        Mongo[MongoDB]
    end
    App[App Java (Host)] --> PG
    App --> Redis
    App --> Mongo
```

---

## 🔧 Configuração e Inicialização do Banco

### Inicialização Automática (`init.sql`)
O container do PostgreSQL é configurado para executar automaticamente o script `init.sql` na primeira vez que o volume de dados é criado. Isso cria as tabelas e esquemas necessários.

### Volumes Persistentes
Os dados são persistidos em volumes Docker para não serem perdidos ao reiniciar os containers:
- `postgres-data`
- `mongodb-data`
- `redis-data`

---

## 🛠️ Comandos Úteis

Você pode gerenciar os containers diretamente com Docker Compose:

```bash
docker compose up -d        # Inicia bancos de dados
docker compose down         # Para e remove containers
docker compose logs -f      # Acompanha logs
```

## 🐛 Troubleshooting

**Porta ocupada?**
Se a porta 5432 estiver em uso, pare o serviço local do Postgres ou altere o mapeamento no `docker-compose.yml`.

**Container não inicia?**
Verifique os logs: `./scripts/docker-dev.sh logs`
