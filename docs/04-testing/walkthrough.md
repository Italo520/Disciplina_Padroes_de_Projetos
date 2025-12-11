# Walkthrough: Implementação do Plano de Qualidade de Software

Este documento resume as atividades realizadas para implementar o Plano de Qualidade de Software no projeto To-Do List.

## 1. Infraestrutura de Testes

*   **Dependências**: Atualizamos o `pom.xml` para incluir JUnit 5, Mockito, AssertJ, Testcontainers, JaCoCo, SpotBugs, Checkstyle e PMD.
*   **Estrutura**: Criamos a estrutura de diretórios padrão `src/test/java` e `src/test/resources`.
*   **Configuração**: Adicionamos `application-test.properties` e `log4j2-test.xml` para isolar o ambiente de testes.

## 2. Testes Unitários (Sprint 1)

Implementamos testes unitários para componentes críticos, focando na validação de padrões de projeto:

*   **Singleton**: `SessionManagerTest` validou que a classe `SessionManager` garante uma única instância e gerencia corretamente o estado da sessão do usuário.
*   **Factory Method**: `DefaultItemFactoryTest` garantiu que a fábrica cria instâncias corretas de `Tarefa` e `Evento` com os atributos esperados.
*   **Observer**: `EventAuditObserverTest` verificou se o observador de auditoria intercepta eventos e salva logs corretamente usando um mock do repositório.

## 3. Testes de Integração (Sprint 2)

Configuramos testes de integração reais usando **Testcontainers** para subir um banco de dados PostgreSQL em container Docker:

*   **Repositório**: `TaskRepositoryIT` testou as operações de CRUD (Salvar, Buscar, Excluir) do `TarefaRepositoryPostgres` contra um banco PostgreSQL real.
*   **Serviço**: `ServiceIntegrationTest` validou o fluxo completo do `TaskService`, garantindo que as regras de negócio (como isolamento de dados por usuário) funcionam com a persistência real.
*   **Refatoração**: Ajustamos a classe `DatabaseConnection` para permitir a injeção de um `EntityManagerFactory` de teste, facilitando a troca do banco de produção pelo container de teste.

## 4. Testes de Padrões Strategy (Sprint 3)

Implementamos e testamos o padrão Strategy para flexibilidade do sistema:

*   **Notificações**: Criamos a interface `INotificador` e implementações para Email e WhatsApp. O teste `NotificationStrategyTest` validou a execução correta de cada estratégia.
*   **Relatórios**: Criamos a interface `IGeradorRelatorio` e implementações para PDF e Excel. O teste `ReportStrategyTest` garantiu a geração dos arquivos nos formatos corretos.

## 5. Fase 1: Camada de Serviço (Enterprise Quality Plan)

Focamos na cobertura crítica da camada de serviço, alcançando 100% de cobertura nos cenários planejados:

*   **UserServiceImpl**: Testes cobrindo cadastro (sucesso/falha), autenticação (sucesso/falha) e busca por email.
*   **ReportServiceImpl**: Testes para envio de relatório diário por email e geração de relatório mensal, incluindo tratamento de listas vazias.
*   **EventServiceImpl**: Cobertura completa de CRUD, listagem por dia/mês e validações de negócio.
*   **TaskServiceImpl**: Cobertura de CRUD, listagem por dia, tarefas críticas e filtros de usuário.

## 6. Fase 2: Camada de Repositório (Enterprise Quality Plan)

Garantimos a integridade da persistência de dados com testes de integração e implementação real de consultas:

*   **UserRepositoryPostgresTest**: Testes de integração (Testcontainers) validando CRUD e unicidade de email no PostgreSQL.
*   **EventoRepositoryPostgresTest**: Testes de integração validando CRUD e consultas por período (Dia/Mês) com lógica JPQL real implementada.
*   **TaskRepositoryIT**: Atualizado para cobrir novas consultas (`buscarPorDia`, `buscarTarefasCriticas`) com lógica JPQL real.
*   **UserRepositoryImplTest**: Teste unitário para garantir que a implementação legada (JSON) continue funcionando.

