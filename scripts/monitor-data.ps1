# Script para monitorar dados dos bancos em tempo real
# Exibe tabelas do Postgres, chaves do Redis e coleções do Mongo

$ErrorActionPreference = "SilentlyContinue"

while($true) {
    Clear-Host
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "   MONITORAMENTO DE DADOS (POSTGRES, REDIS, MONGO)   " -ForegroundColor Cyan
    Write-Host "   Atualizado em: $(Get-Date -Format 'HH:mm:ss')   " -ForegroundColor Gray
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host ""

    # --- POSTGRESQL ---
    Write-Host "🐘 POSTGRESQL (Tabelas)" -ForegroundColor Blue
    Write-Host "-----------------------" -ForegroundColor Blue
    try {
        # Usa pg_stat_user_tables para obter contagens estimadas (rápido)
        docker exec todolist-postgres psql -U todolist_user -d todolist -P pager=off -c "SELECT relname as Tabela, n_live_tup as Registros FROM pg_stat_user_tables ORDER BY relname;" 2>$null | Out-String | Write-Host
    } catch {
        Write-Host "Erro ao conectar no Postgres (Container rodando?)" -ForegroundColor Red
    }

    # --- REDIS ---
    Write-Host "🔴 REDIS (Cache Keys)" -ForegroundColor Red
    Write-Host "---------------------" -ForegroundColor Red
    try {
        # Adicionada senha (-a redis_pass)
        $keys = docker exec todolist-redis redis-cli -a redis_pass keys "*" 2>$null
        if ($keys -and $keys -notmatch "Warning") {
            # Limita a 10 chaves para não poluir
            $count = 0
            foreach ($k in $keys) {
                if ($k -match "Warning") { continue }
                if ($count -ge 10) { 
                    Write-Host "... e mais" 
                    break 
                }
                # Pega o tipo
                $type = docker exec todolist-redis redis-cli -a redis_pass type $k
                Write-Host "Key: $k [$type]"
                $count++
            }
            Write-Host "Total: $($keys.Count) chaves"
        } else {
            Write-Host "(Cache Vazio ou Erro)"
        }
    } catch {
        Write-Host "Erro ao conectar no Redis" -ForegroundColor Red
    }
    Write-Host ""

    # --- MONGODB ---
    Write-Host "🍃 MONGODB (Logs)" -ForegroundColor Green
    Write-Host "-----------------" -ForegroundColor Green
    try {
        # Adicionada autenticação (-u mongo_admin -p mongo_pass --authenticationDatabase admin)
        docker exec todolist-mongodb mongosh -u mongo_admin -p mongo_pass --authenticationDatabase admin todolist_logs --quiet --eval "db.getCollectionNames().forEach(c => { const count = db.getCollection(c).countDocuments(); print('Coleção: ' + c + ' | Docs: ' + count); });" 2>$null | Out-String | Write-Host
    } catch {
        Write-Host "Erro ao conectar no MongoDB" -ForegroundColor Red
    }
    
    Write-Host ""
    Write-Host "------------------------------------------------" -ForegroundColor Gray
    Write-Host "Pressione Ctrl+C para sair. Atualizando em 5s..." -ForegroundColor Yellow
    Start-Sleep -Seconds 5
}
