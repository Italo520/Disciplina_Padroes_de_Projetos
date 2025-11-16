package br.com.todolist.service;

import br.com.todolist.entity.Tarefa;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    void cadastrarTarefa(Tarefa tarefa);
    void excluirTarefa(Tarefa tarefa);
    void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade);
    void atualizarTarefa(Tarefa tarefa);
    List<Tarefa> listarTodasTarefas();
    List<Tarefa> listarTarefasPorDia(LocalDate dia);
    List<Tarefa> listarTarefasCriticas();
}