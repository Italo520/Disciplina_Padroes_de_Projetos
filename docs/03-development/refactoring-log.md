# Histórico de Refatoração e Evolução

Este documento registra a evolução arquitetural do projeto, detalhando como saímos de uma implementação acoplada para uma arquitetura baseada em padrões de projeto (SOLID).

---

## 🔄 Fase 1: Tratamento de Exceções e Camadas

### O Problema
O código original misturava lógica de apresentação (Swing) com lógica de banco de dados e não tinha uma hierarquia clara de exceções.

### A Solução
1.  **Nova Hierarquia de Exceções**:
    *   `BusinessException` (Checked): Para regras de negócio.
    *   `DatabaseException` (Unchecked): Para erros de infraestrutura.
2.  **Refatoração de Repositórios**: Remoção de `printStackTrace` e encapsulamento de erros do Hibernate/JPA.
3.  **Refatoração de Services**: Tradução de erros técnicos em erros de negócio quando apropriado.

---

## 🔄 Fase 2: Aplicação de Padrões de Projeto (GoF)

### 1. Sistema de Notificações (Strategy)
**Antes:** A classe `Mensageiro` era rígida e só enviava e-mails.
**Depois:** Interface `INotificador` com implementações `NotificadorEmail` e `NotificadorWhatsApp`.
**Ganho:** OCP (Open/Closed Principle). Podemos adicionar SMS sem mexer no código existente.

### 2. Sistema de Relatórios (Strategy)
**Antes:** A classe `Central` fazia tudo (PDF e Excel), violando o SRP.
**Depois:** Interface `IGeradorRelatorio` com implementações `GeradorRelatorioPDF` e `GeradorRelatorioExcel`.
**Ganho:** SRP (Single Responsibility Principle). Cada classe faz apenas uma coisa.

### 3. Refatoração do ReportServiceImpl (Dependency Inversion)
**Antes:** O serviço dependia das classes concretas `Mensageiro` e `Central`.
**Depois:** O serviço depende das interfaces `INotificador` e `IGeradorRelatorio`.
**Ganho:** Baixo acoplamento e facilidade de testes (Mocks).

---

## 📊 Comparativo: Antes vs Depois

### Fluxo de Dependências

**ANTES (Acoplamento Forte):**
```
ReportServiceImpl -> Mensageiro (Concreto)
ReportServiceImpl -> Central (Concreto)
```

**DEPOIS (Inversão de Dependência):**
```
ReportServiceImpl -> INotificador (Interface) <- NotificadorEmail
ReportServiceImpl -> IGeradorRelatorio (Interface) <- GeradorRelatorioPDF
```

## Classes Removidas
As seguintes classes legadas foram removidas durante a refatoração:
- `br.com.todolist.util.Mensageiro`
- `br.com.todolist.util.Central`
