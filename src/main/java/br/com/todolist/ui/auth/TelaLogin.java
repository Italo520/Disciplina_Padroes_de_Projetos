package br.com.todolist.ui.auth;

import br.com.todolist.controller.AppController;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.SessionManager;
import br.com.todolist.ui.main.TelaPrincipal;
import javax.swing.*;

/**
 * Tela inicial de login da aplicação.
 * Permite ao usuário entrar no sistema ou navegar para a tela de cadastro.
 */
public class TelaLogin extends JFrame {

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
        setLayout(null);

        // Campo de Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 260, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 260, 250, 30);
        add(campoEmail);

        // Campo de Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 305, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 305, 250, 30);
        add(campoSenha);

        // Botoes
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(550, 365, 120, 30);
        add(botaoEntrar);

        botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.setBounds(680, 365, 120, 30);
        add(botaoCriarConta);
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
                Usuario usuario = AppController.getInstance().getUsuarioLogado();
                SessionManager.getInstance().login(usuario);
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
     * Abre a tela de cadastro de novos usuários.
     * Após o fechamento, preenche o campo de e-mail se o cadastro for bem-sucedido.
     */
    private void abrirTelaDeCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(this);
        telaCadastro.setVisible(true);
        String emailNovo = telaCadastro.getEmailCadastrado();
        if (emailNovo != null) {
            campoEmail.setText(emailNovo);
            campoSenha.setText("");
            campoSenha.requestFocus();
        }
    }
}
