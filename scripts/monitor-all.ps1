# Script para monitorar logs abrindo janelas dedicadas
# Isso garante que os logs sejam exibidos em tempo real sem problemas de buffer

Write-Host "Iniciando janelas de monitoramento..." -ForegroundColor Cyan

# 1. Janela para Logs da Aplicação
if (Test-Path "logs/application.log") {
    Write-Host "Abrindo log da aplicação..." -ForegroundColor Green
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "$host.UI.RawUI.WindowTitle = 'LOGS DA APLICAÇÃO'; Get-Content 'logs/application.log' -Wait -Tail 50"
} else {
    Write-Warning "Arquivo logs/application.log não encontrado. A aplicação já rodou?"
}

# 2. Janela para Logs do Docker
Write-Host "Abrindo logs do Docker..." -ForegroundColor Blue
Start-Process powershell -ArgumentList "-NoExit", "-Command", "$host.UI.RawUI.WindowTitle = 'LOGS DO DOCKER (Postgres, Redis, Mongo)'; docker compose logs -f --tail=50"

Write-Host "Monitoramento iniciado em janelas separadas." -ForegroundColor Yellow
