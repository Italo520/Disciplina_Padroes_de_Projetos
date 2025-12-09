#!/bin/bash

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}[INFO] Verificando ambiente...${NC}"

# Verifica se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}[ERRO] Docker não está rodando. Por favor, inicie o Docker primeiro.${NC}"
    exit 1
fi

echo -e "${YELLOW}[INFO] Iniciando serviços de banco de dados...${NC}"

# Sobe apenas os serviços de infraestrutura (bancos de dados)
# Não sobe o serviço 'app' pois vamos rodar localmente
docker compose up -d postgres redis mongodb

if [ $? -ne 0 ]; then
    echo -e "${RED}[ERRO] Falha ao iniciar containers.${NC}"
    exit 1
fi

echo -e "${YELLOW}[INFO] Aguardando bancos de dados inicializarem...${NC}"
sleep 5

# Compila a aplicação
echo -e "${YELLOW}[INFO] Compilando a aplicação Java...${NC}"
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo -e "${RED}[ERRO] Falha na compilação.${NC}"
    exit 1
fi

# Define variáveis de ambiente para conectar aos serviços no localhost
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=todolist
export DB_USER=todolist_user
export DB_PASSWORD=todolist_pass

export REDIS_HOST=localhost
export REDIS_PORT=6379

export MONGO_HOST=localhost
export MONGO_PORT=27017
export MONGO_DATABASE=todolist_logs

echo -e "${GREEN}[SUCESSO] Ambiente pronto! Iniciando aplicação...${NC}"
echo -e "${YELLOW}[INFO] Pressione Ctrl+C para parar a aplicação.${NC}"

# Executa a aplicação
java -jar target/projeto_to_do_list_java-2.0-jar-with-dependencies.jar
