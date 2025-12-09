package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.log.EventAuditObserver;
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

/**
 * Fachada (Facade) para o sistema.
 * Simplifica a comunicação entre a UI e os serviços da aplicação,
 * atuando como um ponto central de controle.
 */
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

    /**
     * Inicializa a instância Singleton da fachada.
     * Este método deve ser chamado apenas uma vez na inicialização da aplicação.
     *
     * @param userService o serviço de usuário.
     * @param notificador o notificador (e-mail, WhatsApp, etc.).
     * @param itemFactory a fábrica de itens.
     */
    public static void init(IUserService userService, INotificador notificador, IItemFactory itemFactory) {
        if (instance == null) {
            instance = new AppController(userService, notificador, itemFactory);
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

    /**
     * Realiza o login do usuário e inicializa os serviços dependentes do usuário.
     *
     * @param email    O e-mail do usuário.
     * @param password A senha do usuário.
     * @return true se o login for bem-sucedido, false caso contrário.
     */
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

        // Configuração de Auditoria (Log)
        ILogRepository logRepository = LogService.getInstance().getRepository();
        TaskAuditObserver taskAuditObserver = new TaskAuditObserver(logRepository);
        this.taskService.addObserver(taskAuditObserver);
        this.eventService.addObserver(new EventAuditObserver(logRepository));

        // Log de Login
        br.com.todolist.log.LogEntry loginEntry = new br.com.todolist.log.LogEntry(
                br.com.todolist.log.AuditAction.LOGIN,
                "Usuario realizou login",
                usuario.getEmail());
        logRepository.salvarLog(loginEntry);

        // Instancia os geradores de relatório seguindo o padrão Strategy
        this.reportService = new ReportServiceImpl(
                taskService,
                notificador,
                new GeradorRelatorioPDF(),
                new GeradorRelatorioExcel());
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param nome     O nome do usuário.
     * @param email    O e-mail do usuário.
     * @param password A senha do usuário.
     * @throws BusinessException se houver erro no cadastro.
     */
    public void cadastrarUsuario(String nome, String email, String password) throws BusinessException {
        userService.criarNovoUsuario(nome, email, password);
    }

    /**
     * Cadastra uma nova tarefa para o usuário logado.
     *
     * @param titulo     O título da tarefa.
     * @param descricao  A descrição da tarefa.
     * @param deadline   O prazo da tarefa.
     * @param prioridade A prioridade da tarefa.
     * @throws BusinessException se houver erro no cadastro.
     */
    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade)
            throws BusinessException {
        Tarefa novaTarefa = itemFactory.criarTarefa(titulo, descricao, usuarioLogado.getEmail(), deadline, prioridade);
        taskService.cadastrarTarefa(novaTarefa);
    }

    /**
     * Lista todas as tarefas do usuário logado.
     *
     * @return Uma lista de tarefas.
     */
    public List<Tarefa> listarTodasTarefas() {
        return taskService.listarTodasTarefas();
    }

    /**
     * Exclui uma tarefa do usuário logado.
     *
     * @param tarefa A tarefa a ser excluída.
     * @throws BusinessException se houver erro na exclusão.
     */
    public void excluirTarefa(Tarefa tarefa) throws BusinessException {
        taskService.excluirTarefa(tarefa);
    }

    /**
     * Edita uma tarefa existente.
     *
     * @param tarefaOriginal A tarefa original a ser editada.
     * @param novoTitulo     O novo título.
     * @param novaDescricao  A nova descrição.
     * @param novoDeadline   O novo prazo.
     * @param novaPrioridade A nova prioridade.
     * @throws BusinessException se houver erro na edição.
     */
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) throws BusinessException {
        taskService.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
    }

    /**
     * Atualiza uma tarefa (ex: marca como concluída).
     *
     * @param tarefa A tarefa a ser atualizada.
     * @return A tarefa atualizada.
     * @throws BusinessException se houver erro na atualização.
     */
    public Tarefa atualizarTarefa(Tarefa tarefa) throws BusinessException {
        return taskService.atualizarTarefa(tarefa);
    }

    /**
     * Lista tarefas de um dia específico.
     *
     * @param dia O dia a ser consultado.
     * @return Uma lista de tarefas do dia.
     */
    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return taskService.listarTarefasPorDia(dia);
    }

    /**
     * Lista tarefas críticas (alta prioridade).
     *
     * @return Uma lista de tarefas críticas.
     */
    public List<Tarefa> listarTarefasCriticas() {
        return taskService.listarTarefasCriticas();
    }

    /**
     * Cadastra um novo evento para o usuário logado.
     *
     * @param titulo    O título do evento.
     * @param descricao A descrição do evento.
     * @param deadline  A data do evento.
     * @throws BusinessException se houver erro no cadastro.
     */
    public void cadastrarEvento(String titulo, String descricao, LocalDate deadline) throws BusinessException {
        Evento novoEvento = itemFactory.criarEvento(titulo, descricao, usuarioLogado.getEmail(), deadline);
        eventService.cadastrarEvento(novoEvento);
    }

    /**
     * Lista todos os eventos do usuário logado.
     *
     * @return Uma lista de eventos.
     */
    public List<Evento> listarTodosEventos() {
        return eventService.listarTodosEventos();
    }

    /**
     * Exclui um evento.
     *
     * @param evento O evento a ser excluído.
     * @throws BusinessException se houver erro na exclusão.
     */
    public void excluirEvento(Evento evento) throws BusinessException {
        eventService.excluirEvento(evento);
    }

    /**
     * Edita um evento existente.
     *
     * @param eventoOriginal O evento original.
     * @param novoTitulo     O novo título.
     * @param novaDescricao  A nova descrição.
     * @param novoDeadline   A nova data.
     * @throws BusinessException se houver erro na edição.
     */
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline)
            throws BusinessException {
        eventService.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    /**
     * Lista eventos de um dia específico.
     *
     * @param dia O dia a ser consultado.
     * @return Uma lista de eventos do dia.
     */
    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return eventService.listarEventosPorDia(dia);
    }

    /**
     * Lista eventos de um mês específico.
     *
     * @param mes O mês a ser consultado.
     * @return Uma lista de eventos do mês.
     */
    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return eventService.listarEventosPorMes(mes);
    }

    /**
     * Envia um relatório de tarefas do dia por e-mail.
     *
     * @param dia O dia do relatório.
     * @return true se o e-mail foi enviado com sucesso.
     * @throws IllegalStateException se o usuário não estiver logado.
     */
    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia) {
        verificarUsuarioLogado();
        return reportService.enviarRelatorioTarefasDoDiaPorEmail(dia, usuarioLogado);
    }

    /**
     * Gera um relatório de tarefas do mês em um arquivo.
     *
     * @param mes         O mês do relatório.
     * @param nomeArquivo O nome do arquivo a ser gerado.
     * @throws IllegalStateException se o usuário não estiver logado.
     */
    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        verificarUsuarioLogado();
        reportService.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
    }

    /**
     * Gera um relatório em PDF das tarefas do dia (apenas salva o arquivo, sem
     * envio de e-mail).
     *
     * @param dia         O dia do relatório.
     * @param nomeArquivo O nome do arquivo a ser gerado.
     * @throws IllegalStateException se o usuário não estiver logado.
     */
    public void gerarRelatorioPDFTarefasDoDia(LocalDate dia, String nomeArquivo) {
        verificarUsuarioLogado();
        reportService.gerarRelatorioPDFTarefasDoDia(dia, nomeArquivo);
    }

    /**
     * Verifica se o usuário está logado e se os serviços foram inicializados.
     *
     * @throws IllegalStateException se o usuário não estiver logado ou serviços não
     *                               inicializados.
     */
    private void verificarUsuarioLogado() {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Você precisa estar logado para realizar esta operação.");
        }
        if (reportService == null || taskService == null || eventService == null) {
            throw new IllegalStateException(
                    "Serviços não foram inicializados corretamente. Tente fazer login novamente.");
        }
    }

    /**
     * Retorna o usuário atualmente logado.
     *
     * @return O objeto Usuario logado.
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
