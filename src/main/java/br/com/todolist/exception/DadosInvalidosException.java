package br.com.todolist.exception;

public class DadosInvalidosException extends BusinessException {
    public DadosInvalidosException(String message) {
        super(message);
    }
}