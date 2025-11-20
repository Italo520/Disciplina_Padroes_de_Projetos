package br.com.todolist.exception;

/**
 * Exceção base para regras de negócio.
 * Deve ser estendida por exceções específicas de negócio.
 * É uma Checked Exception para forçar o tratamento nas camadas superiores (Controller/View).
 */
public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
