package br.com.todolist.exception;

/**
 * Exceção lançada quando uma tarefa não é encontrada.
 */
public class TarefaNaoEncontradaException extends BusinessException {
    public TarefaNaoEncontradaException(String id) {
        super("Tarefa com ID '" + id + "' não encontrada.");
    }
}
