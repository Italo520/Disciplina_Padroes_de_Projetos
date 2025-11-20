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
 * Filtra as tarefas pelo e-mail do usuário logado.
 */
public class TaskServiceImpl implements ITaskService {

    private final ITarefaRepository tarefaRepository;
    private final String emailUsuario;
    private final List<IObserver<Tarefa>> observers = new ArrayList<>();

    /**
     * Construtor da classe TaskServiceImpl.
     *
     * @param tarefaRepository O repositório de tarefas a ser utilizado.
     * @param emailUsuario     O e-mail do usuário cujas tarefas serão gerenciadas.
     */
    public TaskServiceImpl(ITarefaRepository tarefaRepository, String emailUsuario) {
        this.tarefaRepository = tarefaRepository;
        this.emailUsuario = emailUsuario;
    }

    /**
     * Cadastra uma nova tarefa para o usuário.
     * Notifica os observadores após o cadastro.
     *
     * @param tarefa A tarefa a ser cadastrada.
     */
    @Override
    public void cadastrarTarefa(Tarefa tarefa) {
        tarefaRepository.salvar(tarefa);
        notifyObservers(tarefa);
    }

    /**
     * Exclui uma tarefa, garantindo que pertença ao usuário logado.
     * Notifica os observadores após a exclusão.
     *
     * @param tarefa A tarefa a ser excluída.
     */
    @Override
    public void excluirTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            tarefaRepository.excluir(tarefa);
            notifyObservers(tarefa);
        }
    }

    /**
     * Edita os detalhes de uma tarefa existente, se pertencer ao usuário.
     *
     * @param tarefaOriginal A tarefa original.
     * @param novoTitulo     O novo título.
     * @param novaDescricao  A nova descrição.
     * @param novoDeadline   O novo prazo.
     * @param novaPrioridade A nova prioridade.
     */
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

    /**
     * Atualiza o estado de uma tarefa (ex: conclusão, subtarefas).
     *
     * @param tarefa A tarefa com os dados atualizados.
     */
    @Override
    public void atualizarTarefa(Tarefa tarefa) {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            tarefaRepository.atualizar(tarefa);
            notifyObservers(tarefa);
        }
    }

    /**
     * Lista todas as tarefas do usuário logado.
     *
     * @return Uma lista de tarefas filtrada pelo e-mail do usuário.
     */
    @Override
    public List<Tarefa> listarTodasTarefas() {
        return tarefaRepository.buscarTodos().stream()
                .filter(tarefa -> tarefa.getCriado_por().equals(emailUsuario))
                .collect(Collectors.toList());
    }

    /**
     * Lista as tarefas do usuário para um dia específico.
     *
     * @param dia O dia a ser consultado.
     * @return Uma lista de tarefas do dia.
     */
    @Override
    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return listarTodasTarefas().stream()
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    /**
     * Lista as tarefas críticas do usuário.
     * O critério de criticidade é baseado na proximidade do prazo e na prioridade.
     *
     * @return Uma lista de tarefas críticas.
     */
    @Override
    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return listarTodasTarefas().stream()
                .filter(tarefa -> ChronoUnit.DAYS.between(hoje, tarefa.getDeadline()) - tarefa.getPrioridade() < 0)
                .collect(Collectors.toList());
    }

    /**
     * Adiciona um observador para receber notificações sobre mudanças nas tarefas.
     *
     * @param observer O observador a ser adicionado.
     */
    @Override
    public void addObserver(IObserver<Tarefa> observer) {
        observers.add(observer);
    }

    /**
     * Remove um observador.
     *
     * @param observer O observador a ser removido.
     */
    @Override
    public void removeObserver(IObserver<Tarefa> observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores registrados sobre uma mudança em uma tarefa.
     *
     * @param tarefa A tarefa que sofreu alteração.
     */
    @Override
    public void notifyObservers(Tarefa tarefa) {
        for (IObserver<Tarefa> observer : observers) {
            observer.update(tarefa);
        }
    }
}
