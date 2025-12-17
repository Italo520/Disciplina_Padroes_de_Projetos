package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.log.EventAuditObserver;
import br.com.todolist.service.SessionManager;
import br.com.todolist.log.ILogRepository;
import br.com.todolist.log.LogService;
import br.com.todolist.log.TaskAuditObserver;
import br.com.todolist.repository.postgres.EventoRepositoryPostgres;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.repository.postgres.TarefaRepositoryPostgres;
import br.com.todolist.repository.cache.CachedEventoRepository;
import br.com.todolist.repository.cache.CachedTarefaRepository;
import br.com.todolist.service.IEventService;
import br.com.todolist.service.IReportService;
import br.com.todolist.service.ITaskService;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.impl.EventServiceImpl;
import br.com.todolist.service.impl.ReportServiceImpl;
import br.com.todolist.service.impl.TaskServiceImpl;
import br.com.todolist.service.util.IItemFactory;
import br.com.todolist.util.notificacao.INotificador;
import br.com.todolist.util.relatorio.GeradorRelatorioPDF;
import br.com.todolist.util.relatorio.GeradorRelatorioExcel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class AppController {

    private static AppController instance;
    private final IUserService userService;
    private final INotificador notificador;
    private final IItemFactory itemFactory;
    private ITaskService taskService;
    private IEventService eventService;
    private IReportService reportService;
    private Usuario usuarioLogado;

    private AppController(IUserService userService, INotificador notificador, IItemFactory itemFactory) {
        this.userService = userService;
        this.notificador = notificador;
        this.itemFactory = itemFactory;
    }

    public static void init(IUserService userService, INotificador notificador, IItemFactory itemFactory) {
        if (instance == null) {
            instance = new AppController(userService, notificador, itemFactory);
        }
    }

    public static AppController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppController não foi inicializado. Chame o método init primeiro.");
        }
        return instance;
    }

    public boolean login(String email, String password) {
        try {
            usuarioLogado = userService.autenticarUsuario(email, password);
            if (usuarioLogado != null) {
                configurarRepositorios(usuarioLogado);
                return true;
            }
        } catch (BusinessException e) {

        }
        return false;
    }

    private void configurarRepositorios(Usuario usuario) {
        ITarefaRepository tarefaRepository = new CachedTarefaRepository(new TarefaRepositoryPostgres());
        IEventoRepository eventoRepository = new CachedEventoRepository(new EventoRepositoryPostgres());

        this.taskService = new TaskServiceImpl(tarefaRepository, usuario.getEmail());
        this.eventService = new EventServiceImpl(eventoRepository, usuario.getEmail());

        ILogRepository logRepository = LogService.getInstance().getRepository();
        TaskAuditObserver taskAuditObserver = new TaskAuditObserver(logRepository);
        this.taskService.addObserver(taskAuditObserver);
        this.eventService.addObserver(new EventAuditObserver(logRepository));

        br.com.todolist.log.LogEntry loginEntry = new br.com.todolist.log.LogEntry(
                br.com.todolist.log.AuditAction.LOGIN,
                "Usuario realizou login",
                usuario.getEmail());
        logRepository.salvarLog(loginEntry);

        this.reportService = new ReportServiceImpl(
                taskService,
                notificador,
                new GeradorRelatorioPDF(),
                new GeradorRelatorioExcel());

        SessionManager.getInstance().registrarSessao(usuario, this.taskService, this.eventService);
    }

    public void cadastrarUsuario(String nome, String email, String password) throws BusinessException {
        userService.criarNovoUsuario(nome, email, password);
    }

    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade)
            throws BusinessException {
        Tarefa novaTarefa = itemFactory.criarTarefa(titulo, descricao, usuarioLogado.getEmail(), deadline, prioridade);
        taskService.cadastrarTarefa(novaTarefa);
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

    public void cadastrarEvento(String titulo, String descricao, LocalDate deadline) throws BusinessException {
        Evento novoEvento = itemFactory.criarEvento(titulo, descricao, usuarioLogado.getEmail(), deadline);
        eventService.cadastrarEvento(novoEvento);
    }

    public List<Evento> listarTodosEventos() {
        return eventService.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) throws BusinessException {
        eventService.excluirEvento(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline)
            throws BusinessException {
        eventService.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return eventService.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return eventService.listarEventosPorMes(mes);
    }

    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia) {
        verificarUsuarioLogado();
        return reportService.enviarRelatorioTarefasDoDiaPorEmail(dia, usuarioLogado);
    }

    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        verificarUsuarioLogado();
        reportService.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
    }

    public void gerarRelatorioPDFTarefasDoDia(LocalDate dia, String nomeArquivo) {
        verificarUsuarioLogado();
        reportService.gerarRelatorioPDFTarefasDoDia(dia, nomeArquivo);
    }

    private void verificarUsuarioLogado() {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Você precisa estar logado para realizar esta operação.");
        }
        if (reportService == null || taskService == null || eventService == null) {
            throw new IllegalStateException(
                    "Serviços não foram inicializados corretamente. Tente fazer login novamente.");
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}