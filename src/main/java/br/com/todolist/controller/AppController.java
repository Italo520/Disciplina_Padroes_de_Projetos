package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.ItemRepository;
import br.com.todolist.repository.ItemRepositoryImpl;
import br.com.todolist.service.EventService;
import br.com.todolist.service.EventServiceImpl;
import br.com.todolist.service.ReportService;
import br.com.todolist.service.ReportServiceImpl;
import br.com.todolist.service.TaskService;
import br.com.todolist.factory.ItemFactory;
import br.com.todolist.service.TaskServiceImpl;
import br.com.todolist.service.UserService;
import br.com.todolist.util.Mensageiro;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Fachada (Facade) para o sistema.
 * Simplifica a comunicação entre a UI e os serviços da aplicação,
 * atuando como um ponto central de controle.
 */
public class AppController {

    private static AppController instance;
    private final UserService userService;
    private final Mensageiro mensageiro;
    private final ItemFactory itemFactory;
    private TaskService taskService;
    private EventService eventService;
    private ReportService reportService;
    private Usuario usuarioLogado;

    private AppController(UserService userService, Mensageiro mensageiro, ItemFactory itemFactory) {
        this.userService = userService;
        this.mensageiro = mensageiro;
        this.itemFactory = itemFactory;
    }

    /**
     * Inicializa a instância Singleton da fachada.
     * Este método deve ser chamado apenas uma vez na inicialização da aplicação.
     *
     * @param userService o serviço de usuário.
     * @param mensageiro o utilitário de mensageria.
     * @param itemFactory a fábrica de itens.
     */
    public static void init(UserService userService, Mensageiro mensageiro, ItemFactory itemFactory) {
        if (instance == null) {
            instance = new AppController(userService, mensageiro, itemFactory);
        }
    }

    /**
     * Retorna a instância Singleton da fachada.
     *
     * @return a instância de AppController.
     * @throws IllegalStateException se a instância não foi inicializada.
     */
    public static AppController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppController não foi inicializado. Chame o método init primeiro.");
        }
        return instance;
    }

    public boolean login(String email, String password) {
        usuarioLogado = userService.autenticarUsuario(email, password);
        if (usuarioLogado != null) {
            ItemRepository<Itens> itemRepository = new ItemRepositoryImpl();
            this.taskService = new TaskServiceImpl(itemRepository, usuarioLogado.getEmail());
            this.eventService = new EventServiceImpl(itemRepository, usuarioLogado.getEmail());
            this.reportService = new ReportServiceImpl(taskService, mensageiro);
            return true;
        }
        return false;
    }

    public boolean cadastrarUsuario(String nome, String email, String password) {
        return userService.criarNovoUsuario(nome, email, password);
    }

    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Tarefa novaTarefa = itemFactory.criarTarefa(titulo, descricao, usuarioLogado.getEmail(), deadline, prioridade);
        taskService.cadastrarTarefa(novaTarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return taskService.listarTodasTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) {
        taskService.excluirTarefa(tarefa);
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade) {
        taskService.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
    }

    public void atualizarTarefa(Tarefa tarefa) {
        taskService.atualizarTarefa(tarefa);
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return taskService.listarTarefasPorDia(dia);
    }

    public List<Tarefa> listarTarefasCriticas() {
        return taskService.listarTarefasCriticas();
    }

    public boolean cadastrarEvento(String titulo, String descricao, LocalDate deadline) {
        Evento novoEvento = itemFactory.criarEvento(titulo, descricao, usuarioLogado.getEmail(), deadline);
        return eventService.cadastrarEvento(novoEvento);
    }

    public List<Evento> listarTodosEventos() {
        return eventService.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) {
        eventService.excluirEvento(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        eventService.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return eventService.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return eventService.listarEventosPorMes(mes);
    }

    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia) {
        return reportService.enviarRelatorioTarefasDoDiaPorEmail(dia, usuarioLogado);
    }

    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        reportService.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