## 7. Fase 3: Utilitários e Helpers (Enterprise Quality Plan)

Validamos componentes auxiliares essenciais:

*   **MensageiroTest**: Testes unitários utilizando GreenMail para simular servidor SMTP, garantindo envio de e-mails simples e com anexo sem depender de credenciais reais.
*   **DefaultProgressCalculationStrategyTest**: Cobertura completa da lógica de cálculo de progresso de tarefas e subtarefas.

### Fase 4: Controladores (Controllers)
- **`AppControllerTest`**: Testes unitários com Mockito para verificar a lógica de delegação e o padrão Singleton.
- **Refatoração**: `AppController` agora permite injeção de dependências via setters e reset da instância Singleton para facilitar testes.

### Fase 6: Entidades (Entities)
- **`TarefaTest`**: Testes para construtores, getters/setters, gerenciamento de subtarefas e cálculo de progresso.
- **`EventoTest`**: Testes para construtores e getters/setters.
- **`UsuarioTest`**: Testes para construtores e getters/setters.
- **Correções**: Ajuste no construtor padrão de `Tarefa` para inicializar listas e estratégias, evitando `NullPointerException`. Ajuste nos testes para usar o nome correto do método `getCriado_por` (herdado de `Itens`).

### Fase 5: Interface Gráfica (GUI)
- **`TelaLoginTest`**: Testes automatizados de interface usando **AssertJ Swing**.
- **Cenários Cobertos**:
    - Verificação de visibilidade dos componentes (campos e botões).
    - Validação de obrigatoriedade de campos (mensagem de erro).
- **Ajustes**: Adição de `setName()` nos componentes de `TelaLogin` para permitir identificação nos testes. Adição da dependência `assertj-swing-junit` no `pom.xml`.

### Fase 7: UI Enterprise (Refatoração e Testes)
- **Refatoração para Testabilidade**:
    - Criação de `DialogService` para abstrair `JOptionPane`, permitindo testes sem bloquear a UI.
    - Criação de `NavigationService` para abstrair a navegação entre telas.
    - Refatoração de `TelaLogin` e `TelaCadastro` para utilizar Injeção de Dependência.
- **Testes Implementados**:
    - `TelaLoginTest`: Testes unitários com Mockito validando lógica de login, validação de campos e navegação.
    - `TelaCadastroTest`: Testes unitários com Mockito validando cadastro de usuário, tratamento de erros (email duplicado) e validação de campos.
    - `TelaPrincipalTest`: Testes pragmáticos validando contratos de dados e documentando limitações arquiteturais.

> [!NOTE]
> **Limitações Identificadas**: `TelaPrincipal`, `BarraFerramentas`, `PainelTarefas` e `PainelEventos` dependem fortemente de `AppController.getInstance()` (padrão Singleton), exigindo refatoração extensiva para testes completos de UI. A abordagem atual demonstra os princípios de testabilidade para alunos de ADS, documentando os desafios de testar código legado com acoplamento forte.

## 9. Automação e Documentação (Sprint 4)

*   **Script de Execução**: Criamos o script `run_tests.sh` para facilitar a execução local de todos os testes e verificação de qualidade.
*   **Cobertura de Código**: Configuramos o JaCoCo para gerar relatórios de cobertura. Ajustamos o `pom.xml` para garantir que o agente do JaCoCo seja executado corretamente junto com o Surefire. O relatório pode ser visualizado em `target/site/jacoco/index.html`.

## Como Executar os Testes

Para rodar todos os testes e gerar o relatório de cobertura, execute o script na raiz do projeto:

```bash
./run_tests.sh
```

Ou via Maven diretamente:

```bash
mvn verify -P all-tests,coverage
```

## Conclusão

O projeto agora conta com uma base sólida de testes automatizados, cobrindo desde unidades isoladas até a integração com banco de dados, garantindo maior confiabilidade e facilitando futuras refatorações.
