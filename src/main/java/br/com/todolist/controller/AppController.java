package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.EventoRepositoryImpl;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.repository.TarefaRepositoryImpl;
import br.com.todolist.service.IEventService;
import br.com.todolist.service.impl.EventServiceImpl;
import br.com.todolist.service.IReportService;
import br.com.todolist.service.impl.ReportServiceImpl;
import br.com.todolist.service.ITaskService;
import br.com.todolist.service.util.IItemFactory;
import br.com.todolist.service.impl.TaskServiceImpl;
import br.com.todolist.service.IUserService;
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
    private final IUserService userService;
    private final Mensageiro mensageiro;
    private final IItemFactory itemFactory;
    private ITaskService taskService;
    private IEventService eventService;
    private IReportService reportService;
    private Usuario usuarioLogado;

    private AppController(IUserService userService, Mensageiro mensageiro, IItemFactory itemFactory) {
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
    public static void init(IUserService userService, Mensageiro mensageiro, IItemFactory itemFactory) {
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

    /**
     * Realiza o login do usuário e inicializa os serviços dependentes do usuário.
     *
     * @param email    O e-mail do usuário.
     * @param password A senha do usuário.
     * @return true se o login for bem-sucedido, false caso contrário.
     */
    public boolean login(String email, String password) {
        usuarioLogado = userService.autenticarUsuario(email, password);
        if (usuarioLogado != null) {
            ITarefaRepository tarefaRepository = new TarefaRepositoryImpl();
            IEventoRepository eventoRepository = new EventoRepositoryImpl();
            this.taskService = new TaskServiceImpl(tarefaRepository, usuarioLogado.getEmail());
            this.eventService = new EventServiceImpl(eventoRepository, usuarioLogado.getEmail());
            this.reportService = new ReportServiceImpl(taskService, mensageiro);
            return true;
        }
        return false;
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param nome     O nome do usuário.
     * @param email    O e-mail do usuário.
     * @param password A senha do usuário.
     * @return true se o cadastro for realizado com sucesso, false caso contrário.
     */
    public boolean cadastrarUsuario(String nome, String email, String password) {
        return userService.criarNovoUsuario(nome, email, password);
    }

    /**
     * Cadastra uma nova tarefa para o usuário logado.
     *
     * @param titulo     O título da tarefa.
     * @param descricao  A descrição da tarefa.
     * @param deadline   O prazo da tarefa.
     * @param prioridade A prioridade da tarefa.
     */
    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
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
     */
    public void excluirTarefa(Tarefa tarefa) {
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
     */
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade) {
        taskService.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
    }

    /**
     * Atualiza uma tarefa (ex: marca como concluída).
     *
     * @param tarefa A tarefa a ser atualizada.
     */
    public void atualizarTarefa(Tarefa tarefa) {
        taskService.atualizarTarefa(tarefa);
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
     * @return true se o evento foi cadastrado com sucesso, false caso contrário.
     */
    public boolean cadastrarEvento(String titulo, String descricao, LocalDate deadline) {
        Evento novoEvento = itemFactory.criarEvento(titulo, descricao, usuarioLogado.getEmail(), deadline);
        return eventService.cadastrarEvento(novoEvento);
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
     */
    public void excluirEvento(Evento evento) {
        eventService.excluirEvento(evento);
    }

    /**
     * Edita um evento existente.
     *
     * @param eventoOriginal O evento original.
     * @param novoTitulo     O novo título.
     * @param novaDescricao  A nova descrição.
     * @param novoDeadline   A nova data.
     */
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
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
     */
    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia) {
        return reportService.enviarRelatorioTarefasDoDiaPorEmail(dia, usuarioLogado);
    }

    /**
     * Gera um relatório de tarefas do mês em um arquivo.
     *
     * @param mes         O mês do relatório.
     * @param nomeArquivo O nome do arquivo a ser gerado.
     */
    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        reportService.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
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
