# 🛠️ Guia Rápido de Comandos e Operações

Este documento serve como um guia de referência rápida para os comandos essenciais do projeto **To-Do List**, cobrindo Docker, Banco de Dados, SonarQube e execução da aplicação.

---

## 🚀 Scripts de Automação

O projeto conta com scripts na pasta `scripts/` para facilitar o dia a dia:

### Iniciar Aplicação Completa (Recomendado)
Sobe os containers Docker (bancos de dados) e inicia a aplicação Java Swing.
```bash
./scripts/run-app.sh
```

### Gerenciar Apenas o Docker
Se você quiser apenas subir os bancos de dados sem rodar o app Java:
```bash
./scripts/docker-dev.sh start   # Inicia os containers
./scripts/docker-dev.sh stop    # Para os containers
./scripts/docker-dev.sh status  # Verifica o status
```

---

## 🐳 Docker e Containers

Comandos úteis para gerenciar a infraestrutura local.

| Ação | Comando |
|------|---------|
| **Listar Containers Ativos** | `docker ps` |
| **Ver Logs de um Container** | `docker logs -f <nome_do_container>` |
| **Parar e Remover Tudo** | `docker-compose down` |
| **Reiniciar Containers** | `docker-compose restart` |

### Nomes dos Containers
- `todolist-app` (Aplicação Java - se rodando via Docker)
- `todolist-postgres` (PostgreSQL)
- `todolist-redis` (Redis)
- `todolist-mongodb` (MongoDB)


---

## 🗄️ Acesso aos Bancos de Dados

Como acessar os consoles interativos dos bancos de dados rodando no Docker.

### PostgreSQL (Dados Relacionais)
Acessar o terminal SQL (`psql`) com o usuário e banco corretos:
```bash
docker exec -it todolist-postgres psql -U todolist_user -d todolist
```
*Comandos úteis no psql:*
- `\dt`: Listar tabelas
- `select * from tarefas;`: Ver tarefas
- `\q`: Sair

### Redis (Cache)
Acessar a CLI do Redis (com senha):
```bash
docker exec -it todolist-redis redis-cli -a redis_pass
```
*Comandos úteis no Redis:*
- `KEYS *`: Listar todas as chaves
- `GET <chave>`: Ver valor de uma chave
- `FLUSHALL`: Limpar todo o cache
- `EXIT`: Sair

### MongoDB (Logs e Auditoria)
Acessar o shell do Mongo (`mongosh`) com autenticação:
```bash
docker exec -it todolist-mongodb mongosh -u mongo_admin -p mongo_pass --authenticationDatabase admin
```
*Comandos úteis no Mongo:*
- `show dbs`: Listar bancos
- `use todolist_logs`: Selecionar banco de logs (se existir)
- `show collections`: Listar coleções
- `db.logs.find()`: Listar logs
- `exit`: Sair

---



## ☕ Maven e Java

Comandos para build e execução manual sem o script.

| Ação | Comando |
|------|---------|
| **Limpar e Compilar** | `mvn clean package` |
| **Rodar Testes Unitários** | `mvn test` |
| **Rodar Aplicação (JAR)** | `java -jar target/projeto_to_do_list_java-2.0-jar-with-dependencies.jar` |

---

## 📝 Logs e Debug

### Ver Logs da Aplicação
A aplicação exibe logs no terminal onde foi iniciada.
- **SQL Formatado**: As queries do Hibernate aparecem no console.
- **Parâmetros SQL**: Configuramos o `log4j2.xml` para mostrar os valores (`?`) das queries.

### Arquivo de Configuração de Log
Para ajustar o nível de detalhe (ex: esconder SQL), edite:
`src/main/resources/log4j2.xml`
