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

# Verifica se o Maven está instalado via Scoop ou PATH
$mvnCmd = $null

# 1. Tenta encontrar via Scoop (caminho comum)
$scoopMaven = "$env:USERPROFILE\scoop\apps\maven\current\bin\mvn.cmd"
if (Test-Path $scoopMaven) {
    $mvnCmd = $scoopMaven
    Write-Info "Usando Maven do Scoop: $mvnCmd"
} 
# 2. Tenta encontrar no PATH
elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    $mvnCmd = "mvn"
    Write-Info "Usando Maven do PATH"
}
# 3. Tenta usar o Maven Wrapper
elseif (Test-Path ".\mvnw.cmd") {
    if (Test-Path ".\.mvn\wrapper\maven-wrapper.properties") {
        $mvnCmd = ".\mvnw.cmd"
        Write-Info "Usando Maven Wrapper"
    } else {
        Write-Info "Maven Wrapper incompleto. Tentando reparar..."
    }
}

# Se não encontrou ou wrapper está quebrado, tenta baixar/reparar o Wrapper
if (-not $mvnCmd) {
    Write-Info "Maven não encontrado. Instalando Maven Wrapper..."
    $mvnWrapperUrl = "https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw.cmd"
    $mvnWrapperJarUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    $mvnWrapperPropsUrl = "https://raw.githubusercontent.com/takari/maven-wrapper/master/maven-wrapper.properties"
    
    try {
        New-Item -ItemType Directory -Force -Path ".\.mvn\wrapper" | Out-Null
        
        # Baixa mvnw.cmd se não existir
        if (!(Test-Path "mvnw.cmd")) {
            Invoke-WebRequest -Uri $mvnWrapperUrl -OutFile "mvnw.cmd"
        }
        
        # Baixa maven-wrapper.jar
        Invoke-WebRequest -Uri $mvnWrapperJarUrl -OutFile ".\.mvn\wrapper\maven-wrapper.jar"
        
        # Cria maven-wrapper.properties
        $propsContent = "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip`nwrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
        Set-Content -Path ".\.mvn\wrapper\maven-wrapper.properties" -Value $propsContent
        
        $mvnCmd = ".\mvnw.cmd"
        Write-Success "Maven Wrapper configurado com sucesso."
    } catch {
        Write-Error "Falha ao configurar Maven Wrapper."
        Write-Info "Por favor, instale o Maven manualmente (ex: 'scoop install maven')."
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
