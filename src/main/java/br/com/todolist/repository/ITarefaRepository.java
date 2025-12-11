package br.com.todolist.repository;

import br.com.todolist.entity.Tarefa;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para o repositório de tarefas.
 * Estende a interface genérica IRepository e define as operações de
 * persistência para a entidade Tarefa.
 */
public interface ITarefaRepository extends IRepository<Tarefa, String> {
    List<Tarefa> buscarPorDia(LocalDate dia);

    List<Tarefa> buscarTarefasCriticas(int prioridadeMinima);
}
