# Relatório de Refatoração do Projeto To-Do List

## 1. Introdução

Este documento detalha o processo de refatoração do projeto To-Do List, que foi realizado com o objetivo de aplicar os princípios de Programação Orientada a Objetos (POO), SOLID e padrões de projeto, utilizando um estilo de código compatível com Java 8.

## 2. Estrutura de Pacotes

A estrutura de pacotes foi reorganizada para seguir o padrão Model-View-Controller (MVC), com a adição de uma camada de serviço e repositório.

**Estrutura Antiga:**

```
src/main/java/br/com/todolist/
├── models/
├── persistence/
├── service/
├── ui/
└── util/
```

**Estrutura Nova:**

```
src/main/java/br/com/todolist/
├── entity/
├── repository/
├── service/
├── controller/
└── ui/
```

- **entity:** Contém as classes de modelo (POJOs) que representam os dados da aplicação.
- **repository:** Responsável pela persistência dos dados.
- **service:** Contém a lógica de negócios da aplicação.
- **controller:** Responsável por mediar a comunicação entre a UI e os serviços.
- **ui:** Contém as classes da interface gráfica do usuário.

## 3. Princípios SOLID

### 3.1. Single Responsibility Principle (SRP)

- **Antes:** A classe `Orquestrador` centralizava múltiplas responsabilidades, como lógica de autenticação, gerenciamento de tarefas, eventos e geração de relatórios, violando o SRP.
- **Depois:** As responsabilidades foram decompostas em classes coesas:
    - **Controladores Especializados:** `AuthController`, `TaskController`, `EventController`, e `ReportController` foram criados, cada um com a responsabilidade única de orquestrar um domínio específico da aplicação.
    - **Serviços:** `UserService`, `TaskService`, e `EventService` encapsulam a lógica de negócios, como a criação de entidades e a validação de regras.
    - **Repositórios:** `UserRepository` e `ItemRepository` lidam exclusivamente com a persistência dos dados.

### 3.2. Open/Closed Principle (OCP)

- **Antes:** Adicionar um novo tipo de item (além de `Tarefa` e `Evento`) exigiria alterações em classes existentes como `GerenteDeDadosDoUsuario`.
- **Depois:** O uso de interfaces como `ItemRepository` e a nova estrutura de serviços permitem a extensão do sistema (ex: adicionar um novo tipo de item com seu próprio serviço e controlador) sem modificar o código existente.

### 3.3. Liskov Substitution Principle (LSP)

O uso da herança entre `Itens`, `Tarefa` e `Evento` já respeitava este princípio. A refatoração manteve e reforçou essa estrutura.

### 3.4. Interface Segregation Principle (ISP)

- **Antes:** Não havia interfaces, forçando acoplamento a implementações concretas.
- **Depois:** Foram criadas interfaces específicas para cada serviço e repositório (`UserService`, `TaskService`, `UserRepository`), garantindo que os clientes dependam apenas dos métodos que utilizam.

### 3.5. Dependency Inversion Principle (DIP)

- **Antes:** As classes da UI dependiam diretamente de classes de lógica concretas (`GerenteDeUsuarios`).
- **Depois:** As classes da UI agora dependem dos controladores especializados, que por sua vez dependem das **abstrações** (interfaces) dos serviços, e não de suas implementações concretas.

## 4. Padrões de Projeto

### 4.1. Repository

- **Implementação:** `UserRepositoryImpl` e `ItemRepositoryImpl`.
- **Justificativa:** Isola a lógica de acesso a dados da lógica de negócios, permitindo que a forma de persistência (JSON, banco de dados, etc.) possa ser trocada sem impactar o resto do sistema.

### 4.2. Service

- **Implementação:** `UserServiceImpl`, `TaskServiceImpl`, `EventServiceImpl`.
- **Justificativa:** Encapsula a lógica de negócios e as regras da aplicação, mantendo os controladores enxutos e focados na orquestração.

### 4.3. Singleton

- **Implementação:** `SessionManager`.
- **Justificativa:** Garante uma única instância para gerenciar o estado da sessão do usuário (usuário logado e serviços associados), fornecendo um ponto de acesso global e controlado.

### 4.4. Controller

- **Implementação:** `AuthController`, `TaskController`, `EventController`, `ReportController`.
- **Justificativa:** Atua como um intermediário entre a UI e a camada de serviço. Cada controlador tem uma responsabilidade única, recebendo as requisições da UI e delegando a execução para os serviços apropriados.

## 5. Exemplos de Antes e Depois

### 5.1. `TelaLogin`

**Antes (com `AppController` como fachada):**

```java
public class TelaLogin extends JFrame {
    private final AppController appController;

    public TelaLogin() {
        this.appController = AppController.getInstance();
        // ...
        if (appController.login(email, senha)) {
            new TelaPrincipal(appController).setVisible(true); // Passa o AppController
            this.dispose();
        }
    }
}
```

**Depois (com `AuthController` e `SessionManager`):**

```java
public class TelaLogin extends JFrame {
    private final AuthController authController;

    public TelaLogin() {
        this.authController = new AuthController();
        // ...
        Usuario usuario = authController.login(email, senha);
        if (usuario != null) {
            SessionManager.getInstance().login(usuario); // Inicia a sessão
            new TelaPrincipal().setVisible(true); // Não precisa mais passar dependências
            this.dispose();
        }
    }
}
```

## 6. Instruções para Validação

Para validar a arquitetura, o professor pode seguir os seguintes passos:

1.  **Analisar a Estrutura de Pacotes:** Verificar a organização em `entity`, `repository`, `service`, `controller` e `ui`.
2.  **Verificar os Controladores:** Analisar as classes no pacote `controller` e confirmar que cada uma possui uma responsabilidade única (autenticação, tarefas, eventos, relatórios).
3.  **Analisar o `SessionManager`:** Inspecionar a classe `SessionManager` no pacote `service` e sua implementação do padrão Singleton.
4.  **Verificar a Injeção de Dependência:** Observar como as classes da UI (`TelaPrincipal`, `PainelTarefas`, etc.) recebem e utilizam os controladores específicos, e como os controladores, por sua vez, dependem das interfaces de serviço.
5.  **Analisar a Camada de Serviço:** Confirmar que a lógica de negócios, como a criação de objetos `Tarefa`, está encapsulada nos serviços (`TaskServiceImpl`) e não nos controladores.
