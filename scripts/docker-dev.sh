#!/bin/bash

# Script de desenvolvimento para gerenciar containers Docker
# Uso: ./docker-dev.sh [comando]

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

PROJECT_NAME="todolist"

# Função para exibir mensagens coloridas
info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Função para mostrar ajuda
show_help() {
    echo "======================================"
    echo "  ToDoList - Docker Management Script"
    echo "======================================"
    echo ""
    echo "Uso: ./docker-dev.sh [comando]"
    echo ""
    echo "Comandos disponíveis:"
    echo "  up          - Inicia todos os containers"
    echo "  down        - Para todos os containers"
    echo "  restart     - Reinicia todos os containers"
    echo "  logs        - Mostra logs de todos os containers"
    echo "  logs-app    - Mostra logs apenas da aplicação"
    echo "  logs-db     - Mostra logs apenas do PostgreSQL"
    echo "  build       - Reconstrói a imagem da aplicação"
    echo "  rebuild     - Reconstrói e inicia os containers"
    echo "  clean       - Remove containers, imagens e volumes (CUIDADO!)"
    echo "  status      - Mostra status dos containers"
    echo "  db-shell    - Abre shell do PostgreSQL"
    echo "  mongo-shell - Abre shell do MongoDB"
    echo "  redis-cli   - Abre CLI do Redis"
    echo "  help        - Mostra esta ajuda"
    echo ""
}

# Verifica se Docker está instalado
check_docker() {
    if ! command -v docker &> /dev/null; then
        error "Docker não está instalado!"
        echo "Instale o Docker: https://docs.docker.com/get-docker/"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        error "Docker Compose não está instalado!"
        echo "Instale o Docker Compose: https://docs.docker.com/compose/install/"
        exit 1
    fi
}

# Inicia os containers
start_containers() {
    info "Iniciando containers..."
    docker-compose up -d
    success "Containers iniciados!"
    echo ""
    docker-compose ps
}

# Para os containers
stop_containers() {
    info "Parando containers..."
    docker-compose down
    success "Containers parados!"
}

# Reinicia os containers
restart_containers() {
    info "Reiniciando containers..."
    docker-compose restart
    success "Containers reiniciados!"
}

# Mostra logs
show_logs() {
    info "Mostrando logs..."
    docker-compose logs -f --tail=100
}

# Mostra logs da aplicação
show_app_logs() {
    info "Mostrando logs da aplicação..."
    docker-compose logs -f --tail=100 app
}

# Mostra logs do banco
show_db_logs() {
    info "Mostrando logs do PostgreSQL..."
    docker-compose logs -f --tail=100 postgres
}

# Reconstrói a imagem
build_image() {
    info "Reconstruindo imagem da aplicação..."
    docker-compose build --no-cache app
    success "Imagem reconstruída!"
}

# Reconstrói e inicia
rebuild_and_start() {
    info "Reconstruindo e iniciando..."
    docker-compose up -d --build
    success "Containers reconstruídos e iniciados!"
    echo ""
    docker-compose ps
}

# Limpeza completa
clean_all() {
    warning "ATENÇÃO: Esta operação irá remover TODOS os containers, imagens e volumes!"
    read -p "Tem certeza? (digite 'yes' para confirmar): " confirm
    
    if [ "$confirm" = "yes" ]; then
        info "Removendo containers..."
        docker-compose down -v --rmi all
        success "Limpeza completa realizada!"
    else
        info "Operação cancelada."
    fi
}

# Mostra status dos containers
show_status() {
    info "Status dos containers:"
    echo ""
    docker-compose ps
    echo ""
    info "Uso de recursos:"
    docker stats --no-stream $(docker-compose ps -q)
}

# Abre shell do PostgreSQL
open_db_shell() {
    info "Abrindo shell do PostgreSQL..."
    docker-compose exec postgres psql -U todolist_user -d todolist
}

# Abre shell do MongoDB
open_mongo_shell() {
    info "Abrindo shell do MongoDB..."
    docker-compose exec mongodb mongosh -u mongo_admin -p mongo_pass
}

# Abre CLI do Redis
open_redis_cli() {
    info "Abrindo CLI do Redis..."
    docker-compose exec redis redis-cli -a redis_pass
}

# Processa comando
check_docker

case "${1:-help}" in
    up)
        start_containers
        ;;
    down)
        stop_containers
        ;;
    restart)
        restart_containers
        ;;
    logs)
        show_logs
        ;;
    logs-app)
        show_app_logs
        ;;
    logs-db)
        show_db_logs
        ;;
    build)
        build_image
        ;;
    rebuild)
        rebuild_and_start
        ;;
    clean)
        clean_all
        ;;
    status)
        show_status
        ;;
    db-shell)
        open_db_shell
        ;;
    mongo-shell)
        open_mongo_shell
        ;;
    redis-cli)
        open_redis_cli
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        error "Comando inválido: $1"
        echo ""
        show_help
        exit 1
        ;;
esac
