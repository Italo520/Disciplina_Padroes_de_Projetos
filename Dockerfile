# Multi-stage build para otimizar o tamanho da imagem final

# Estágio 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copia os arquivos de configuração do Maven primeiro (para aproveitar cache de layers)
COPY pom.xml .

# Baixa as dependências (isso será cacheado se o pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila e empacota a aplicação (cria o JAR com dependências)
RUN mvn clean package -DskipTests=true

# Estágio 2: Imagem runtime otimizada
FROM eclipse-temurin:21-jre-alpine

# Informações sobre a imagem
LABEL maintainer="todolist-team"
LABEL version="2.0"
LABEL description="ToDoList Application - Java 21 + PostgreSQL"

# Cria um usuário não-root para segurança
RUN addgroup -g 1000 appgroup && \
    adduser -D -u 1000 -G appgroup appuser

WORKDIR /app

# Copia o JAR compilado do estágio builder
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

# Define o usuário não-root
USER appuser

# Variáveis de ambiente com valores padrão (podem ser sobrescritas no docker-compose)
ENV DB_HOST=postgres \
    DB_PORT=5432 \
    DB_NAME=todolist \
    DB_USER=todolist_user \
    DB_PASSWORD=todolist_pass \
    REDIS_HOST=redis \
    REDIS_PORT=6379 \
    MONGO_HOST=mongodb \
    MONGO_PORT=27017 \
    MONGO_DATABASE=todolist_logs

# Expõe a porta da aplicação (se houver servidor web no futuro)
# EXPOSE 8080

# Healthcheck (verifica se a aplicação está rodando)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD pgrep -f "java.*app.jar" || exit 1

# Comando para executar a aplicação com configurações de JVM otimizadas
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
