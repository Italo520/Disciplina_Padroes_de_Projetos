package br.com.todolist.service;

import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.observer.Observer;
import br.com.todolist.repository.ItemRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do serviço de tarefas.
 * Contém a lógica de negócio para gerenciar tarefas e notificar observadores sobre mudanças.
 */
public class TaskServiceImpl implements TaskService {

    private final ItemRepository<Itens> itemRepository;
    private final String emailUsuario;
    private final List<Observer<Tarefa>> observers = new ArrayList<>();

    public TaskServiceImpl(ItemRepository<Itens> itemRepository, String emailUsuario) {
        this.itemRepository = itemRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public void cadastrarTarefa(Tarefa tarefa) {
        itemRepository.salvar(tarefa);
        notifyObservers(tarefa);
    }

    @Override
    public void excluirTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            itemRepository.excluir(tarefa);
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
            itemRepository.atualizar(tarefaOriginal);
            notifyObservers(tarefaOriginal);
        }
    }

    @Override
    public void atualizarTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            itemRepository.atualizar(tarefa);
            notifyObservers(tarefa);
        }
    }

    @Override
    public List<Tarefa> listarTodasTarefas() {
        List<Tarefa> tarefas = new ArrayList<>();
        for (Itens item : itemRepository.buscarTodos()) {
            if (item instanceof Tarefa && emailUsuario.equals(item.getCriado_por())) {
                tarefas.add((Tarefa) item);
            }
        }
        return tarefas;
    }

    @Override
    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        List<Tarefa> tarefasDoDia = new ArrayList<>();
        for (Tarefa tarefa : listarTodasTarefas()) {
            if (tarefa.getDeadline().isEqual(dia)) {
                tarefasDoDia.add(tarefa);
            }
        }
        return tarefasDoDia;
    }

    @Override
    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        List<Tarefa> tarefasCriticas = new ArrayList<>();
        for (Tarefa tarefa : listarTodasTarefas()) {
            long diasRestantes = ChronoUnit.DAYS.between(hoje, tarefa.getDeadline());
            if ((diasRestantes - tarefa.getPrioridade()) < 0) {
                tarefasCriticas.add(tarefa);
            }
        }
        return tarefasCriticas;
    }

    @Override
    public void addObserver(Observer<Tarefa> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Tarefa> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Tarefa tarefa) {
        for (Observer<Tarefa> observer : observers) {
            observer.update(tarefa);
        }
    }
}
