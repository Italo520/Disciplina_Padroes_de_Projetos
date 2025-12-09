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
    Tarefa buscarPorId(Long id);

    /**
     * Busca todas as tarefas do repositório.
     *
     * @return uma lista com todas as tarefas.
     */
    List<Tarefa> buscarTodos();

    /**
     * Busca tarefas por data de deadline.
     *
     * @param dia a data limite.
     * @return uma lista de tarefas para o dia.
     */
    List<Tarefa> buscarPorDia(java.time.LocalDate dia);

    /**
     * Busca tarefas consideradas críticas (prazo próximo e alta prioridade).
     *
     * @return uma lista de tarefas críticas.
     */
    List<Tarefa> buscarTarefasCriticas();
}
