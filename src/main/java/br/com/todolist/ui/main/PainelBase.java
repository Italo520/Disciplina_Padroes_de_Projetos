package br.com.todolist.ui.main;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * Classe abstrata que serve como base para os painéis da tela principal
 * (Tarefas e Eventos).
 * Define a estrutura comum de layout e métodos abstratos que devem ser
 * implementados pelas subclasses.
 */
public abstract class PainelBase extends JPanel {

    /**
     * Construtor da classe PainelBase.
     * Configura o layout nulo para posicionamento absoluto.
     */
    /**
     * Construtor da classe PainelBase.
     * Configura o layout nulo para posicionamento absoluto.
     */
    protected PainelBase() {
        super();
        setLayout(null);
    }

    /**
     * Define o tamanho preferido do painel.
     *
     * @return As dimensões preferidas do painel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1265, 630);
    }

    /**
     * Inicializa o layout do painel, adicionando o painel de botões e o painel de
     * conteúdo.
     * Este método deve ser chamado no construtor das subclasses.
     */
    protected final void inicializarLayout() {

        JPanel painelDeBotoes = criarPainelDeBotoes();
        JPanel painelDeConteudo = criarPainelDeConteudo();

        painelDeBotoes.setBounds(0, 0, 1270, 50);
        painelDeConteudo.setBounds(0, 50, 1260, 560);

        add(painelDeBotoes);
        add(painelDeConteudo);
    }

    /**
     * Método abstrato para criar o painel de botões (ferramentas específicas da
     * tela).
     *
     * @return O painel de botões configurado.
     */
    protected abstract JPanel criarPainelDeBotoes();

    /**
     * Método abstrato para criar o painel de conteúdo (tabelas, listas, etc.).
     *
     * @return O painel de conteúdo configurado.
     */
    protected abstract JPanel criarPainelDeConteudo();
}
