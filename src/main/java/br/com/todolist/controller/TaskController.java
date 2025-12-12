package br.com.todolist.controller;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.ITaskService;
import java.time.LocalDate;
import java.util.List;

public class TaskController {
    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    public void cadastrarTarefa(Tarefa tarefa) throws BusinessException {
        taskService.cadastrarTarefa(tarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return taskService.listarTodasTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) throws BusinessException {
        taskService.excluirTarefa(tarefa);
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) throws BusinessException {
        taskService.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
    }

    public Tarefa atualizarTarefa(Tarefa tarefa) throws BusinessException {
        return taskService.atualizarTarefa(tarefa);
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return taskService.listarTarefasPorDia(dia);
    }

    public List<Tarefa> listarTarefasCriticas() {
        return taskService.listarTarefasCriticas();
    }
}