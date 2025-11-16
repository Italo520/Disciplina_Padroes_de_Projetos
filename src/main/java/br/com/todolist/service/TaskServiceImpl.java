package br.com.todolist.service;

import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.ItemRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {

    private final ItemRepository<Itens> itemRepository;
    private final String emailUsuario;

    public TaskServiceImpl(ItemRepository<Itens> itemRepository, String emailUsuario) {
        this.itemRepository = itemRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public void cadastrarTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            itemRepository.salvar(tarefa);
        }
    }

    @Override
    public void excluirTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            itemRepository.excluir(tarefa);
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
        }
    }

    @Override
    public void atualizarTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            itemRepository.atualizar(tarefa);
        }
    }

    @Override
    public List<Tarefa> listarTodasTarefas() {
        return itemRepository.buscarTodos().stream()
                .filter(item -> item instanceof Tarefa && emailUsuario.equals(item.getCriado_por()))
                .map(item -> (Tarefa) item)
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
                .filter(tarefa -> {
                    long diasRestantes = ChronoUnit.DAYS.between(hoje, tarefa.getDeadline());
                    return (diasRestantes - tarefa.getPrioridade()) < 0;
                })
                .collect(Collectors.toList());
    }
}