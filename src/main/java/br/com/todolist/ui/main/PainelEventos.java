package br.com.todolist.ui.main;

import br.com.todolist.controller.EventController;
import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PainelEventos extends PainelBase {
    private final transient EventController eventController;
    private transient DefaultListModel<Evento> modeloListaEventos;
    private JList<Evento> listaDeEventos;
    private JLabel valorDescricao;
    private JLabel valorTempoRestante;

    public PainelEventos(EventController eventController) {
        this.eventController = eventController;
        inicializarLayout();
    }

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

    public void popularListaEventos() {
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

    public void exibirEventos(List<Evento> eventos) {
        modeloListaEventos.clear();
        if (eventos != null) {
            eventos.forEach(modeloListaEventos::addElement);
        }

        atualizarDetalhesEvento(null);
    }

    private class OuvinteBotaoNovoEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) SwingUtilities.getWindowAncestor(PainelEventos.this);
            if (telaPrincipal != null) {
                telaPrincipal.exibirFormularioEvento(null);
            }
        }
    }

    private class OuvinteBotaoEditarEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Evento eventoSelecionado = listaDeEventos.getSelectedValue();
            if (eventoSelecionado == null) {
                JOptionPane.showMessageDialog(PainelEventos.this, "Selecione um evento para editar.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            TelaPrincipal telaPrincipal = (TelaPrincipal) SwingUtilities.getWindowAncestor(PainelEventos.this);
            if (telaPrincipal != null) {
                telaPrincipal.exibirFormularioEvento(eventoSelecionado);
            }
        }
    }

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

    private class OuvinteSelecaoEvento implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Evento eventoSelecionado = listaDeEventos.getSelectedValue();
                atualizarDetalhesEvento(eventoSelecionado);
            }
        }
    }
}