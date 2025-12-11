# Plano de Implementação de Qualidade de Software - To-Do List Project

Este documento detalha a estratégia completa para elevar a qualidade do software do projeto To-Do List, migrando para uma arquitetura de testes robusta e moderna.

## 1. Estratégia de Testes

### 1.1 Pirâmide de Testes
Adotaremos uma distribuição de testes baseada na Pirâmide de Testes de Cohn, adaptada para a complexidade do projeto:

*   **Testes Unitários (70%)**: Foco na lógica de negócio, padrões de projeto (Strategy, Observer, Factory, Singleton) e utilitários. São rápidos, isolados e baratos de manter.
    *   *Justificativa*: O projeto possui muitas regras de negócio encapsuladas em padrões de projeto. Garantir que cada unidade funcione isoladamente é crucial antes de testar integrações.
*   **Testes de Integração (20%)**: Foco na persistência (PostgreSQL, MongoDB), cache (Redis) e serviços externos (Email).
    *   *Justificativa*: Com múltiplos bancos de dados e sistemas externos, é vital garantir que os componentes conversem corretamente. Usaremos **Testcontainers** para ambientes descartáveis e fiéis à produção.
*   **Testes End-to-End (10%)**: Foco nos fluxos críticos do usuário via GUI (Swing).
    *   *Justificativa*: Testes de GUI são frágeis e lentos. Focaremos apenas nos "Caminhos Felizes" e fluxos críticos de erro para garantir que o usuário final consiga operar o sistema.

### 1.2 Tipos de Testes a Implementar

| Tipo | Escopo | Ferramentas | Prioridade | Cobertura Meta |
| :--- | :--- | :--- | :--- | :--- |
| **Unitários** | Classes de negócio, Padrões GOF, Helpers | JUnit 5, Mockito, AssertJ | **Crítica** | 80%+ |
| **Integração** | Repositórios (SQL/NoSQL), Cache, Auditoria | Testcontainers, H2 (rápido) | **Alta** | 70%+ |
| **Interface (GUI)** | Telas principais, Fluxos de navegação | AssertJ Swing | Média | 30% (Fluxos principais) |
| **Performance** | Operações de banco, Geração de Relatórios | JMH (Microbenchmark) | Baixa | N/A (Foco em gargalos) |
| **Segurança** | Auth, Criptografia, SQL Injection | JUnit 5, SpotBugs | Alta | 100% (Componentes de Seg) |
| **Relatórios** | Geração de PDF e Excel | JUnit 5, Apache POI/iText | Média | 80% |

---

## 2. Estrutura de Testes

A organização dos diretórios espelhará a estrutura do código fonte, mas separada por tipos de teste para facilitar a execução seletiva.

```text
src/test/java/br/com/todolist/
├── unit/                       # TESTES UNITÁRIOS (Rápidos, sem IO/DB)
│   ├── domain/                 # Entidades (User, Task, etc.)
│   ├── service/                # Regras de negócio
│   ├── patterns/               # Testes específicos dos Padrões GOF
│   │   ├── factory/            # DefaultItemFactory
│   │   ├── observer/           # EventAuditObserver
│   │   ├── singleton/          # SessionManager
│   │   └── strategy/           # Notificadores e Relatórios
│   └── util/                   # Helpers e Utilitários
├── integration/                # TESTES DE INTEGRAÇÃO (Lentos, com DB/Docker)
│   ├── repository/             # Repositórios (Postgres, Mongo)
│   ├── cache/                  # Redis
│   └── audit/                  # Auditoria e Logs
├── gui/                        # TESTES DE INTERFACE (Swing)
│   ├── flows/                  # Fluxos completos (Login -> Criar Tarefa)
│   └── components/             # Testes de componentes isolados
└── fixtures/                   # INFRAESTRUTURA DE TESTES
    ├── builders/               # Test Data Builders (Criação fácil de objetos)
    └── config/                 # Configurações de Testcontainers e Contexto
```

### 2.2 Classes Prioritárias (Top 10 para Sprint 1)

1.  **`SessionManagerTest`** (Unit): Garantir que o Singleton é thread-safe e gerencia a sessão corretamente.
2.  **`DefaultItemFactoryTest`** (Unit): Garantir a criação correta de Tarefas e Eventos.
3.  **`TaskRepositoryIT`** (Integration): CRUD completo de tarefas usando Testcontainers (PostgreSQL).
4.  **`EventAuditObserverTest`** (Unit/Integration): Garantir que eventos geram logs de auditoria.
5.  **`BCryptServiceTest`** (Unit): Validar hashing e verificação de senhas.
6.  **`EmailNotificationStrategyTest`** (Unit): Mockar envio de email e validar lógica de template.
7.  **`UserEntityTest`** (Unit): Validações de campos obrigatórios e regras de domínio.
8.  **`RedisCacheIT`** (Integration): Testar set/get e expiração de cache.
9.  **`ReportGeneratorStrategyTest`** (Unit): Validar seleção de estratégia (PDF vs Excel).
10. **`LoginFlowGuiTest`** (GUI): Teste automatizado de login com sucesso e falha.

