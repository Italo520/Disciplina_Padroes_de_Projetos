# Script de teste para validar o MCP Redis
# Este script verifica se o servidor MCP do Redis está funcionando corretamente

Write-Host "====================================" -ForegroundColor Cyan
Write-Host "Teste do MCP Redis" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar se o Docker está rodando
Write-Host "[1/6] Verificando se o Docker Desktop está rodando..." -ForegroundColor Yellow
$dockerRunning = docker info 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ ERRO: Docker Desktop não está rodando!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Docker Desktop está rodando" -ForegroundColor Green
Write-Host ""

# 2. Verificar se o container Redis está ativo
Write-Host "[2/6] Verificando se o container Redis está ativo..." -ForegroundColor Yellow
$redisContainer = docker ps --filter "name=todolist-redis" --format "{{.Names}}"
if ([string]::IsNullOrEmpty($redisContainer)) {
    Write-Host "❌ ERRO: Container Redis não está rodando!" -ForegroundColor Red
    Write-Host "💡 Execute: docker-compose up -d redis" -ForegroundColor Cyan
    exit 1
}
Write-Host "✅ Container Redis está ativo: $redisContainer" -ForegroundColor Green
Write-Host ""

# 3. Verificar conectividade com o Redis
Write-Host "[3/6] Testando conectividade com o Redis..." -ForegroundColor Yellow
$dbTest = docker exec todolist-redis redis-cli -a redis_pass PING 2>&1 | Select-String "PONG"
if ([string]::IsNullOrEmpty($dbTest)) {
    Write-Host "❌ ERRO: Não foi possível conectar ao Redis!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Conexão com o Redis bem-sucedida (PONG recebido)" -ForegroundColor Green
Write-Host ""

# 4. Verificar versão do Redis
Write-Host "[4/6] Verificando versão do Redis..." -ForegroundColor Yellow
$version = docker exec todolist-redis redis-cli -a redis_pass INFO SERVER 2>&1 | Select-String "redis_version"
Write-Host $version -ForegroundColor White
Write-Host ""

# 5. Testar operações básicas
Write-Host "[5/6] Testando operações básicas (SET/GET)..." -ForegroundColor Yellow
$setResult = docker exec todolist-redis redis-cli -a redis_pass SET test_key "MCP_Test_Value" 2>&1 | Select-String "OK"
if ([string]::IsNullOrEmpty($setResult)) {
    Write-Host "❌ ERRO: Falha ao executar SET!" -ForegroundColor Red
} else {
    Write-Host "✅ SET executado com sucesso" -ForegroundColor Green
}

$getResult = docker exec todolist-redis redis-cli -a redis_pass GET test_key 2>&1
if ($getResult -match "MCP_Test_Value") {
    Write-Host "✅ GET executado com sucesso: $getResult" -ForegroundColor Green
} else {
    Write-Host "❌ ERRO: Falha ao executar GET!" -ForegroundColor Red
}

# Limpar teste
docker exec todolist-redis redis-cli -a redis_pass DEL test_key 2>&1 | Out-Null
Write-Host ""

# 6. Testar o servidor MCP Redis
Write-Host "[6/6] Testando o servidor MCP Redis..." -ForegroundColor Yellow
Write-Host "⏳ Iniciando servidor MCP (isso pode levar alguns segundos)..." -ForegroundColor Cyan

# Criar um processo em background para testar o MCP
$mcpProcess = Start-Process -FilePath "npx" -ArgumentList "-y", "@modelcontextprotocol/server-redis", "redis://:redis_pass@localhost:6379" -PassThru -NoNewWindow -RedirectStandardError "mcp_redis_error.log" -RedirectStandardOutput "mcp_redis_output.log"

Start-Sleep -Seconds 5

if ($mcpProcess.HasExited) {
    Write-Host "❌ ERRO: O servidor MCP não conseguiu iniciar!" -ForegroundColor Red
    if (Test-Path "mcp_redis_error.log") {
        Write-Host "Erro capturado:" -ForegroundColor Red
        Get-Content "mcp_redis_error.log" | Write-Host -ForegroundColor Red
    }
    exit 1
} else {
    Write-Host "✅ Servidor MCP Redis está rodando (PID: $($mcpProcess.Id))" -ForegroundColor Green
    Write-Host "⚠️  Encerrando processo de teste..." -ForegroundColor Yellow
    Stop-Process -Id $mcpProcess.Id -Force
}

# Limpar arquivos temporários
Remove-Item -Path "mcp_redis_error.log" -ErrorAction SilentlyContinue
Remove-Item -Path "mcp_redis_output.log" -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "====================================" -ForegroundColor Cyan
Write-Host "✅ TESTE CONCLUÍDO COM SUCESSO!" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Configuração do MCP Redis:" -ForegroundColor Cyan
Write-Host "   Arquivo: .vscode/mcp.json" -ForegroundColor White
Write-Host "   Servidor: redis" -ForegroundColor White
Write-Host "   Porta: 6379" -ForegroundColor White
Write-Host "   Pacote: @modelcontextprotocol/server-redis" -ForegroundColor White
Write-Host "   Autenticação: Habilitada (senha configurada)" -ForegroundColor White
Write-Host ""
Write-Host "📝 Próximos passos:" -ForegroundColor Cyan
Write-Host "   1. Reinicie o VS Code para carregar o servidor MCP" -ForegroundColor White
Write-Host "   2. O servidor MCP 'redis' estará disponível para uso" -ForegroundColor White
Write-Host "   3. Você poderá fazer operações Redis através do MCP" -ForegroundColor White
Write-Host ""
