package br.com.todolist.repository;

import br.com.todolist.entity.Tarefa;
import java.util.List;

public interface ITarefaRepository {
    void salvar(Tarefa entity);
    void excluir(Tarefa entity);
    Tarefa atualizar(Tarefa entity);
    Tarefa buscarPorId(Long id);
    List<Tarefa> buscarTodos();
    List<Tarefa> buscarPorDia(java.time.LocalDate dia);
    List<Tarefa> buscarTarefasCriticas();
}