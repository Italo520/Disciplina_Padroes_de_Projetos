package br.com.todolist.ui.main;

import br.com.todolist.controller.EventController;
import br.com.todolist.controller.TaskController;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.SessionManager;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class TelaPrincipal extends JFrame {

    private PainelTarefas painelTarefas;

    private PainelEventos painelEventos;

    private JTabbedPane painelComAbas;

    private transient TaskController taskController;

    private transient EventController eventController;

    public TelaPrincipal() {
        super("Usuário: " + SessionManager.getInstance().getUsuarioLogado().getNome());

        SessionManager sessionManager = SessionManager.getInstance();
        this.taskController = new TaskController(sessionManager.getTaskService());
        this.eventController = new EventController(sessionManager.getEventService());

        configurarJanela();
        montarLayout();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
    }

    private void montarLayout() {
        setJMenuBar(BarraFerramentas.criarBarraFerramentas(this, taskController, eventController));

        criarPaineis();
        painelComAbas.setBounds(5, 5, 1270, 650);

        add(painelComAbas);
    }

    private void criarPaineis() {
        painelComAbas = new JTabbedPane();

        this.painelTarefas = new PainelTarefas(taskController);
        this.painelEventos = new PainelEventos(eventController);

        painelComAbas.addTab("Tarefas", null, this.painelTarefas, "Gerenciador de Tarefas");
        painelComAbas.addTab("Eventos", null, this.painelEventos, "Gerenciador de Eventos");
    }

    public void exibirFormularioTarefa(Tarefa tarefa) {
        PainelFormularioTarefa formulario = new PainelFormularioTarefa(this, taskController, tarefa);
        getContentPane().removeAll();
        getContentPane().add(formulario, java.awt.BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void voltarParaListaTarefas() {
        getContentPane().removeAll();
        getContentPane().add(painelComAbas, java.awt.BorderLayout.CENTER);
        painelTarefas.popularListaTarefas(); 
        revalidate();
        repaint();
    }

    public void exibirFormularioEvento(Evento evento) {
        PainelFormularioEvento formulario = new PainelFormularioEvento(this, eventController, evento);
        getContentPane().removeAll();
        getContentPane().add(formulario, java.awt.BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void voltarParaListaEventos() {
        getContentPane().removeAll();
        getContentPane().add(painelComAbas, java.awt.BorderLayout.CENTER);
        painelEventos.popularListaEventos();
        revalidate();
        repaint();
    }

    public void atualizarPainelDeTarefas(List<Tarefa> tarefas) {
        painelComAbas.setSelectedComponent(painelTarefas);
        painelTarefas.exibirTarefasDoDia(tarefas);
    }

    public void atualizarPainelDeEventos(List<Evento> eventos) {
        painelComAbas.setSelectedComponent(painelEventos);
        painelEventos.exibirEventos(eventos);
    }
}