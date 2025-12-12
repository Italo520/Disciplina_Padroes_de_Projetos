package br.com.todolist.ui.auth;

import br.com.todolist.controller.AppController;

import br.com.todolist.ui.main.TelaPrincipal;
import javax.swing.*;
import java.awt.BorderLayout;

/**
 * Tela inicial de login da aplicação.
 * Permite ao usuário entrar no sistema ou navegar para a tela de cadastro.
 */
public class TelaLogin extends JFrame {

    /** Painel que contém os componentes de login. */
    private JPanel painelLogin;

    /** Campo de texto para o e-mail. */
    private JTextField campoEmail;

    /** Campo de senha. */
    private JPasswordField campoSenha;

    /** Botão para efetuar o login. */
    private JButton botaoEntrar;

    /** Botão para abrir a tela de cadastro. */
    private JButton botaoCriarConta;

    /**
     * Construtor padrão da classe TelaLogin.
     * Inicializa a interface gráfica.
     */
    public TelaLogin() {
        super("Login - ToDo List");
        configurarLayout();
        configurarAcoes();
    }

    /**
     * Configura o layout e os componentes da tela de login.
     */
    private void configurarLayout() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        // Define o layout do JFrame como CardLayout ou simplesmente null se formos
        // trocar manualmente
        setLayout(new BorderLayout());

        painelLogin = new JPanel();
        painelLogin.setLayout(null);
        painelLogin.setSize(1280, 720);

        // Campo de Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 260, 100, 30);
        painelLogin.add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 260, 250, 30);
        painelLogin.add(campoEmail);

        // Campo de Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 305, 100, 30);
        painelLogin.add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 305, 250, 30);
        painelLogin.add(campoSenha);

        // Botoes
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(550, 365, 120, 30);
        painelLogin.add(botaoEntrar);

        botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.setBounds(680, 365, 120, 30);
        painelLogin.add(botaoCriarConta);

        // Adiciona o painel de login ao frame
        add(painelLogin, BorderLayout.CENTER);
    }

    /**
     * Configura os listeners para os botões e campo de senha.
     */
    private void configurarAcoes() {
        botaoEntrar.addActionListener(e -> realizarLogin());
        botaoCriarConta.addActionListener(e -> abrirTelaDeCadastro());
        campoSenha.addActionListener(e -> realizarLogin());
    }

    /**
     * Tenta realizar o login com as credenciais fornecidas.
     * Se bem-sucedido, inicializa a sessão e abre a tela principal.
     */
    private void realizarLogin() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        try {
            boolean sucesso = AppController.getInstance().login(email, senha);
            if (sucesso) {

                new TelaPrincipal().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre a tela de cadastro de novos usuários, substituindo o painel atual.
     */
    private void abrirTelaDeCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(this);
        getContentPane().removeAll();
        getContentPane().add(telaCadastro, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Volta para o painel de login.
     *
     * @param email O e-mail para preencher automaticamente (pode ser null).
     */
    public void exibirPainelLogin(String email) {
        getContentPane().removeAll();
        getContentPane().add(painelLogin, BorderLayout.CENTER);

        if (email != null) {
            campoEmail.setText(email);
            campoSenha.setText("");
            campoSenha.requestFocus();
        }

        revalidate();
        repaint();
    }
}
