# Script para monitorar todos os logs (App + Bancos) de forma unificada
# Requer que a aplicação esteja configurada para escrever em logs/application.log

$ErrorActionPreference = "SilentlyContinue"
$logFile = "logs/application.log"

# Garante que o diretório e arquivo de log existam
if (!(Test-Path "logs")) { New-Item -ItemType Directory -Path "logs" | Out-Null }
if (!(Test-Path $logFile)) { New-Item -ItemType File -Path $logFile | Out-Null }

Clear-Host
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "   MONITORAMENTO UNIFICADO (APP + BANCOS DE DADOS)   " -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "Monitorando:" -ForegroundColor Gray
Write-Host "  - Aplicação Java (logs/application.log)" -ForegroundColor Green
Write-Host "  - Docker (Postgres, Redis, MongoDB)" -ForegroundColor Blue
Write-Host ""
Write-Host "Pressione Ctrl+C para parar." -ForegroundColor Yellow
Write-Host "------------------------------------------------------------" -ForegroundColor Gray

# Job para logs do Docker
$dockerJob = Start-Job -ScriptBlock {
    # Filtra logs para remover timestamps duplicados se possível, ou apenas raw
    docker compose logs -f --tail=20
}

# Job para logs da Aplicação
$appJob = Start-Job -ScriptBlock {
    param($path)
    Get-Content -Path $path -Wait -Tail 20
} -ArgumentList $logFile

try {
    while($true) {
        # Verifica e exibe logs do Docker
        $dLogs = Receive-Job -Job $dockerJob
        if ($dLogs) {
            foreach ($line in $dLogs) {
                if ([string]::IsNullOrWhiteSpace($line)) { continue }
                
                # Tenta colorir baseado no serviço
                if ($line -match "postgres") { Write-Host "[DOCKER] $line" -ForegroundColor Blue }
                elseif ($line -match "redis") { Write-Host "[DOCKER] $line" -ForegroundColor Red }
                elseif ($line -match "mongo") { Write-Host "[DOCKER] $line" -ForegroundColor Magenta }
                else { Write-Host "[DOCKER] $line" -ForegroundColor Cyan }
            }
        }

        # Verifica e exibe logs da App
        $aLogs = Receive-Job -Job $appJob
        if ($aLogs) {
            foreach ($line in $aLogs) {
                if ([string]::IsNullOrWhiteSpace($line)) { continue }
                Write-Host "[APP]    $line" -ForegroundColor Green
            }
        }

        Start-Sleep -Milliseconds 500
    }
} finally {
    Write-Host "`nParando monitoramento..." -ForegroundColor Yellow
    Stop-Job $dockerJob
    Stop-Job $appJob
    Remove-Job $dockerJob
    Remove-Job $appJob
}
