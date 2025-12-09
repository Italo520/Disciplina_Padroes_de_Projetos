# To-Do List (Java Design Patterns)

> **Sistema de Gerenciamento de Tarefas (To-Do List)** desenvolvido em Java Swing para demonstrar a aplicação de **Padrões de Projeto (GoF)** e princípios **SOLID**.

## 🧩 Patterns Implementados

Este projeto serve como um catálogo vivo de padrões de projeto:

- **Observer**: Monitoramento de auditoria (`EventAuditObserver`).
- **Strategy**: Estratégias de notificação e relatórios (`INotificador`, `IGeradorRelatorio`).
- **Factory Method**: Criação padronizada de itens (`DefaultItemFactory`).
- **Singleton**: Gerenciamento único de sessão (`SessionManager`).

## 🚀 Quick Start

### Windows (PowerShell)

Para rodar o ambiente completo (App + Bancos) usando Docker:

```powershell
.\scripts\run-app.ps1
```

> Se você não tiver o Maven instalado, o script tentará usar o Maven Wrapper ou você pode usar `.\scripts\run-app-docker-build.ps1` para compilar via Docker.

### Monitoramento Unificado

Para ver logs da aplicação e dos bancos de dados em tempo real:

```powershell
.\scripts\monitor-all.ps1
```

### Linux / Mac

```bash
./scripts/run-app.sh
```

> O script irá subir o PostgreSQL, Redis, MongoDB e a Aplicação Java automaticamente.

## 📚 Documentação

A documentação completa do projeto foi reorganizada para facilitar o entendimento:

- **[Arquitetura e Padrões](docs/01-architecture/)**: Detalhes sobre as decisões técnicas e diagramas.
- **[Guia de Instalação](docs/02-setup/)**: Como rodar com Docker ou Localmente.
- **[Desenvolvimento](docs/03-development/)**: Histórico de refatoração e evolução do código.

---
*Desenvolvido para a disciplina de Padrões de Projetos.*
