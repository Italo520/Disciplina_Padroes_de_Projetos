package br.com.todolist.exception;

/**
 * Exceção para erros de infraestrutura e persistência.
 * É uma Unchecked Exception (RuntimeException) pois geralmente representa falhas irrecuperáveis
 * ou técnicas que devem ser tratadas no topo da pilha ou convertidas em BusinessException.
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
