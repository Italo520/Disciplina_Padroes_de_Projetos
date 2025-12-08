package br.com.todolist.ui.main;

import br.com.todolist.controller.TaskController;
import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.ui.dialogs.DialogoTarefa;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel responsável pela gestão visual das tarefas.
 * Exibe a lista de tarefas, detalhes da tarefa selecionada e suas subtarefas.
 */
public class PainelTarefas extends PainelBase {

    /** Controlador de tarefas para operações de negócio. */
    private final transient TaskController taskController;

    /** Modelo de lista para as tarefas exibidas. */
    private transient DefaultListModel<Tarefa> modeloListaTarefas;

    /** Modelo de lista para as subtarefas da tarefa selecionada. */
    private transient DefaultListModel<Subtarefa> modeloListaSubtarefas;

    /** Componente visual da lista de tarefas. */
    private JList<Tarefa> listaDeTarefas;

    /** Componente visual da lista de subtarefas. */
    private JList<Subtarefa> listaDeSubtarefas;

    /** Label para exibir a descrição da tarefa. */
    private JLabel valorDescricao;

    /** Label para exibir a prioridade da tarefa. */
    private JLabel valorPrioridade;

    /** Label para exibir o prazo da tarefa. */
    private JLabel valorPrazo;

    /** Label para exibir a conclusão da tarefa em percentual. */
    private JLabel valorConclusao;

    /** Formatador de data padrão para exibição (dd/MM/yyyy). */
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private final transient DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private static final String ERROR_PREFIX = "Erro: ";
    private static final String NO_TASK_SELECTED = "Nenhuma Tarefa Selecionada";

    /**
     * Construtor da classe PainelTarefas.
     *
     * @param taskController O controlador de tarefas.
     */
    public PainelTarefas(TaskController taskController) {
        super();
        this.taskController = taskController;
        super.inicializarLayout();
    }

    /**
     * Cria o painel de botões superior com as ações principais.
     *
     * @return O painel de botões.
     */
    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton botaoNovaTarefa = new JButton("Nova Tarefa");
        botaoNovaTarefa.addActionListener(new OuvinteBotaoNovaTarefa());

        JButton botaoEditarTarefa = new JButton("Editar Tarefa");
        botaoEditarTarefa.addActionListener(new OuvinteBotaoEditarTarefa());

        JButton botaoExcluirTarefa = new JButton("Excluir Tarefa");
        botaoExcluirTarefa.addActionListener(new OuvinteBotaoExcluirTarefa());

        painel.add(botaoNovaTarefa);
        painel.add(botaoEditarTarefa);
        painel.add(botaoExcluirTarefa);

