package br.com.todolist.repository;

import br.com.todolist.entity.Tarefa;

/**
 * Interface para o repositório de tarefas.
 * Estende a interface genérica IRepository e define as operações de persistência para a entidade Tarefa.
 */
public interface ITarefaRepository extends IRepository<Tarefa, String> {
    // Métodos específicos para o repositório de tarefas podem ser definidos aqui.
}
