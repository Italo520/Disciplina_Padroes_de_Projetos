package br.com.todolist.exception;

public class TarefaNaoEncontradaException extends BusinessException {
    public TarefaNaoEncontradaException(String id) {
        super("Tarefa com ID '" + id + "' não encontrada.");
    }
}