---

## 3. Atualização do `pom.xml`

Adicione as seguintes dependências ao seu `pom.xml` para habilitar a stack de testes moderna.

### 3.1 Dependências (Adicionar dentro de `<dependencies>`)

```xml
<!-- ================= TEST DEPENDENCIES ================= -->

<!-- JUnit 5 BOM (Bill of Materials) para gerenciar versões -->
<dependency>
    <groupId>org.junit</groupId>
    <artifactId>junit-bom</artifactId>
    <version>5.10.1</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<!-- JUnit 5 API e Engine -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>

<!-- Mockito para Mocks -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.8.0</version>
    <scope>test</scope>
</dependency>

<!-- AssertJ para Asserções Fluentes (Melhor que assertEquals) -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.25.1</version>
    <scope>test</scope>
</dependency>

<!-- Testcontainers (Docker para testes de integração) -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.19.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.19.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mongodb</artifactId>
    <version>1.19.3</version>
    <scope>test</scope>
</dependency>

<!-- AssertJ Swing para testes de GUI -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-swing-junit</artifactId>
    <version>3.17.1</version>
    <scope>test</scope>
</dependency>
```

### 3.2 Plugins (Adicionar em `<build><plugins>`)

```xml
<!-- Plugin para rodar testes JUnit 5 -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.3</version>
</plugin>

<!-- Plugin de Cobertura (JaCoCo) -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

---

## 4. Exemplos de Implementação

### 4.1 Teste Unitário: Singleton (SessionManager)

```java
package br.com.todolist.unit.patterns.singleton;

import br.com.todolist.session.SessionManager;
import br.com.todolist.model.User;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class SessionManagerTest {

    @BeforeEach
    void clearSession() {
        SessionManager.getInstance().logout();
    }

    @Test
    @DisplayName("Deve manter a mesma instância (Singleton)")
    void shouldReturnSameInstance() {
        SessionManager s1 = SessionManager.getInstance();
        SessionManager s2 = SessionManager.getInstance();
        assertThat(s1).isSameAs(s2);
    }

    @Test
    @DisplayName("Deve armazenar usuário na sessão")
    void shouldStoreUserInSession() {
        User user = new User("test", "pass");
        SessionManager.getInstance().login(user);
        
        assertThat(SessionManager.getInstance().getCurrentUser())
            .isNotNull()
            .isEqualTo(user);
    }
}
```

### 4.2 Teste de Integração: Repositório com Testcontainers

```java
package br.com.todolist.integration.repository;

import br.com.todolist.model.Task;
import br.com.todolist.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.*;

@Testcontainers
class TaskRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Test
    void shouldSaveAndRetrieveTask() {
        // Configurar conexão com postgres.getJdbcUrl(), postgres.getUsername()...
        // (Isso geralmente é feito em um @BeforeAll configurando o EntityManager)
        
        TaskRepository repo = new TaskRepository(entityManager);
        Task task = new Task("Comprar leite");
        
        repo.save(task);
        
        Task found = repo.findById(task.getId());
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Comprar leite");
    }
}
```

---

## 5. Cronograma de Execução

Para um aluno de ADS ou desenvolvedor júnior/pleno, recomenda-se a seguinte abordagem gradual:

*   **Sprint 1 (Fundação)**:
    *   Configurar `pom.xml`.
    *   Criar estrutura de pastas.
    *   Implementar testes unitários para o `SessionManager` e `Factory`.
    *   *Meta*: Rodar `mvn test` e ver os primeiros "verdes".

*   **Sprint 2 (Regras de Negócio)**:
    *   Cobrir as Strategies (Notificações e Relatórios) com testes unitários usando Mocks.
    *   Testar validações das Entidades.

*   **Sprint 3 (Integração)**:
    *   Configurar Testcontainers.
    *   Criar testes para `TaskRepository` e `UserRepository`.
    *   Garantir que o banco sobe e desce corretamente nos testes.

*   **Sprint 4 (Refinamento)**:
    *   Testes de GUI básicos.
    *   Configurar CI (GitHub Actions) para rodar testes no push.
    *   Analisar relatório do JaCoCo e melhorar cobertura.

## 6. Próximos Passos

1.  Aprovar este plano.
2.  Executar a atualização do `pom.xml`.
3.  Criar a estrutura de diretórios `src/test/java`.
4.  Escrever o primeiro teste (`SessionManagerTest`).
