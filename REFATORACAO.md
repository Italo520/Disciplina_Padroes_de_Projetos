# Documentação da Refatoração de Tratamento de Exceções

Este documento detalha as mudanças arquiteturais implementadas para tornar o sistema de tratamento de exceções robusto, seguindo os princípios SOLID e Clean Code.

## 1. Nova Hierarquia de Exceções

Criamos um pacote `br.com.todolist.exception` para centralizar as exceções de negócio e de infraestrutura.

### BusinessException (Checked)
Base para todas as exceções de regra de negócio. Força o tratamento nas camadas superiores (View/Controller).

```java
package br.com.todolist.exception;

public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### DatabaseException (Unchecked)
Encapsula erros técnicos de persistência (JPA/Hibernate), desacoplando a camada de serviço de detalhes de implementação do banco.

```java
package br.com.todolist.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    // ...
}
```

### UsuarioJaCadastradoException
Exemplo de exceção específica de negócio.

```java
package br.com.todolist.exception;

public class UsuarioJaCadastradoException extends BusinessException {
    public UsuarioJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado no sistema.");
    }
}
```

---

## 2. Refatoração da Camada de Repositório

Removemos os `e.printStackTrace()` e passamos a capturar exceções do Hibernate, relançando-as como `DatabaseException`.

**Exemplo: `UserRepositoryPostgres`**

```java
@Override
public void salvar(Usuario entity) {
    EntityManager em = DatabaseConnection.getInstance().getEntityManager();
    try {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        // Encapsula o erro original (ex: ConstraintViolationException)
        throw new DatabaseException("Erro ao salvar usuário: " + e.getMessage(), e);
    } finally {
        em.close();
    }
}
```

---

## 3. Refatoração da Camada de Serviço

A camada de serviço agora traduz erros de infraestrutura em regras de negócio ou validações prévias.

**Exemplo: `UserServiceImpl`**

```java
@Override
public void criarNovoUsuario(String nome, String email, String password) throws BusinessException {
    // 1. Validação de Negócio Prévia
    if (userRepository.buscarPorEmail(email) != null) {
        throw new UsuarioJaCadastradoException(email);
    }

    String senhaHasheada = hashSenha(password);
    Usuario novoUsuario = new Usuario(nome, email, senhaHasheada);

    try {
        userRepository.salvar(novoUsuario);
    } catch (DatabaseException e) {
        // 2. Tradução de Erros Técnicos (caso a validação prévia falhe por concorrência)
        if (e.getCause() != null && e.getCause().getMessage().contains("ConstraintViolation")) {
            throw new UsuarioJaCadastradoException(email);
        }
        throw e; // Relança se for outro erro técnico
    }
}
```

---

## 4. Refatoração de Controller e View

Os Controllers propagam as exceções de negócio, e as Views (Telas) são responsáveis por capturá-las e exibir mensagens amigáveis.

**Exemplo: `AuthController`**

```java
public void cadastrarUsuario(String nome, String email, String password) throws BusinessException {
    userService.criarNovoUsuario(nome, email, password);
}
```

**Exemplo: `TelaCadastro` (View)**

```java
private void realizarCadastro() {
    try {
        authController.cadastrarUsuario(nome, email, senha);
        JOptionPane.showMessageDialog(this, "Sucesso!", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    } catch (BusinessException e) {
        // Captura exceção de negócio e exibe mensagem limpa
        JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        // Captura erros inesperados
        JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
```

---

## 5. Princípios SOLID Aplicados

### Single Responsibility Principle (SRP)
*   **Exceptions:** Classes de exceção apenas definem o tipo do erro.
*   **Repository:** Foca apenas em acesso a dados e captura de erros técnicos.
*   **Service:** Foca em regras de negócio e orquestração.
*   **View:** Foca na exibição (incluindo erros). O Controller não decide COMO mostrar o erro, apenas repassa.

### Open/Closed Principle (OCP)
*   Novas exceções de negócio podem ser criadas estendendo `BusinessException` sem alterar o código existente que captura `BusinessException` genericamente (se desejado) ou permitindo novos blocos `catch` específicos sem quebrar a lógica de fluxo.
*   A mudança para PostgreSQL foi feita criando novas implementações de Repositório (`UserRepositoryPostgres`) sem alterar as interfaces (`IUserRepository`), permitindo a substituição fácil via injeção de dependência (Manual DI neste caso).

### Dependency Inversion Principle (DIP)
*   Os Serviços dependem de abstrações (`IUserRepository`), não de implementações concretas.
*   As exceções lançadas pelos repositórios (`DatabaseException`) são desacopladas da tecnologia específica (Hibernate/SQL), permitindo que o Serviço trate erros de persistência de forma agnóstica.
