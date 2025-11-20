package br.com.todolist.ui.TelasDialogo;

import br.com.todolist.controller.TaskController;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Diálogo modal para cadastro e edição de Tarefas.
 * Oferece um formulário para preencher título, descrição, prioridade e prazo da tarefa.
 */
public class DialogoTarefa extends JDialog {

    /** Controlador de tarefas para persistência dos dados. */
    private final TaskController taskController;

    /** Tarefa a ser editada (null se for cadastro). */
    private Tarefa tarefa;

    /** Campo de texto para o título da tarefa. */
    private JTextField campoTitulo;

    /** Campo de texto para a descrição da tarefa. */
    private JTextField campoDescricao;

    /** Campo de seleção numérica para a prioridade da tarefa. */
    private JSpinner campoPrioridade;

    /** Campo de texto para o prazo da tarefa. */
    private JTextField campoPrazo;

    /** Botão para salvar as alterações. */
    private JButton botaoSalvar;

    /** Botão para cancelar e fechar o diálogo. */
    private JButton botaoCancelar;

    /** Indica se a operação de salvar foi realizada com sucesso. */
    private boolean salvo = false;

    /** Formatador de data utilizado nos campos de texto (dd/MM/yyyy). */
    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor para criação de uma nova tarefa.
     *
     * @param frame          O frame pai da janela.
     * @param taskController O controlador de tarefas.
     */
    public DialogoTarefa(Frame frame, TaskController taskController) {
        super(frame, "Nova Tarefa", true);
        this.taskController = taskController;
        this.tarefa = null;
        configurarEAdicionarComponentes();
        configurarAcoes();
    }

    /**
     * Construtor para edição de uma tarefa existente.
     *
     * @param frame           O frame pai da janela.
     * @param taskController  O controlador de tarefas.
     * @param tarefaParaEditar A tarefa a ser editada.
     */
    public DialogoTarefa(Frame frame, TaskController taskController, Tarefa tarefaParaEditar) {
        super(frame, "Editar Tarefa", true);
        this.taskController = taskController;
        this.tarefa = tarefaParaEditar;
        configurarEAdicionarComponentes();
        preencherCampos();
        configurarAcoes();
    }

    /**
     * Inicializa e posiciona os componentes da interface gráfica.
     */
    private void configurarEAdicionarComponentes() {
        setTitle(tarefa == null ? "Nova Tarefa" : "Editar Tarefa");
        setSize(1280, 720);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel labelTitulo = new JLabel("Título:");
        labelTitulo.setBounds(400, 230, 100, 30);
        add(labelTitulo);

        campoTitulo = new JTextField();
        campoTitulo.setBounds(600, 230, 400 , 30);
        add(campoTitulo);

        JLabel labelDescricao = new JLabel("Descrição:");
        labelDescricao.setBounds(400, 275, 100, 30);
        add(labelDescricao);

        campoDescricao = new JTextField();
        campoDescricao.setBounds(600, 275, 400, 30);
        add(campoDescricao);

        JLabel labelPrioridade = new JLabel("Prioridade:");
        labelPrioridade.setBounds(400, 320, 100, 30);
        add(labelPrioridade);

        campoPrioridade = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        campoPrioridade.setBounds(600, 320, 70, 30);
        add(campoPrioridade);

        JLabel labelPrazo = new JLabel("Prazo (dd/MM/yyyy):");
        labelPrazo.setBounds(400, 365, 150, 30);
        add(labelPrazo);

        campoPrazo = new JTextField();
        campoPrazo.setBounds(600, 365, 400, 30);
        add(campoPrazo);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(600, 425, 120, 30);
        add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(750, 425, 120, 30);
        add(botaoCancelar);
    }
    
    /**
     * Preenche os campos do formulário com os dados da tarefa em edição.
     */
    private void preencherCampos() {
        if (tarefa != null) {
            campoTitulo.setText(tarefa.getTitulo());
            campoDescricao.setText(tarefa.getDescricao());
            campoPrioridade.setValue(tarefa.getPrioridade());
            campoPrazo.setText(tarefa.getDeadline().format(formatadorDeData));
        }
    }

    /**
     * Configura os listeners para os botões de ação.
     */
    private void configurarAcoes() {
        botaoCancelar.addActionListener(e -> dispose());
        botaoSalvar.addActionListener(e -> salvar());
    }

    /**
     * Valida os campos e salva a tarefa (nova ou editada).
     * Interage com o controlador para persistir os dados.
     */
    private void salvar() {
        try {
            String titulo = campoTitulo.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Título' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String descricao = campoDescricao.getText().trim();
            int prioridade = (int) campoPrioridade.getValue();
            LocalDate prazo = LocalDate.parse(campoPrazo.getText(), formatadorDeData);

            if (this.tarefa == null) {
                String user = SessionManager.getInstance().getUsuarioLogado().getEmail();
                Tarefa novaTarefa = new Tarefa(titulo, descricao, user, prazo, prioridade);
                taskController.cadastrarTarefa(novaTarefa);
            } else {
                taskController.editarTarefa(this.tarefa, titulo, descricao, prazo, prioridade);
            }

            this.salvo = true;
            dispose();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Indica se a operação de salvar foi concluída com sucesso.
     *
     * @return true se a tarefa foi salva, false caso contrário.
     */
    public boolean foiSalvo() {
        return this.salvo;
    }
}
