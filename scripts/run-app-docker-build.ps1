# Script para executar a aplicação TodoList usando Docker para compilação
# Não requer Maven instalado localmente

$ErrorActionPreference = "Stop"

function Write-Info {
    param([string]$Message)
    Write-Host "[INFO] $Message" -ForegroundColor Yellow
}

function Write-Success {
    param([string]$Message)
    Write-Host "[SUCESSO] $Message" -ForegroundColor Green
}

function Write-Error {
    param([string]$Message)
    Write-Host "[ERRO] $Message" -ForegroundColor Red
}

Write-Info "Verificando ambiente..."

# Verifica se o Docker está rodando
try {
    docker info | Out-Null
} catch {
    Write-Error "Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
}

Write-Info "Iniciando serviços de banco de dados..."

# Sobe apenas os serviços de infraestrutura
docker compose up -d postgres redis mongodb --remove-orphans

if ($LASTEXITCODE -ne 0) {
    Write-Error "Falha ao iniciar containers."
    exit 1
}

Write-Info "Aguardando bancos de dados inicializarem..."
Start-Sleep -Seconds 5

# Compila a aplicação usando Docker (sem precisar de Maven local)
Write-Info "Compilando a aplicação Java usando Docker..."
docker run --rm -v "${PWD}:/app" -w /app maven:3.9-eclipse-temurin-17 mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Error "Falha na compilação."
    exit 1
}

# Define variáveis de ambiente
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "todolist"
$env:DB_USER = "todolist_user"
$env:DB_PASSWORD = "todolist_pass"

$env:REDIS_HOST = "localhost"
$env:REDIS_PORT = "6379"

$env:MONGO_HOST = "localhost"
$env:MONGO_PORT = "27017"
$env:MONGO_DATABASE = "todolist_logs"

Write-Success "Ambiente pronto! Iniciando aplicação..."
Write-Info "Pressione Ctrl+C para parar a aplicação."

# Executa a aplicação
java -jar target\projeto_to_do_list_java-2.0-jar-with-dependencies.jar