        return painel;
    }

    /**
     * Cria o painel de conteúdo principal, contendo a lista de tarefas e o painel
     * de detalhes.
     *
     * @return O painel de conteúdo.
     */
    @Override
    protected JPanel criarPainelDeConteudo() {
        modeloListaTarefas = new DefaultListModel<>();
        listaDeTarefas = new JList<>(modeloListaTarefas);
        listaDeTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeTarefas.addListSelectionListener(new OuvinteSelecaoTarefa());
        JScrollPane scrollTarefas = new JScrollPane(listaDeTarefas);
        scrollTarefas.setBorder(BorderFactory.createTitledBorder("Tarefas"));

        JPanel painelDireito = criarPainelDireito();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTarefas, painelDireito);
        splitPane.setResizeWeight(0.5);

        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.add(splitPane, BorderLayout.CENTER);

        popularListaTarefas();
        return painelConteudo;
    }

    /**
     * Cria o painel direito que exibe os detalhes da tarefa e as subtarefas.
     *
     * @return O painel direito configurado.
     */
    private JPanel criarPainelDireito() {
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Subtarefas e Detalhes"));

        painelDireito.add(criarPainelDetalhes(), BorderLayout.NORTH);
        painelDireito.add(criarPainelSubtarefas(), BorderLayout.CENTER);

        return painelDireito;
    }

    /**
     * Cria o painel de detalhes da tarefa selecionada.
     *
     * @return O painel de detalhes.
     */
    private JPanel criarPainelDetalhes() {
        JPanel painelDetalhes = new JPanel(new GridLayout(0, 2, 5, 5));
        painelDetalhes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        valorDescricao = new JLabel("N/D");
        valorPrioridade = new JLabel("N/D");
        valorPrazo = new JLabel("N/D");
        valorConclusao = new JLabel("N/D");

        painelDetalhes.add(new JLabel("Descrição:"));
        painelDetalhes.add(valorDescricao);
        painelDetalhes.add(new JLabel("Prioridade:"));
        painelDetalhes.add(valorPrioridade);
        painelDetalhes.add(new JLabel("Prazo:"));
        painelDetalhes.add(valorPrazo);
        painelDetalhes.add(new JLabel("Conclusão:"));
        painelDetalhes.add(valorConclusao);

        return painelDetalhes;
    }

    /**
     * Cria o painel de subtarefas.
     *
     * @return O painel de subtarefas.
     */
    private JPanel criarPainelSubtarefas() {
        JPanel painelSubtarefas = new JPanel(new BorderLayout());
        modeloListaSubtarefas = new DefaultListModel<>();
        listaDeSubtarefas = new JList<>(modeloListaSubtarefas);
        listaDeSubtarefas.setCellRenderer(new SubtarefaCellRenderer());
        listaDeSubtarefas.addMouseListener(new OuvinteCliqueSubtarefa());

        JPanel painelBotoesSubtarefa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoNovaSubtarefa = new JButton("Nova Subtarefa");
        botaoNovaSubtarefa.addActionListener(new OuvinteBotaoNovaSubtarefa());
        JButton botaoEditarSubtarefa = new JButton("Editar Subtarefa");
        botaoEditarSubtarefa.addActionListener(new OuvinteBotaoEditarSubtarefa());
        JButton botaoExcluirSubtarefa = new JButton("Excluir Subtarefa");
        botaoExcluirSubtarefa.addActionListener(new OuvinteBotaoExcluirSubtarefa());

        painelBotoesSubtarefa.add(botaoNovaSubtarefa);
        painelBotoesSubtarefa.add(botaoEditarSubtarefa);
        painelBotoesSubtarefa.add(botaoExcluirSubtarefa);

        painelSubtarefas.add(new JScrollPane(listaDeSubtarefas), BorderLayout.CENTER);
        painelSubtarefas.add(painelBotoesSubtarefa, BorderLayout.SOUTH);

        return painelSubtarefas;
    }

    /**
     * Atualiza os campos de detalhes com as informações da tarefa selecionada.
     *
     * @param tarefa A tarefa selecionada, ou null para limpar os campos.
     */
    private void atualizarDetalhesTarefa(Tarefa tarefa) {
        if (tarefa != null) {
            valorDescricao.setText(tarefa.getDescricao());
            valorPrioridade.setText(String.valueOf(tarefa.getPrioridade()));
            valorPrazo.setText(tarefa.getDeadline().format(formatadorDeData));
            valorConclusao.setText((int) tarefa.obterPercentual() + "%");
        } else {
            valorDescricao.setText("Selecione uma tarefa");
            valorPrioridade.setText("-");
            valorPrazo.setText("-");
            valorConclusao.setText("-");
        }
    }

    /**
     * Recarrega a lista de tarefas a partir do controlador.
     */
    private void popularListaTarefas() {
        modeloListaTarefas.clear();
        try {
            List<Tarefa> tarefas = taskController.listarTodasTarefas();
            if (tarefas != null) {
                tarefas.forEach(modeloListaTarefas::addElement);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar tarefas: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
        atualizarListaSubtarefas(null);
        atualizarDetalhesTarefa(null);
    }

    /**
     * Atualiza a lista de subtarefas com base na tarefa selecionada.
     *
     * @param tarefa A tarefa selecionada.
     */
    private void atualizarListaSubtarefas(Tarefa tarefa) {
        modeloListaSubtarefas.clear();
        if (tarefa != null && tarefa.getSubtarefas() != null) {
            tarefa.getSubtarefas().forEach(modeloListaSubtarefas::addElement);
        }
    }

    /**
     * Filtra a lista para exibir apenas as tarefas de um dia específico.
     *
     * @param tarefasDoDia A lista de tarefas a serem exibidas.
     */
    public void exibirTarefasDoDia(List<Tarefa> tarefasDoDia) {
        modeloListaTarefas.clear();
        if (tarefasDoDia != null) {
            tarefasDoDia.forEach(modeloListaTarefas::addElement);
        }
        atualizarListaSubtarefas(null);
        atualizarDetalhesTarefa(null);
    }

    // OUVINTES

    /**
     * Listener para o botão "Nova Tarefa".
     */
    private class OuvinteBotaoNovaTarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DialogoTarefa dialogo = new DialogoTarefa((Frame) SwingUtilities.getWindowAncestor(PainelTarefas.this),
                    taskController);
            dialogo.setVisible(true);
            if (dialogo.foiSalvo()) {
                popularListaTarefas();
            }
        }
    }

    /**
     * Listener para o botão "Editar Tarefa".
     */
    private class OuvinteBotaoEditarTarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
            if (tarefaSelecionada == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Por favor, selecione uma tarefa para editar.",
                        NO_TASK_SELECTED, JOptionPane.WARNING_MESSAGE);
                return;
            }
            DialogoTarefa dialogo = new DialogoTarefa((Frame) SwingUtilities.getWindowAncestor(PainelTarefas.this),
                    taskController, tarefaSelecionada);
            dialogo.setVisible(true);
            if (dialogo.foiSalvo()) {
                popularListaTarefas();
            }
        }
    }

    /**
     * Listener para o botão "Excluir Tarefa".
     */
    private class OuvinteBotaoExcluirTarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
            if (tarefaSelecionada == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Por favor, selecione uma tarefa para excluir.",
                        NO_TASK_SELECTED, JOptionPane.WARNING_MESSAGE);
                return;
            }
            int resposta = JOptionPane.showConfirmDialog(PainelTarefas.this,
                    "Tem certeza que deseja excluir a tarefa:\n" + tarefaSelecionada.getTitulo(),
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                try {
                    taskController.excluirTarefa(tarefaSelecionada);
                    popularListaTarefas();
                } catch (BusinessException ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, "Erro ao excluir tarefa: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Listener para mudanças na seleção da lista de tarefas.
     */
    private class OuvinteSelecaoTarefa implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
                atualizarListaSubtarefas(tarefaSelecionada);
                atualizarDetalhesTarefa(tarefaSelecionada);
            }
        }
    }

    /**
     * Listener para cliques na lista de subtarefas (para alternar status).
     */
    private class OuvinteCliqueSubtarefa extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int index = listaDeSubtarefas.locationToIndex(e.getPoint());
            if (index != -1) {
                Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
                Subtarefa subtarefa = modeloListaSubtarefas.getElementAt(index);
                subtarefa.mudarStatus();
                if (tarefaPai != null) {
                    try {
                        Tarefa tarefaAtualizada = taskController.atualizarTarefa(tarefaPai);

                        // Atualiza a referência na lista de tarefas
                        int indexTarefa = modeloListaTarefas.indexOf(tarefaPai);
                        if (indexTarefa != -1) {
                            modeloListaTarefas.set(indexTarefa, tarefaAtualizada);
                            listaDeTarefas.setSelectedValue(tarefaAtualizada, false);
                        }

                        atualizarDetalhesTarefa(tarefaAtualizada);
                        // Não precisamos repintar manualmente se atualizarmos o modelo
                        // listaDeSubtarefas.repaint(listaDeSubtarefas.getCellBounds(index, index));
                        // listaDeTarefas.repaint();
                    } catch (BusinessException ex) {
                        JOptionPane.showMessageDialog(PainelTarefas.this, ex.getMessage(), "Erro ao Atualizar",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(PainelTarefas.this, ERROR_PREFIX + ex.getMessage(), "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Listener para o botão "Nova Subtarefa".
     */
    private class OuvinteBotaoNovaSubtarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
            if (tarefaPai == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this,
                        "Selecione uma tarefa principal para adicionar uma subtarefa.", NO_TASK_SELECTED,
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String descricao = JOptionPane.showInputDialog(PainelTarefas.this, "Descrição da nova subtarefa:",
                    "Nova Subtarefa", JOptionPane.PLAIN_MESSAGE);
            if (descricao != null && !descricao.trim().isEmpty()) {
                tarefaPai.adicionarSubtarefa(new Subtarefa(descricao));
                try {
                    Tarefa tarefaAtualizada = taskController.atualizarTarefa(tarefaPai);

                    // Atualiza a referência na lista de tarefas
                    int indexTarefa = modeloListaTarefas.indexOf(tarefaPai);
                    if (indexTarefa != -1) {
                        modeloListaTarefas.set(indexTarefa, tarefaAtualizada);
                        listaDeTarefas.setSelectedValue(tarefaAtualizada, false);
                    }

                    atualizarListaSubtarefas(tarefaAtualizada);
                    atualizarDetalhesTarefa(tarefaAtualizada);
                } catch (BusinessException ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, ERROR_PREFIX + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Listener para o botão "Editar Subtarefa".
     */
    private class OuvinteBotaoEditarSubtarefa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
            Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
            if (tarefaPai == null || subtarefa == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Selecione uma subtarefa para editar.",
                        "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String novoTitulo = (String) JOptionPane.showInputDialog(PainelTarefas.this, "Nova descrição:",
                    "Editar Subtarefa", JOptionPane.PLAIN_MESSAGE, null, null, subtarefa.getTitulo());
            if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
                subtarefa.setTitulo(novoTitulo);
                try {
                    Tarefa tarefaAtualizada = taskController.atualizarTarefa(tarefaPai);

                    // Atualiza a referência na lista de tarefas
                    int indexTarefa = modeloListaTarefas.indexOf(tarefaPai);
                    if (indexTarefa != -1) {
                        modeloListaTarefas.set(indexTarefa, tarefaAtualizada);
                        listaDeTarefas.setSelectedValue(tarefaAtualizada, false);
                    }

                    atualizarListaSubtarefas(tarefaAtualizada);
                } catch (BusinessException ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, ERROR_PREFIX + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Listener para o botão "Excluir Subtarefa".
     */
    private class OuvinteBotaoExcluirSubtarefa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
            Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
            if (tarefaPai == null || subtarefa == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Selecione uma subtarefa para excluir.",
                        "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int resposta = JOptionPane.showConfirmDialog(PainelTarefas.this,
                    "Excluir a subtarefa '" + subtarefa.getTitulo() + "'?", "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                tarefaPai.removerSubtarefa(subtarefa);
                try {
                    Tarefa tarefaAtualizada = taskController.atualizarTarefa(tarefaPai);

                    // Atualiza a referência na lista de tarefas
                    int indexTarefa = modeloListaTarefas.indexOf(tarefaPai);
                    if (indexTarefa != -1) {
                        modeloListaTarefas.set(indexTarefa, tarefaAtualizada);
                        listaDeTarefas.setSelectedValue(tarefaAtualizada, false);
                    }

                    atualizarListaSubtarefas(tarefaAtualizada);
                    atualizarDetalhesTarefa(tarefaAtualizada);
                } catch (BusinessException ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PainelTarefas.this, ERROR_PREFIX + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
