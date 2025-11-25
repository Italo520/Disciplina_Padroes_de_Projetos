# Configuração do Ambiente Local (Sem Docker)

Se você preferir rodar a aplicação diretamente na sua máquina (sem Docker), siga estes passos.

## Pré-requisitos

*   **Java 17+** (JDK)
*   **Maven** instalado e configurado no PATH.
*   **Bancos de Dados**: Você precisará ter instâncias rodando localmente ou acessíveis de:
    *   PostgreSQL (Porta 5432)
    *   Redis (Porta 6379)
    *   MongoDB (Porta 27017)

> **Nota:** Se não quiser instalar os bancos, você pode subir apenas os bancos via Docker e rodar a App localmente.

## Configuração

1.  **Banco de Dados**:
    *   Crie um banco de dados chamado `todolist` no PostgreSQL.
    *   Execute o script `init.sql` (na raiz do projeto) para criar as tabelas.

2.  **Arquivo de Propriedades**:
    *   Copie `src/main/resources/database.properties.example` para `src/main/resources/database.properties`.
    *   Edite o arquivo com as credenciais do seu banco local.

## Executando a Aplicação

Na raiz do projeto, execute:

```bash
# Compilar e instalar dependências
mvn clean install

# Rodar a classe principal
mvn exec:java -Dexec.mainClass="br.com.todolist.Main"
```

## Solução de Problemas Comuns

*   **Erro de Conexão**: Verifique se o `database.properties` está correto e se os serviços de banco estão rodando.
*   **Dependências**: Se o Maven falhar, tente `mvn dependency:resolve`.
