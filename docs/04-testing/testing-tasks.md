# Tarefas de Implementação de Qualidade

- [x] **Configuração Inicial**
    - [x] Atualizar `pom.xml` com dependências (JUnit 5, Mockito, AssertJ, Testcontainers) <!-- id: 0 -->
    - [x] Criar estrutura de diretórios `src/test/java` e `src/test/resources` <!-- id: 1 -->
    - [x] Criar arquivos de configuração de teste (`application-test.properties`, `log4j2-test.xml`) <!-- id: 2 -->

- [x] **Sprint 1: Testes Unitários Críticos**
    - [x] Implementar `SessionManagerTest` (Singleton) <!-- id: 3 -->
    - [x] Implementar `DefaultItemFactoryTest` (Factory) <!-- id: 4 -->
    - [x] Implementar `EventAuditObserverTest` (Observer) <!-- id: 5 -->

- [x] **Sprint 2: Testes de Integração**
    - [x] Configurar Testcontainers para PostgreSQL <!-- id: 6 -->
    - [x] Implementar `TaskRepositoryIT` (Integração com Banco) <!-- id: 7 -->
    - [x] Implementar `ServiceIntegrationTest` (Fluxo completo) <!-- id: 8 -->

- [x] **Sprint 3: Testes de Padrões e Estratégias**
    - [x] Implementar `NotificationStrategyTest` <!-- id: 8 -->
    - [x] Implementar `ReportStrategyTest` <!-- id: 9 -->

- [x] **Sprint 4: CI/CD e Documentação**
    - [x] Criar script local de execução de testes (`run_tests.sh`) <!-- id: 10 -->
    - [x] Gerar relatório de cobertura JaCoCo <!-- id: 11 -->

- [x] **Sprint 5: Testes de Controladores**
    - [x] Refatorar `AuthController` para injeção de dependência <!-- id: 12 -->
    - [x] Implementar `AuthControllerTest` <!-- id: 13 -->
    - [x] Implementar `TaskControllerTest` <!-- id: 14 -->
    - [x] Implementar `EventControllerTest` <!-- id: 15 -->

# Plano de Qualidade Enterprise (Meta: 80% Cobertura)

- [ ] **Fase 1: Camada de Serviço (Crítico)**
    - [x] Implementar `UserServiceImplTest` (6 cenários) <!-- id: 16 -->
    - [x] Implementar `ReportServiceImplTest` (3 cenários) <!-- id: 17 -->
    - [x] Completar `EventServiceImplTest` (13 métodos) <!-- id: 18 -->
    - [x] Completar `TaskServiceImplTest` (8 branches) <!-- id: 19 -->

- [ ] **Fase 2: Camada de Repositório (Alta)**
    - [x] Implementar `UserRepositoryPostgresTest` (CRUD) <!-- id: 20 -->
    - [x] Implementar `EventoRepositoryPostgresTest` (Consultas) <!-- id: 21 -->
    - [x] Implementar `UserRepositoryImplTest` (JSON) <!-- id: 22 -->
    - [x] Completar Repositórios Parciais (`TarefaRepositoryImpl`, `EventoRepositoryImpl`) <!-- id: 23 -->

- [ ] **Fase 3: Utilitários e Helpers (Alta)**
    - [x] Implementar `MensageiroTest` (Envio de Email) <!-- id: 24 -->
    - [x] Implementar `DefaultProgressCalculationStrategyTest` (Estratégia) <!-- id: 25 -->

- [ ] **Fase 4: Controladores (Média)**
    - [x] Implementar `AppControllerTest` (Fluxo Principal) <!-- id: 26 -->

- [x] **Fase 5: Interface Gráfica (Estratégico)**
    - [x] Definir estratégia (AssertJ Swing ou Refatoração) <!-- id: 27 -->
    - [x] Implementar `TelaLoginTest` (Login com Sucesso/Falha) <!-- id: 29 -->

- [x] **Fase 6: Entidades (Baixa)**
    - [x] Implementar `TarefaTest` (POJO) <!-- id: 28 -->
    - [x] Implementar `EventoTest` (POJO) <!-- id: 29 -->
    - [x] Implementar `UsuarioTest` (POJO) <!-- id: 30 -->

- [x] **Fase 7: UI Enterprise (Refatoração e Testes)**
    - [x] Refatorar `TelaLogin` para Injeção de Dependência <!-- id: 31 -->
    - [x] Implementar `TelaLoginTest` (Mocks + UI) <!-- id: 32 -->
    - [x] Refatorar `TelaCadastro` para Injeção de Dependência <!-- id: 33 -->
    - [x] Implementar `TelaCadastroTest` <!-- id: 34 -->
    - [x] Implementar `TelaPrincipalTest` e Painéis <!-- id: 35 -->
