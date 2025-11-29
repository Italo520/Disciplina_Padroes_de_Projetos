package br.com.todolist.repository;

import br.com.todolist.entity.Tarefa;
import java.util.List;

/**
 * Interface para o repositório de tarefas.
 * Define as operações de persistência para a entidade Tarefa.
 */
public interface ITarefaRepository {

    /**
     * Salva uma tarefa no repositório.
     *
     * @param entity a tarefa a ser salva.
     */
    void salvar(Tarefa entity);

    /**
     * Exclui uma tarefa do repositório.
     *
     * @param entity a tarefa a ser excluída.
     */
    void excluir(Tarefa entity);

    /**
     * Atualiza uma tarefa no repositório.
     *
     * @param entity a tarefa a ser atualizada.
     * @return a tarefa atualizada.
     */
    Tarefa atualizar(Tarefa entity);

    /**
     * Busca uma tarefa pelo seu identificador.
     *
     * @param id o identificador da tarefa.
     * @return a tarefa encontrada, ou null se não for encontrada.
     */
    Tarefa buscarPorId(String id);

    /**
     * Busca todas as tarefas do repositório.
     *
     * @return uma lista com todas as tarefas.
     */
    List<Tarefa> buscarTodos();
    // Métodos específicos para o repositório de tarefas podem ser definidos aqui.
}
