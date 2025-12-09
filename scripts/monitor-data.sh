#!/bin/bash

# Script para monitorar dados dos bancos em tempo real (Linux/Mac)

while true; do
    clear
    echo -e "\033[1;36m================================================\033[0m"
    echo -e "\033[1;36m   MONITORAMENTO DE DADOS (POSTGRES, REDIS, MONGO)   \033[0m"
    echo -e "\033[0;37m   Atualizado em: $(date +'%H:%M:%S')   \033[0m"
    echo -e "\033[1;36m================================================\033[0m"
    echo ""

    # --- POSTGRESQL ---
    echo -e "\033[1;34m🐘 POSTGRESQL (Tabelas)\033[0m"
    echo -e "\033[1;34m-----------------------\033[0m"
    docker exec todolist-postgres psql -U todolist_user -d todolist -P pager=off -c "SELECT relname as Tabela, n_live_tup as Registros FROM pg_stat_user_tables ORDER BY relname;" 2>/dev/null || echo "Erro ao conectar no Postgres"
    echo ""

    # --- REDIS ---
    echo -e "\033[1;31m🔴 REDIS (Cache Keys)\033[0m"
    echo -e "\033[1;31m---------------------\033[0m"
    # Adicionada senha (-a redis_pass)
    KEYS=$(docker exec todolist-redis redis-cli -a redis_pass keys "*" 2>/dev/null | grep -v "Warning")
    if [ -z "$KEYS" ]; then
        echo "(Cache Vazio)"
    else
        echo "$KEYS" | head -n 10 | while read k; do
            TYPE=$(docker exec todolist-redis redis-cli -a redis_pass type "$k")
            echo "Key: $k [$TYPE]"
        done
        COUNT=$(echo "$KEYS" | wc -l)
        if [ "$COUNT" -gt 10 ]; then
            echo "... e mais $((COUNT-10)) chaves"
        fi
        echo "Total: $COUNT chaves"
    fi
    echo ""

    # --- MONGODB ---
    echo -e "\033[1;32m🍃 MONGODB (Logs)\033[0m"
    echo -e "\033[1;32m-----------------\033[0m"
    # Adicionada autenticação
    docker exec todolist-mongodb mongosh -u mongo_admin -p mongo_pass --authenticationDatabase admin todolist_logs --quiet --eval "db.getCollectionNames().forEach(c => { const count = db.getCollection(c).countDocuments(); print('Coleção: ' + c + ' | Docs: ' + count); });" 2>/dev/null || echo "Erro ao conectar no MongoDB"
    
    echo ""
    echo -e "\033[0;37m------------------------------------------------\033[0m"
    echo -e "\033[1;33mPressione Ctrl+C para sair. Atualizando em 5s...\033[0m"
    sleep 5
done
