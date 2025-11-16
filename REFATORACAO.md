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

- **Antes:** A classe `Orquestrador` era responsável por diversas tarefas, como a lógica de negócios, a comunicação com a persistência e a geração de relatórios.
- **Depois:** As responsabilidades foram divididas em classes menores e mais coesas:
    - `AppController`: Orquestra as ações do usuário.
    - `UserService`, `TaskService`, `EventService`: Lógica de negócios.
    - `UserRepository`, `ItemRepository`: Persistência de dados.

### 3.2. Open/Closed Principle (OCP)

- **Antes:** Para adicionar um novo tipo de item (além de `Tarefa` e `Evento`), seria necessário alterar a classe `GerenteDeDadosDoUsuario`.
- **Depois:** A introdução de interfaces como `ItemRepository` e `ItemService` permite a adição de novos tipos de itens sem a necessidade de alterar o código existente.

### 3.3. Liskov Substitution Principle (LSP)

O uso da herança entre `Itens`, `Tarefa` e `Evento` já respeitava este princípio. A refatoração manteve essa estrutura.

### 3.4. Interface Segregation Principle (ISP)

- **Antes:** Não havia interfaces.
- **Depois:** Foram criadas interfaces específicas para cada serviço e repositório, como `UserService`, `TaskService`, `UserRepository`, etc.

### 3.5. Dependency Inversion Principle (DIP)

- **Antes:** As classes da UI dependiam diretamente das classes de serviço concretas (ex: `TelaLogin` dependia de `GerenteDeUsuarios`).
- **Depois:** As classes da UI agora dependem do `AppController`, que por sua vez depende das interfaces de serviço.

## 4. Padrões de Projeto

### 4.1. Repository

- **Implementação:** `UserRepositoryImpl` e `ItemRepositoryImpl`.
- **Justificativa:** Isola a lógica de acesso a dados da lógica de negócios.

### 4.2. Service

- **Implementação:** `UserServiceImpl`, `TaskServiceImpl`, `EventServiceImpl`.
- **Justificativa:** Encapsula a lógica de negócios da aplicação.

### 4.3. Singleton

- **Implementação:** `AppController`.
- **Justificativa:** Garante que haja apenas uma instância do controlador na aplicação.

## 5. Exemplos de Antes e Depois

### 5.1. `TelaLogin`

**Antes:**

```java
public class TelaLogin extends JFrame {
    private final GerenteDeUsuarios gerenteDeUsuarios;

    public TelaLogin() {
        this.gerenteDeUsuarios = new GerenteDeUsuarios();
        // ...
    }
}
```

**Depois:**

```java
public class TelaLogin extends JFrame {
    private final AppController appController;

    public TelaLogin() {
        this.appController = AppController.getInstance();
        // ...
    }
}
```

## 6. Instruções para Validação

Para validar a arquitetura, o professor pode seguir os seguintes passos:

1.  Analisar a nova estrutura de pacotes.
2.  Verificar as interfaces criadas nos pacotes `repository` e `service`.
3.  Analisar as implementações das interfaces, observando a separação de responsabilidades.
4.  Verificar a classe `AppController` e sua implementação do padrão Singleton.
5.  Analisar as classes da UI e observar como elas interagem com o `AppController`.
