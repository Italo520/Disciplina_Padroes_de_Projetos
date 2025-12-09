# Script para executar a aplicação TodoList no Windows
# Usa Maven Wrapper para não depender de instalação local do Maven

# Configuração de cores
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

# Sobe apenas os serviços de infraestrutura (bancos de dados)
docker compose up -d postgres redis mongodb --remove-orphans

if ($LASTEXITCODE -ne 0) {
    Write-Error "Falha ao iniciar containers."
    exit 1
}

Write-Info "Aguardando bancos de dados inicializarem..."
Start-Sleep -Seconds 5

# Verifica se o Maven Wrapper existe
$mvnCmd = if (Test-Path ".\mvnw.cmd") {
    ".\mvnw.cmd"
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    "mvn"
} else {
    Write-Error "Maven não encontrado. Instalando Maven Wrapper..."
    # Download do Maven Wrapper
    $mvnWrapperUrl = "https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw.cmd"
    $mvnWrapperJarUrl = "https://raw.githubusercontent.com/takari/maven-wrapper/master/maven-wrapper.jar"
    
    try {
        New-Item -ItemType Directory -Force -Path ".\.mvn\wrapper" | Out-Null
        Invoke-WebRequest -Uri $mvnWrapperUrl -OutFile "mvnw.cmd"
        Invoke-WebRequest -Uri "https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw" -OutFile "mvnw"
        
        Write-Error "Maven Wrapper baixado, mas ainda precisa ser configurado. Por favor, instale o Maven ou configure o Maven Wrapper manualmente."
        exit 1
    } catch {
        Write-Error "Falha ao baixar Maven Wrapper. Por favor, instale o Maven manualmente."
        Write-Info "Você pode instalar o Maven usando: winget install Apache.Maven"
        Write-Info "Ou baixar de: https://maven.apache.org/download.cgi"
        exit 1
    }
}

# Compila a aplicação
Write-Info "Compilando a aplicação Java..."
& $mvnCmd clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Error "Falha na compilação."
    exit 1
}

# Define variáveis de ambiente para conectar aos serviços no localhost
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
