# 🚀 Docker - Guia de Início Rápido

## ⚡ Início Rápido (3 passos)

### 1️⃣ Instalar Docker (se necessário)
```bash
bash install-docker.sh
# Depois: sair e entrar novamente na sessão
```

### 2️⃣ Iniciar Aplicação
```bash
./docker-dev.sh up
```

### 3️⃣ Verificar Status
```bash
./docker-dev.sh status
```

**Pronto! 🎉** A aplicação está rodando com PostgreSQL, Redis e MongoDB.

---

## 📚 Comandos Essenciais

```bash
./docker-dev.sh up       # Iniciar tudo
./docker-dev.sh down     # Parar tudo
./docker-dev.sh logs     # Ver logs
./docker-dev.sh status   # Ver status
./docker-dev.sh help     # Ver todos os comandos
```

---

## 🗄️ Acessar Bancos de Dados

```bash
# PostgreSQL
./docker-dev.sh db-shell

# MongoDB
./docker-dev.sh mongo-shell

# Redis
./docker-dev.sh redis-cli
```

---

## 📖 Documentação Completa

Consulte **[DOCKER.md](./DOCKER.md)** para:
- Guia detalhado de configuração
- Troubleshooting
- Comandos avançados
- Boas práticas de segurança
- Otimizações de performance

---

## 🏗️ Arquitetura

```
📦 todolist-app (Java 21)
  ↓
├─ 🗄️ PostgreSQL (Dados principais)
├─ 💾 Redis (Cache)
└─ 📝 MongoDB (Logs)
```

---

## 🐛 Problemas?

1. **Porta ocupada?** 
   - Mude a porta no `docker-compose.yml`: `"5433:5432"`

2. **Container não inicia?**
   - Veja os logs: `./docker-dev.sh logs-app`

3. **Docker não encontrado?**
   - Instale: `bash install-docker.sh`

4. **Permissão negada?**
   ```bash
   sudo usermod -aG docker $USER
   newgrp docker
   ```

---

## 📁 Arquivos Docker

| Arquivo | Descrição |
|---------|-----------|
| `Dockerfile` | Build da aplicação Java |
| `docker-compose.yml` | Orquestração de containers |
| `.dockerignore` | Arquivos ignorados no build |
| `docker-dev.sh` | Script de gerenciamento |
| `install-docker.sh` | Instalador do Docker |

---

## ✅ Checklist Pós-Instalação

- [ ] Docker instalado: `docker --version`
- [ ] Docker Compose instalado: `docker compose version`
- [ ] Usuário no grupo docker: `groups | grep docker`
- [ ] Containers iniciados: `./docker-dev.sh up`
- [ ] Status OK: `./docker-dev.sh status`

---

**💡 Dica**: Execute `./docker-dev.sh help` para ver todos os comandos disponíveis!
