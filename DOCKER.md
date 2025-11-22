# 🐳 Docker - Guia Completo de Implantação

## 📋 Índice
1. [Visão Geral](#visão-geral)
2. [Pré-requisitos](#pré-requisitos)
3. [Arquitetura](#arquitetura)
4. [Início Rápido](#início-rápido)
5. [Configuração Detalhada](#configuração-detalhada)
6. [Comandos Úteis](#comandos-úteis)
7. [Troubleshooting](#troubleshooting)
8. [Segurança](#segurança)
9. [Performance](#performance)

---

## 📊 Visão Geral

Este projeto foi containerizado usando **Docker** e **Docker Compose**, permitindo:

✅ **Ambientes isolados** - Aplicação e dependências em containers  
✅ **Reprodutibilidade** - Mesmo ambiente em dev, test e prod  
✅ **Escalabilidade** - Fácil adicionar/remover recursos  
✅ **Portabilidade** - Roda em qualquer sistema com Docker  

### Containers da Aplicação:

| Container | Imagem | Porta | Descrição |
|-----------|--------|-------|-----------|
| **app** | Custom (Java 21) | - | Aplicação ToDoList |
| **postgres** | postgres:16-alpine | 5432 | Banco de dados principal |
| **redis** | redis:7-alpine | 6379 | Cache |
| **mongodb** | mongo:7-jammy | 27017 | Logs de auditoria |

---

## ⚙️ Pré-requisitos

### 1. Instalar Docker

#### Linux (Ubuntu/Debian):
```bash
# Atualizar repositórios
sudo apt update

# Instalar Docker
sudo apt install docker.io docker-compose -y

# Adicionar usuário ao grupo docker (para rodar sem sudo)
sudo usermod -aG docker $USER
newgrp docker

# Verificar instalação
docker --version
docker-compose --version
```

#### Windows/Mac:
- Baixe e instale o [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### 2. Verificar Instalação

```bash
docker --version
# Deve retornar: Docker version 24.x.x ou superior

docker-compose --version
# Deve retornar: docker-compose version 1.29.x ou superior
```

---

## 🏗️ Arquitetura

### Diagrama de Containers:

```
┌─────────────────────────────────────────────────────┐
│                  todolist-network                   │
│                   (Docker Network)                  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────┐    ┌───────────────────────┐    │
│  │   app        │───▶│   postgres            │    │
│  │ (Java 21)    │    │  (PostgreSQL 16)      │    │
│  │              │    │  Port: 5432           │    │
│  └───────┬──────┘    └───────────────────────┘    │
│          │                                         │
│          │           ┌───────────────────────┐    │
│          ├──────────▶│   redis               │    │
│          │           │  (Redis 7)            │    │
│          │           │  Port: 6379           │    │
│          │           └───────────────────────┘    │
│          │                                         │
│          │           ┌───────────────────────┐    │
│          └──────────▶│   mongodb             │    │
│                      │  (MongoDB 7)          │    │
│                      │  Port: 27017          │    │
│                      └───────────────────────┘    │
│                                                     │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│               Volumes Persistentes                  │
├─────────────────────────────────────────────────────┤
│  • postgres-data  → /var/lib/postgresql/data       │
│  • mongodb-data   → /data/db                        │
│  • redis-data     → /data                           │
└─────────────────────────────────────────────────────┘
```

### Características do Dockerfile:

- ✅ **Multi-stage build** - Imagem final ~200MB (otimizada)
- ✅ **Non-root user** - Segurança aprimorada
- ✅ **Healthcheck** - Monitoramento automático
- ✅ **JVM otimizado** - GC G1 + Container support

---

## 🚀 Início Rápido

### Opção 1: Usando Script Automático (Recomendado)

```bash
# Ver comandos disponíveis
./docker-dev.sh help

# Iniciar todos os containers
./docker-dev.sh up

# Ver logs
./docker-dev.sh logs

# Ver status
./docker-dev.sh status
```

### Opção 2: Comandos Docker Compose Manuais

```bash
# 1. Construir e iniciar containers
docker-compose up -d --build

# 2. Verificar status
docker-compose ps

# 3. Ver logs
docker-compose logs -f

# 4. Parar containers
docker-compose down
```

### ⏱️ Tempo de Inicialização

- **Primeira vez** (build): ~5-10 minutos
- **Depois (com cache)**: ~30-60 segundos

---

## 🔧 Configuração Detalhada

### 1. Configurar Variáveis de Ambiente (Opcional)

Crie um arquivo `.env` na raiz do projeto:

```bash
# .env
POSTGRES_DB=todolist
POSTGRES_USER=todolist_user
POSTGRES_PASSWORD=minha_senha_segura

REDIS_PASSWORD=redis_senha_segura

MONGO_ROOT_USER=mongo_admin
MONGO_ROOT_PASSWORD=mongo_senha_segura
```

### 2. Configurar database.properties

Copie o exemplo e edite conforme necessário:

```bash
cp database.properties.example src/main/resources/database.properties
```

O arquivo já suporta variáveis de ambiente do Docker!

### 3. Personalizar docker-compose.yml

Você pode editar o `docker-compose.yml` para:
- Alterar portas expostas
- Adicionar mais recursos (memória, CPU)
- Configurar networks customizadas

---

## 📖 Comandos Úteis

### Script docker-dev.sh:

```bash
./docker-dev.sh up          # Inicia containers
./docker-dev.sh down        # Para containers
./docker-dev.sh restart     # Reinicia containers
./docker-dev.sh logs        # Mostra logs de todos
./docker-dev.sh logs-app    # Logs apenas da app
./docker-dev.sh logs-db     # Logs apenas do PostgreSQL
./docker-dev.sh build       # Reconstrói imagem da app
./docker-dev.sh rebuild     # Reconstrói e reinicia
./docker-dev.sh status      # Status e uso de recursos
./docker-dev.sh db-shell    # Acessa PostgreSQL CLI
./docker-dev.sh mongo-shell # Acessa MongoDB CLI
./docker-dev.sh redis-cli   # Acessa Redis CLI
./docker-dev.sh clean       # Remove tudo (CUIDADO!)
```

### Comandos Docker Compose Diretos:

```bash
# Iniciar em background
docker-compose up -d

# Ver logs específicos
docker-compose logs -f app
docker-compose logs -f postgres

# Executar comandos no container
docker-compose exec app java -version
docker-compose exec postgres psql -U todolist_user -d todolist

# Parar um container específico
docker-compose stop app

# Reconstruir apenas um serviço
docker-compose up -d --build app

# Ver uso de recursos
docker stats

# Inspecionar volumes
docker volume ls
docker volume inspect docs-javadoc-update_postgres-data
```

### Acessar Bancos de Dados:

```bash
# PostgreSQL
docker-compose exec postgres psql -U todolist_user -d todolist

# Dentro do psql:
\dt              # Listar tabelas
\d usuarios      # Descrever tabela usuarios
SELECT * FROM usuarios;
\q               # Sair

# MongoDB
docker-compose exec mongodb mongosh -u mongo_admin -p mongo_pass

# Dentro do mongosh:
show dbs
use todolist_logs
show collections
db.logs.find()
exit

# Redis
docker-compose exec redis redis-cli -a redis_pass

# Dentro do redis-cli:
KEYS *
GET chave
exit
```

---

## 🐛 Troubleshooting

### Problema 1: "Port is already allocated"

```bash
# Verificar o que está usando a porta
sudo lsof -i :5432

# Parar o serviço conflitante ou mudar a porta no docker-compose.yml
# Exemplo: "5433:5432"
```

### Problema 2: Container não inicia

```bash
# Ver logs detalhados
docker-compose logs app

# Ver últimas 100 linhas
docker-compose logs --tail=100 app

# Verificar healthcheck
docker inspect todolist-app | grep Health -A 10
```

### Problema 3: "No space left on device"

```bash
# Limpar imagens não utilizadas
docker system prune -a

# Limpar volumes não utilizados (CUIDADO!)
docker volume prune
```

### Problema 4: Permissões no Linux

```bash
# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER
newgrp docker

# Reiniciar serviço docker
sudo systemctl restart docker
```

### Problema 5: Build muito lento

```bash
# Usar BuildKit (mais rápido)
DOCKER_BUILDKIT=1 docker-compose build

# Limpar cache de build
docker builder prune
```

---

## 🔒 Segurança

### Boas Práticas Implementadas:

✅ **Non-root user** - App roda com usuário não-privilegiado  
✅ **Network isolation** - Containers em rede privada  
✅ **Secrets** - Senhas via variáveis de ambiente  
✅ **Read-only mounts** - `init.sql` montado como read-only  
✅ **Resource limits** - Limites de memória configuráveis  

### Recomendações para Produção:

1. **Nunca commite senhas** no docker-compose.yml
2. **Use Docker Secrets** ou vault para credenciais
3. **Habilite TLS/SSL** para conexões externas
4. **Atualize imagens** regularmente
5. **Monitore logs** de segurança

```bash
# Exemplo usando .env (não commitar!)
docker-compose --env-file .env.production up -d
```

---

## ⚡ Performance

### Otimizações Aplicadas:

#### 1. Multi-stage Build
- **Build stage**: ~800MB
- **Runtime stage**: ~200MB
- **Redução**: 75%

#### 2. JVM Tuning
```dockerfile
-XX:+UseContainerSupport    # Detecta limites do container
-XX:MaxRAMPercentage=75.0   # Usa 75% da RAM disponível
-XX:+UseG1GC                # Garbage Collector otimizado
```

#### 3. Layer Caching
```dockerfile
COPY pom.xml .              # Primeiro: dependências (cache)
RUN mvn dependency:go-offline
COPY src ./src              # Depois: código fonte
RUN mvn package
```

### Monitoramento:

```bash
# Ver uso de CPU/Memória em tempo real
docker stats

# Limitar recursos no docker-compose.yml:
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          memory: 512M
```

---

## 📊 Backup e Restore

### Backup do PostgreSQL:

```bash
# Criar backup
docker-compose exec -T postgres pg_dump -U todolist_user todolist > backup_$(date +%Y%m%d_%H%M%S).sql

# Restaurar backup
cat backup_20250122_153000.sql | docker-compose exec -T postgres psql -U todolist_user -d todolist
```

### Backup de Volumes:

```bash
# Backup de todos os volumes
docker run --rm \
  -v docs-javadoc-update_postgres-data:/data \
  -v $(pwd):/backup \
  alpine tar czf /backup/postgres-backup.tar.gz -C /data .
```

---

## 🎯 Próximos Passos

1. **CI/CD**: Integrar com GitHub Actions
2. **Docker Registry**: Publicar imagens
3. **Kubernetes**: Orquestração avançada
4. **Monitoring**: Prometheus + Grafana

---

## 📚 Recursos Adicionais

- [Docker Docs](https://docs.docker.com/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Multi-stage Builds](https://docs.docker.com/build/building/multi-stage/)

---

## 🆘 Suporte

Problemas ou dúvidas?
1. Verifique a seção [Troubleshooting](#troubleshooting)
2. Consulte os logs: `./docker-dev.sh logs`
3. Verifique o status: `./docker-dev.sh status`

---

**Desenvolvido com ❤️ usando Docker + Java 21**
