package br.com.todolist.exception;

/**
 * Exceção lançada quando os dados de entrada são inválidos.
 */
public class DadosInvalidosException extends BusinessException {
    public DadosInvalidosException(String message) {
        super(message);
    }
}
