package br.com.todolist.ui.main;

import br.com.todolist.controller.EventController;
import br.com.todolist.controller.TaskController;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.SessionManager;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * A tela principal da aplicação.
 * Contém as abas para gerenciamento de tarefas e eventos, além da barra de
 * ferramentas.
 * Exibe o nome do usuário logado no título.
 */
public class TelaPrincipal extends JFrame {

    /** Painel para exibição e gerenciamento de tarefas. */
    private PainelTarefas painelTarefas;

    /** Painel para exibição e gerenciamento de eventos. */
    private PainelEventos painelEventos;

    /** Componente de abas para alternar entre tarefas e eventos. */
    private JTabbedPane painelComAbas;

    /** Controlador de tarefas. */
    private transient TaskController taskController;

    /** Controlador de eventos. */
    private transient EventController eventController;

    /**
     * Construtor da classe TelaPrincipal.
     * Inicializa os controladores e configura a interface gráfica.
     */
    public TelaPrincipal() {
        super("Usuário: " + SessionManager.getInstance().getUsuarioLogado().getNome());

        SessionManager sessionManager = SessionManager.getInstance();
        this.taskController = new TaskController(sessionManager.getTaskService());
        this.eventController = new EventController(sessionManager.getEventService());

        configurarJanela();
        montarLayout();
    }

    /**
     * Configura as propriedades básicas da janela principal.
     */
    private void configurarJanela() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
    }

    /**
     * Monta o layout da tela, adicionando a barra de ferramentas e o painel de
     * abas.
     */
    private void montarLayout() {
        setJMenuBar(BarraFerramentas.criarBarraFerramentas(this, taskController, eventController));

        criarPaineis();
        painelComAbas.setBounds(5, 5, 1270, 650);

        add(painelComAbas);
    }

    /**
     * Inicializa os painéis de tarefas e eventos e os adiciona ao painel de abas.
     */
    private void criarPaineis() {
        painelComAbas = new JTabbedPane();

        this.painelTarefas = new PainelTarefas(taskController);
        this.painelEventos = new PainelEventos(eventController);

        painelComAbas.addTab("Tarefas", null, this.painelTarefas, "Gerenciador de Tarefas");
        painelComAbas.addTab("Eventos", null, this.painelEventos, "Gerenciador de Eventos");
    }

    /**
     * Exibe o formulário de tarefa (criação ou edição).
     *
     * @param tarefa A tarefa a ser editada, ou null para nova tarefa.
     */
    public void exibirFormularioTarefa(Tarefa tarefa) {
        PainelFormularioTarefa formulario = new PainelFormularioTarefa(this, taskController, tarefa);
        getContentPane().removeAll();
        getContentPane().add(formulario, java.awt.BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Volta para a visualização principal (abas de tarefas/eventos).
     * Atualiza a lista de tarefas.
     */
    public void voltarParaListaTarefas() {
        getContentPane().removeAll();
        getContentPane().add(painelComAbas, java.awt.BorderLayout.CENTER);
        painelTarefas.popularListaTarefas(); // Atualiza a lista
        revalidate();
        repaint();
    }

    /**
     * Exibe o formulário de evento (criação ou edição).
     *
     * @param evento O evento a ser editado, ou null para novo evento.
     */
    public void exibirFormularioEvento(Evento evento) {
        PainelFormularioEvento formulario = new PainelFormularioEvento(this, eventController, evento);
        getContentPane().removeAll();
        getContentPane().add(formulario, java.awt.BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Volta para a visualização principal (abas de tarefas/eventos).
     * Atualiza a lista de eventos.
     */
    public void voltarParaListaEventos() {
        getContentPane().removeAll();
        getContentPane().add(painelComAbas, java.awt.BorderLayout.CENTER);
        painelEventos.popularListaEventos();
        revalidate();
        repaint();
    }

    /**
     * Atualiza o painel de tarefas com uma lista específica de tarefas e seleciona
     * a aba de tarefas.
     * Útil para exibir resultados de filtros.
     *
     * @param tarefas A lista de tarefas a ser exibida.
     */
    public void atualizarPainelDeTarefas(List<Tarefa> tarefas) {
        painelComAbas.setSelectedComponent(painelTarefas);
        painelTarefas.exibirTarefasDoDia(tarefas);
    }

    /**
     * Atualiza o painel de eventos com uma lista específica de eventos e seleciona
     * a aba de eventos.
     * Útil para exibir resultados de filtros.
     *
     * @param eventos A lista de eventos a ser exibida.
     */
    public void atualizarPainelDeEventos(List<Evento> eventos) {
        painelComAbas.setSelectedComponent(painelEventos);
        painelEventos.exibirEventos(eventos);
    }
}
