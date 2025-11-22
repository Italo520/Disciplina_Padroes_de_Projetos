package br.com.todolist.service.util;

import br.com.todolist.service.event.TaskEvent;

/**
 * Interface para o padrão Observer específico para eventos de tarefa.
 * Define o método que deve ser implementado por observadores de tarefas.
 */
public interface ITaskObserver {

    /**
     * Método chamado quando um evento de tarefa é atualizado.
     *
     * @param event o evento que foi atualizado.
     */
    void update(TaskEvent event);
}
