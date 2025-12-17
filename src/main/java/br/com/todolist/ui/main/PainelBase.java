package br.com.todolist.ui.main;

import java.awt.Dimension;
import javax.swing.JPanel;

public abstract class PainelBase extends JPanel {

    protected PainelBase() {
        super();
        setLayout(null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1265, 630);
    }

    protected final void inicializarLayout() {

        JPanel painelDeBotoes = criarPainelDeBotoes();
        JPanel painelDeConteudo = criarPainelDeConteudo();

        painelDeBotoes.setBounds(0, 0, 1270, 50);
        painelDeConteudo.setBounds(0, 50, 1260, 560);

        add(painelDeBotoes);
        add(painelDeConteudo);
    }

    protected abstract JPanel criarPainelDeBotoes();

    protected abstract JPanel criarPainelDeConteudo();
}