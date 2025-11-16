package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.ItemRepository;
import br.com.todolist.repository.ItemRepositoryImpl;
import br.com.todolist.repository.UserRepository;
import br.com.todolist.repository.UserRepositoryImpl;
import br.com.todolist.service.EventService;
import br.com.todolist.service.EventServiceImpl;
import br.com.todolist.service.TaskService;
import br.com.todolist.service.TaskServiceImpl;
import br.com.todolist.service.UserService;
import br.com.todolist.service.UserServiceImpl;
import br.com.todolist.util.Central;
import br.com.todolist.util.Mensageiro;
import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AppController {

    private static AppController instance;
    private final UserService userService;
    private TaskService taskService;
    private EventService eventService;
    private Usuario usuarioLogado;
    private final Mensageiro mensageiro;

    private AppController() {
        UserRepository userRepository = new UserRepositoryImpl();
        this.userService = new UserServiceImpl(userRepository);
        this.mensageiro = new Mensageiro();
    }

    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    public boolean login(String email, String password) {
        usuarioLogado = userService.autenticarUsuario(email, password);
        if (usuarioLogado != null) {
            ItemRepository<Itens> itemRepository = new ItemRepositoryImpl();
            this.taskService = new TaskServiceImpl(itemRepository, usuarioLogado.getEmail());
            this.eventService = new EventServiceImpl(itemRepository, usuarioLogado.getEmail());
            return true;
        }
        return false;
    }

    public boolean cadastrarUsuario(String nome, String email, String password) {
        return userService.criarNovoUsuario(nome, email, password);
    }

    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Tarefa novaTarefa = new Tarefa(titulo, descricao, usuarioLogado.getEmail(), deadline, prioridade);
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
        Evento novoEvento = new Evento(titulo, descricao, usuarioLogado.getEmail(), deadline);
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
        List<Tarefa> tarefas = taskService.listarTarefasPorDia(dia);
        if (tarefas == null || tarefas.isEmpty()) {
            return false;
        }
        String nomeArquivo = "Relatorio_Tarefas_" + dia.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] cabecalhos = {"Título", "Descrição", "Prioridade"};
        List<String[]> dados = tarefas.stream()
                .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade())})
                .collect(Collectors.toList());
        Central.gerarPdf(nomeArquivo, tituloRelatorio, cabecalhos, dados);
        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n\nAtenciosamente,\nSistema ToDoList.";
        boolean sucesso = mensageiro.enviarEmailComAnexo(usuarioLogado.getEmail(), assunto, corpo, nomeArquivo);
        new File(nomeArquivo).delete();
        return sucesso;
    }

    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        List<Tarefa> tarefasDoMes = taskService.listarTodasTarefas().stream()
                .filter(t -> YearMonth.from(t.getDeadline()).equals(mes))
                .collect(Collectors.toList());
        String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo", "Conclusão (%)"};
        List<String[]> dados = tarefasDoMes.stream()
                .map(t -> new String[]{
                        t.getTitulo(),
                        t.getDescricao(),
                        String.valueOf(t.getPrioridade()),
                        t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                })
                .collect(Collectors.toList());
        List<String> colunaExtra = tarefasDoMes.stream()
                .map(t -> String.format("%.0f%%", t.obterPercentual()))
                .collect(Collectors.toList());
        String nomePlanilha = "Tarefas de " + mes.format(DateTimeFormatter.ofPattern("MM-yyyy"));
        Central.gerarExcel(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}