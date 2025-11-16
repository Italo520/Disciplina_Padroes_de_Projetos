package br.com.todolist.ui.telasusuario;

import br.com.todolist.controller.AuthController;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.SessionManager;
import br.com.todolist.ui.telaPrincipal.TelaPrincipal;
import javax.swing.*;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoCriarConta;
    private final AuthController authController;

    public TelaLogin() {
        super("Login - ToDo List");
        this.authController = new AuthController();
        configurarLayout();
        configurarAcoes();
    }
    
    private void configurarLayout() {
        // ... (seu método configurarLayout() continua aqui, sem alterações)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    private void configurarAcoes() {
        botaoEntrar.addActionListener(e -> realizarLogin());
        botaoCriarConta.addActionListener(e -> abrirTelaDeCadastro());
        campoSenha.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        if (email.trim().isEmpty() || senha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email e senha são obrigatórios.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = authController.login(email, senha);
        if (usuario != null) {
            SessionManager.getInstance().login(usuario);
            new TelaPrincipal().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTelaDeCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(this, authController);
        telaCadastro.setVisible(true);
        String emailNovo = telaCadastro.getEmailCadastrado();
        if (emailNovo != null) {
            campoEmail.setText(emailNovo);
            campoSenha.setText("");
            campoSenha.requestFocus();
        }
    }
}