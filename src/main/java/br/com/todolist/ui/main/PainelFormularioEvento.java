package br.com.todolist.ui.main;

import br.com.todolist.controller.EventController;
import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Painel para cadastro e edição de Eventos.
 * Oferece um formulário para preencher título, descrição e data do evento.
 */
public class PainelFormularioEvento extends JPanel {

    /** Referência para a tela principal para navegação. */
    private final TelaPrincipal telaPrincipal;

    /** Controlador de eventos para persistência dos dados. */
    private final transient EventController eventController;

    /** Evento a ser editado (null se for cadastro). */
    private transient Evento evento;

    /** Campo de texto para o título do evento. */
    private JTextField campoTitulo;

    /** Campo de texto para a descrição do evento. */
    private JTextField campoDescricao;

    /** Campo de texto para a data do evento. */
    private JTextField campoData;

    /** Botão para salvar as alterações. */
    private JButton botaoSalvar;

    /** Botão para cancelar e voltar. */
    private JButton botaoCancelar;

    /** Formatador de data utilizado nos campos de texto (dd/MM/yyyy). */
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private final transient DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Construtor do painel de formulário de evento.
     *
     * @param telaPrincipal    A tela principal para navegação.
     * @param eventController  O controlador de eventos.
     * @param eventoParaEditar O evento a ser editado (null para novo evento).
     */
    public PainelFormularioEvento(TelaPrincipal telaPrincipal, EventController eventController,
            Evento eventoParaEditar) {
        this.telaPrincipal = telaPrincipal;
        this.eventController = eventController;
        this.evento = eventoParaEditar;
        configurarEAdicionarComponentes();
        preencherCampos();
        configurarAcoes();
    }

    /**
     * Inicializa e posiciona os componentes da interface gráfica.
     */
    private void configurarEAdicionarComponentes() {
        setLayout(null);
        setSize(1280, 720);

        JLabel labelTituloPagina = new JLabel(evento == null ? "Novo Evento" : "Editar Evento");
        labelTituloPagina.setFont(new Font("Arial", Font.BOLD, 24));
        labelTituloPagina.setBounds(50, 30, 300, 40);
        add(labelTituloPagina);

        JLabel labelTitulo = new JLabel("Título:");
        labelTitulo.setBounds(400, 230, 100, 30);
        add(labelTitulo);

        campoTitulo = new JTextField();
        campoTitulo.setBounds(600, 230, 400, 30);
        add(campoTitulo);

        JLabel labelDescricao = new JLabel("Descrição:");
        labelDescricao.setBounds(400, 275, 100, 30);
        add(labelDescricao);

        campoDescricao = new JTextField();
        campoDescricao.setBounds(600, 275, 400, 30);
        add(campoDescricao);

        JLabel labelPrazo = new JLabel("Deadline (dd/MM/yyyy):");
        labelPrazo.setBounds(400, 320, 150, 30);
        add(labelPrazo);

        campoData = new JTextField();
        campoData.setBounds(600, 320, 400, 30);
        add(campoData);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(600, 425, 120, 30);
        add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(750, 425, 120, 30);
        add(botaoCancelar);
    }

    /**
     * Preenche os campos do formulário com os dados do evento em edição.
     */
    private void preencherCampos() {
        if (evento != null) {
            campoTitulo.setText(evento.getTitulo());
            campoDescricao.setText(evento.getDescricao());
            campoData.setText(evento.getDeadline().format(formatadorDeData));
        }
    }

    /**
     * Configura os listeners para os botões de ação.
     */
    private void configurarAcoes() {
        botaoCancelar.addActionListener(e -> telaPrincipal.voltarParaListaEventos());
        botaoSalvar.addActionListener(e -> salvar());
    }

    /**
     * Valida os campos e salva o evento (novo ou editado).
     * Interage com o controlador para persistir os dados.
     */
    private void salvar() {
        if (!validarCampos()) {
            return;
        }

        String titulo = campoTitulo.getText();
        String descricao = campoDescricao.getText();
        LocalDate deadline = LocalDate.parse(campoData.getText(), formatadorDeData);

        try {
            if (this.evento == null) {
                String user = SessionManager.getInstance().getUsuarioLogado().getEmail();
                Evento novoEvento = new Evento(titulo, descricao, user, deadline);
                eventController.cadastrarEvento(novoEvento);
            } else {
                eventController.editarEvento(this.evento, titulo, descricao, deadline);
            }

            JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            telaPrincipal.voltarParaListaEventos();

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao Salvar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Valida se os campos obrigatórios foram preenchidos corretamente.
     *
     * @return true se os dados forem válidos, false caso contrário.
     */
    private boolean validarCampos() {
        if (campoTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo 'Título' é obrigatório.", "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(campoData.getText(), formatadorDeData);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "O formato da data é inválido. Use " + DATE_PATTERN + ".",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
