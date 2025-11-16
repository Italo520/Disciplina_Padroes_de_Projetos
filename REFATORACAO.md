# Documento de Refatoração - To-Do List

Este documento detalha o processo de refatoração do projeto To-Do List, com foco na aplicação de princípios de arquitetura de software, SOLID e padrões de projeto para fins didáticos.

## 1. Estrutura de Pastas

A estrutura de pacotes foi reorganizada para seguir uma arquitetura mais clara e modular.

**Antes:**

```
src/main/java/
└── br/com/todolist/
    ├── controller/
    │   ├── AppController.java
    │   ├── AuthController.java
    │   ├── EventController.java
    │   ├── ReportController.java
    │   └── TaskController.java
    ├── entity/
    ├── repository/
    ├── service/
    ├── ui/
    └── util/
```

**Depois:**

```
src/main/java/
└── br/com/todolist/
    ├── controller/
    │   └── AppController.java
    ├── entity/
    ├── factory/
    │   ├── DefaultItemFactory.java
    │   └── ItemFactory.java
    ├── observer/
    │   ├── EmailNotifier.java
    │   ├── Observer.java
    │   └── Subject.java
    ├── repository/
    ├── service/
    ├── strategy/
    │   ├── DefaultProgressCalculationStrategy.java
    │   └── ProgressCalculationStrategy.java
    ├── ui/
    └── util/
```

## 2. Princípios SOLID

A seguir, um resumo de como cada princípio SOLID foi aplicado no projeto.

- **SRP (Single Responsibility Principle):** A lógica de geração de relatórios foi movida do `AppController` para um `ReportService` dedicado, garantindo que cada classe tenha uma única responsabilidade.
- **OCP (Open/Closed Principle):** A introdução dos padrões Strategy e Observer permite que o sistema seja estendido com novos comportamentos (cálculo de progresso, notificações) sem modificar o código existente.
- **LSP (Liskov Substitution Principle):** As interfaces de serviço e repositório garantem que as implementações concretas possam ser substituídas sem quebrar o sistema.
- **ISP (Interface Segregation Principle):** Foram criadas interfaces específicas para cada serviço e repositório, evitando que as classes implementem métodos que não precisam.
- **DIP (Dependency Inversion Principle):** Os serviços e controladores agora dependem de abstrações (interfaces) em vez de implementações concretas, promovendo o desacoplamento e a testabilidade.

## 3. Padrões de Projeto

Os seguintes padrões de projeto foram implementados para melhorar a modularidade, flexibilidade e manutenibilidade do código.

- **Repository:** A camada de persistência foi refatorada para usar uma interface genérica `Repository`, desacoplando os serviços da implementação do acesso a dados.
- **Strategy:** O cálculo do progresso das tarefas agora é feito através de uma estratégia, permitindo que diferentes algoritmos de cálculo sejam usados de forma intercambiável.
- **Observer:** Um sistema de notificações foi implementado usando o padrão Observer, permitindo que diferentes partes do sistema reajam a eventos (criação, edição, exclusão de tarefas) de forma desacoplada.
- **Facade:** O `AppController` atua como uma fachada, simplificando a comunicação entre a UI e os serviços da aplicação.
- **Factory:** A criação de objetos `Tarefa` e `Evento` foi centralizada em uma `ItemFactory`, encapsulando a lógica de instanciação.
- **Singleton:** A classe `SessionManager` foi implementada como um Singleton para garantir que haja apenas uma instância global de gerenciamento de sessão.

## 4. Orientações para Professores

Esta versão refatorada do projeto To-Do List serve como um exemplo prático de como aplicar princípios de arquitetura de software e padrões de projeto em uma aplicação Java simples. Os alunos podem usar este projeto como base para entender os seguintes conceitos:

- **Arquitetura em Camadas:** A separação clara entre as camadas de apresentação (UI), controle, serviço e persistência.
- **Inversão de Dependência:** A importância de depender de abstrações em vez de implementações concretas.
- **Padrões de Projeto:** Como os padrões de projeto podem ser usados para resolver problemas comuns de design de software.

Incentive os alunos a explorar o código, identificar os padrões implementados e discutir as vantagens de cada um. Eles também podem tentar estender o projeto com novas funcionalidades, como:

- Uma nova estratégia de cálculo de progresso de tarefas.
- Um novo tipo de notificação (por exemplo, SMS ou push).
- Um novo tipo de item (por exemplo, um lembrete).
