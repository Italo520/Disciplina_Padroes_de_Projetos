package br.com.todolist.service.impl;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.service.util.IObserver;
import br.com.todolist.service.ITaskService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de tarefas.
 * Contém a lógica de negócio para gerenciar tarefas e notificar observadores sobre mudanças.
 */
public class TaskServiceImpl implements ITaskService {

    private final ITarefaRepository tarefaRepository;
    private final String emailUsuario;
    private final List<IObserver<Tarefa>> observers = new ArrayList<>();

    public TaskServiceImpl(ITarefaRepository tarefaRepository, String emailUsuario) {
        this.tarefaRepository = tarefaRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public void cadastrarTarefa(Tarefa tarefa) {
        tarefaRepository.salvar(tarefa);
        notifyObservers(tarefa);
    }

    @Override
    public void excluirTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            tarefaRepository.excluir(tarefa);
            notifyObservers(tarefa);
        }
    }

    @Override
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade) {
        if (tarefaOriginal.getCriado_por().equals(emailUsuario)) {
            tarefaOriginal.setTitulo(novoTitulo);
            tarefaOriginal.setDescricao(novaDescricao);
            tarefaOriginal.setDeadLine(novoDeadline);
            tarefaOriginal.setPrioridade(novaPrioridade);
            tarefaRepository.atualizar(tarefaOriginal);
            notifyObservers(tarefaOriginal);
        }
    }

    @Override
    public void atualizarTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            tarefaRepository.atualizar(tarefa);
            notifyObservers(tarefa);
        }
    }

    @Override
    public List<Tarefa> listarTodasTarefas() {
        return tarefaRepository.buscarTodos().stream()
                .filter(tarefa -> tarefa.getCriado_por().equals(emailUsuario))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return listarTodasTarefas().stream()
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return listarTodasTarefas().stream()
                .filter(tarefa -> ChronoUnit.DAYS.between(hoje, tarefa.getDeadline()) - tarefa.getPrioridade() < 0)
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(IObserver<Tarefa> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver<Tarefa> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Tarefa tarefa) {
        for (IObserver<Tarefa> observer : observers) {
            observer.update(tarefa);
        }
    }
}
