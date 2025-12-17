package br.com.todolist.service;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.event.TaskEvent;
import br.com.todolist.service.util.ITaskObserver;
import java.time.LocalDate;
import java.util.List;

public interface ITaskService {

    void cadastrarTarefa(Tarefa tarefa) throws BusinessException;

    void excluirTarefa(Tarefa tarefa) throws BusinessException;

    void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) throws BusinessException;

    Tarefa atualizarTarefa(Tarefa tarefa) throws BusinessException;

    List<Tarefa> listarTodasTarefas();

    List<Tarefa> listarTarefasPorDia(LocalDate dia);

    List<Tarefa> listarTarefasCriticas();

    void addObserver(ITaskObserver observer);

    void removeObserver(ITaskObserver observer);

    void notifyObservers(TaskEvent object);
}