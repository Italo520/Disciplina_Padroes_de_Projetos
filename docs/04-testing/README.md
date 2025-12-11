# Documentação de Testes - To-Do List

Esta pasta contém toda a documentação relacionada aos testes automatizados implementados no projeto.

## 📁 Estrutura

- **[walkthrough.md](walkthrough.md)** - Guia completo do processo de implementação de testes, incluindo todas as fases executadas
- **[implementation_plan.md](implementation_plan.md)** - Plano de implementação detalhado das fases de testes
- **[plano_implementacao_qualidade.md](plano_implementacao_qualidade.md)** - Plano de qualidade enterprise original
- **[testing-tasks.md](testing-tasks.md)** - Checklist de tarefas de implementação de testes

## 📊 Cobertura de Testes

O projeto agora inclui:

### ✅ Testes Unitários
- Padrões de Projeto (Singleton, Factory, Observer)
- Camada de Serviço (UserService, TaskService, EventService, ReportService)
- Camada de Controle (AuthController, TaskController, EventController, AppController)
- Utilitários (Mensageiro, ProgressCalculation)
- Entidades (Tarefa, Evento, Usuario)

### ✅ Testes de Integração
- Repositórios PostgreSQL (TaskRepository, EventRepository, UserRepository)
- Fluxos completos de serviço com banco de dados real (Testcontainers)

### ✅ Testes de UI Enterprise
- TelaLogin (com Dependency Injection e mocks)
- TelaCadastro (com Dependency Injection e mocks)
- TelaPrincipal (testes pragmáticos e documentação de limitações)

### ✅ Testes de Estratégia
- Estratégias de Notificação (Email, WhatsApp)
- Estratégias de Relatório (PDF, Excel)

## 🔧 Ferramentas Utilizadas

- **JUnit 5** - Framework de testes
- **Mockito** - Mocking de dependências
- **AssertJ** - Assertions fluentes
- **Testcontainers** - Testes de integração com PostgreSQL
- **GreenMail** - Servidor SMTP para testes de e-mail
- **JaCoCo** - Cobertura de código

## 🚀 Executando os Testes

```bash
# Todos os testes
mvn test

# Com relatório de cobertura
mvn verify

# Script automatizado
./run_tests.sh
```

## 📈 Relatórios

Os relatórios de cobertura JaCoCo podem ser encontrados em:
- `target/site/jacoco/index.html`

