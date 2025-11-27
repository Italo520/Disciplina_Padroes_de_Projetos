package br.com.todolist.ui.main;

import br.com.todolist.controller.EventController;
import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.ui.dialogs.DialogoEvento;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Painel responsável pela gestão visual dos eventos.
 * Exibe a lista de eventos e seus detalhes, incluindo o tempo restante para o
 * prazo.
 */
public class PainelEventos extends PainelBase {

    /** Controlador responsável pelas operações de eventos. */
    private final transient EventController eventController;

    /** Modelo de lista para armazenar os eventos exibidos. */
    private transient DefaultListModel<Evento> modeloListaEventos;

    /** Componente gráfico de lista para exibir os eventos. */
    private JList<Evento> listaDeEventos;

    /** Label para exibir a descrição do evento selecionado. */
    private JLabel valorDescricao;

    /** Label para exibir o tempo restante até o evento. */
    private JLabel valorTempoRestante;

    /**
     * Construtor da classe PainelEventos.
     *
     * @param eventController O controlador de eventos.
     */
    public PainelEventos(EventController eventController) {
        this.eventController = eventController;
        inicializarLayout();
    }

    /**
     * Cria o painel de botões superior com as ações principais para eventos.
     *
     * @return O painel de botões.
     */
    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoNovoEvento = new JButton("Novo Evento");
        JButton botaoEditarEvento = new JButton("Editar Evento");
        JButton botaoExcluirEvento = new JButton("Excluir Evento");

        botaoNovoEvento.addActionListener(new OuvinteBotaoNovoEvento());
        botaoEditarEvento.addActionListener(new OuvinteBotaoEditarEvento());
        botaoExcluirEvento.addActionListener(new OuvinteBotaoExcluirEvento());

        painel.add(botaoNovoEvento);
        painel.add(botaoEditarEvento);
        painel.add(botaoExcluirEvento);

        return painel;
    }

    /**
     * Cria o painel de conteúdo principal, contendo a lista de eventos e os
     * detalhes.
     *
     * @return O painel de conteúdo.
     */
    @Override
    protected JPanel criarPainelDeConteudo() {
        modeloListaEventos = new DefaultListModel<>();
        listaDeEventos = new JList<>(modeloListaEventos);
        listaDeEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeEventos.setBorder(BorderFactory.createTitledBorder("Eventos"));
        listaDeEventos.addListSelectionListener(new OuvinteSelecaoEvento());

        JPanel painelDetalhes = new JPanel(new BorderLayout());
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes do Evento"));

        JPanel painelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        valorDescricao = new JLabel();
        valorTempoRestante = new JLabel();

        painelCampos.add(new JLabel("Descrição:"));
        painelCampos.add(valorDescricao);
        painelCampos.add(new JLabel("Tempo Restante:"));
        painelCampos.add(valorTempoRestante);

        painelDetalhes.add(painelCampos, BorderLayout.NORTH);

        popularListaEventos();

        JSplitPane painelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaDeEventos),
                painelDetalhes);
        painelDividido.setDividerLocation(300);

        JPanel painelDeConteudo = new JPanel(new BorderLayout());
        painelDeConteudo.add(painelDividido, BorderLayout.CENTER);

        return painelDeConteudo;
    }

    /**
     * Atualiza os campos de detalhes com as informações do evento selecionado.
     * Calcula e exibe o tempo restante para o evento.
     *
     * @param evento O evento selecionado, ou null para limpar os campos.
     */
    private void atualizarDetalhesEvento(Evento evento) {
        if (evento != null) {
            valorDescricao.setText("<html>" + evento.getDescricao() + "</html>");

            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), evento.getDeadline());
            String textoTempo;
            if (diasRestantes > 1) {
                textoTempo = "Faltam " + diasRestantes + " dias";
            } else if (diasRestantes == 1) {
                textoTempo = "Falta 1 dia";
            } else if (diasRestantes == 0) {
                textoTempo = "É hoje!";
            } else {
                textoTempo = "Atrasado";
            }
            valorTempoRestante.setText(textoTempo);
        } else {
            valorDescricao.setText("Selecione um evento");
            valorTempoRestante.setText("-");
        }
    }

    /**
     * Recarrega a lista de eventos a partir do controlador.
     */
    private void popularListaEventos() {
        modeloListaEventos.clear();
        try {
            for (Evento evento : this.eventController.listarTodosEventos()) {
                modeloListaEventos.addElement(evento);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar eventos: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
        atualizarDetalhesEvento(null);
    }

    /**
     * Atualiza a lista com um conjunto específico de eventos (usado para filtros).
     *
     * @param eventos A lista de eventos a ser exibida.
     */
    public void exibirEventos(List<Evento> eventos) {
        modeloListaEventos.clear();
        if (eventos != null) {
            eventos.forEach(modeloListaEventos::addElement);
        }
        atualizarDetalhesEvento(null);
    }

    // OUVINTES

    /**
     * Listener para o botão "Novo Evento".
     */
    private class OuvinteBotaoNovoEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(PainelEventos.this);
            DialogoEvento dialogo = new DialogoEvento(framePrincipal, eventController);
            dialogo.setVisible(true);

            if (dialogo.foiSalvo()) {
                popularListaEventos();
            }
        }
    }

    /**
     * Listener para o botão "Editar Evento".
     */
    private class OuvinteBotaoEditarEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Evento eventoSelecionado = listaDeEventos.getSelectedValue();
            if (eventoSelecionado == null) {
                JOptionPane.showMessageDialog(PainelEventos.this, "Selecione um evento para editar.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(PainelEventos.this);
            DialogoEvento dialogo = new DialogoEvento(framePrincipal, eventController, eventoSelecionado);
            dialogo.setVisible(true);

            if (dialogo.foiSalvo()) {
                popularListaEventos();
            }
        }
    }

    /**
     * Listener para o botão "Excluir Evento".
     */
    private class OuvinteBotaoExcluirEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Evento eventoSelecionado = listaDeEventos.getSelectedValue();
            if (eventoSelecionado == null) {
                JOptionPane.showMessageDialog(PainelEventos.this, "Selecione um evento para excluir.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(PainelEventos.this,
                    "Tem certeza que deseja excluir o evento '" + eventoSelecionado.getTitulo() + "'?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    eventController.excluirEvento(eventoSelecionado);
                    popularListaEventos();
                } catch (BusinessException ex) {
                    JOptionPane.showMessageDialog(PainelEventos.this, ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PainelEventos.this, "Erro: " + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Listener para mudanças na seleção da lista de eventos.
     */
    private class OuvinteSelecaoEvento implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Evento eventoSelecionado = listaDeEventos.getSelectedValue();
                atualizarDetalhesEvento(eventoSelecionado);
            }
        }
    }
}
