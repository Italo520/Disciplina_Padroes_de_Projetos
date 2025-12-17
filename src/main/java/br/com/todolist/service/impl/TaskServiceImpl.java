package br.com.todolist.service.impl;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.DadosInvalidosException;
import br.com.todolist.exception.DatabaseException;
import br.com.todolist.log.AuditAction;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.service.ITaskService;
import br.com.todolist.service.event.TaskEvent;
import br.com.todolist.service.util.ITaskObserver;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class TaskServiceImpl implements ITaskService {

    private final ITarefaRepository tarefaRepository;
    private final String emailUsuario;
    private final List<ITaskObserver> observers = new ArrayList<>();

    public TaskServiceImpl(ITarefaRepository tarefaRepository, String emailUsuario) {
        this.tarefaRepository = tarefaRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public void cadastrarTarefa(Tarefa tarefa) throws BusinessException {
        if (tarefa == null) {
            throw new DadosInvalidosException("Tarefa não pode ser nula.");
        }
        try {
            tarefaRepository.salvar(tarefa);
            notifyObservers(new TaskEvent(AuditAction.CREATE, tarefa));
        } catch (DatabaseException e) {
            throw new BusinessException("Erro ao salvar tarefa.", e);
        }
    }

    @Override
    public void excluirTarefa(Tarefa tarefa) throws BusinessException {
        if (tarefa.getCriadoPor() != null && tarefa.getCriadoPor().equals(emailUsuario)) {
            try {
                tarefaRepository.excluir(tarefa);
                notifyObservers(new TaskEvent(AuditAction.DELETE, tarefa));
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao excluir tarefa.", e);
            }
        } else {
            throw new DadosInvalidosException("Tarefa não pertence ao usuário logado.");
        }
    }

    @Override
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) throws BusinessException {
        if (tarefaOriginal.getCriadoPor() != null && tarefaOriginal.getCriadoPor().equals(emailUsuario)) {
            Tarefa oldTarefa = tarefaOriginal.copiar();

            tarefaOriginal.setTitulo(novoTitulo);
            tarefaOriginal.setDescricao(novaDescricao);
            tarefaOriginal.setDeadLine(novoDeadline);
            tarefaOriginal.setPrioridade(novaPrioridade);
            try {
                tarefaRepository.atualizar(tarefaOriginal);
                notifyObservers(new TaskEvent(AuditAction.UPDATE, tarefaOriginal, oldTarefa));
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao atualizar tarefa.", e);
            }
        } else {
            throw new DadosInvalidosException("Tarefa não pertence ao usuário logado.");
        }
    }

    @Override
    public Tarefa atualizarTarefa(Tarefa tarefa) throws BusinessException {
        if (tarefa.getCriadoPor() != null && tarefa.getCriadoPor().equals(emailUsuario)) {
            try {

                Tarefa oldTarefa = tarefaRepository.buscarPorId(tarefa.getId());
                if (oldTarefa != null) {
                    oldTarefa = oldTarefa.copiar();
                }
                Tarefa updatedTask = tarefaRepository.atualizar(tarefa);
                notifyObservers(new TaskEvent(AuditAction.UPDATE, updatedTask, oldTarefa));
                return updatedTask;
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao atualizar tarefa.", e);
            }
        } else {
            throw new DadosInvalidosException("Tarefa não pertence ao usuário logado.");
        }
    }

    @Override
    public List<Tarefa> listarTodasTarefas() {
        return tarefaRepository.buscarTodos().stream()
                .filter(tarefa -> tarefa.getCriadoPor() != null && tarefa.getCriadoPor().equals(emailUsuario))
                .toList();
    }

    @Override
    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return tarefaRepository.buscarPorDia(dia).stream()
                .filter(tarefa -> tarefa.getCriadoPor() != null && tarefa.getCriadoPor().equals(emailUsuario))
                .toList();
    }

    @Override
    public List<Tarefa> listarTarefasCriticas() {
        return tarefaRepository.buscarTarefasCriticas().stream()
                .filter(tarefa -> tarefa.getCriadoPor() != null && tarefa.getCriadoPor().equals(emailUsuario))
                .toList();
    }

    @Override
    public void addObserver(ITaskObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ITaskObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(TaskEvent event) {
        for (ITaskObserver observer : observers) {
            try {
                observer.update(event);
            } catch (Exception e) {

                System.err.println("ALERTA: Falha ao notificar observador (Auditoria): " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}