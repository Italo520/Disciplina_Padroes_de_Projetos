package br.com.todolist.ui.main;

import br.com.todolist.entity.Subtarefa;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Renderizador personalizado para células de lista de subtarefas.
 * Exibe cada subtarefa como um CheckBox, refletindo seu status (concluído ou não).
 */
public class SubtarefaCellRenderer extends JPanel implements ListCellRenderer<Subtarefa> {

    /** Componente CheckBox usado para renderizar o status da subtarefa. */
    private final JCheckBox checkBox;

    /**
     * Construtor padrão da classe SubtarefaCellRenderer.
     * Inicializa o layout e o componente CheckBox.
     */
    public SubtarefaCellRenderer() {
        setLayout(new BorderLayout());
        setOpaque(true);
        checkBox = new JCheckBox();
        checkBox.setOpaque(true);
        add(checkBox, BorderLayout.CENTER);
    }

    /**
     * Configura o componente para exibir a célula da lista.
     *
     * @param list         A lista que está sendo renderizada.
     * @param subtarefa    O objeto Subtarefa a ser exibido.
     * @param index        O índice da célula.
     * @param isSelected   Se a célula está selecionada.
     * @param cellHasFocus Se a célula tem o foco.
     * @return O componente configurado para exibição.
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Subtarefa> list,
                                                  Subtarefa subtarefa,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        checkBox.setText(subtarefa.getTitulo());
        checkBox.setSelected(subtarefa.isStatus());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            checkBox.setBackground(list.getSelectionBackground());
            checkBox.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            checkBox.setBackground(list.getBackground());
            checkBox.setForeground(list.getForeground());
        }

        return this;
    }
}
