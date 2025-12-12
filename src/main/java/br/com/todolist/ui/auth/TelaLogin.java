package br.com.todolist.ui.auth;

import br.com.todolist.controller.AppController;
import br.com.todolist.ui.main.TelaPrincipal;
import javax.swing.*;
import java.awt.BorderLayout;

public class TelaLogin extends JFrame {
    private JPanel painelLogin;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoCriarConta;

    public TelaLogin() {
        super("Login - ToDo List");
        configurarLayout();
        configurarAcoes();
    }

    private void configurarLayout() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        painelLogin = new JPanel();
        painelLogin.setLayout(null);
        painelLogin.setSize(1280, 720);
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 260, 100, 30);
        painelLogin.add(labelEmail);
        campoEmail = new JTextField();
        campoEmail.setBounds(550, 260, 250, 30);
        painelLogin.add(campoEmail);
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 305, 100, 30);
        painelLogin.add(labelSenha);
        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 305, 250, 30);
        painelLogin.add(campoSenha);
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(550, 365, 120, 30);
        painelLogin.add(botaoEntrar);
        botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.setBounds(680, 365, 120, 30);
        painelLogin.add(botaoCriarConta);
        add(painelLogin, BorderLayout.CENTER);
    }

    private void configurarAcoes() {
        botaoEntrar.addActionListener(e -> realizarLogin());
        botaoCriarConta.addActionListener(e -> abrirTelaDeCadastro());
        campoSenha.addActionListener(e -> realizarLogin());
    }

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

    private void abrirTelaDeCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(this);
        getContentPane().removeAll();
        getContentPane().add(telaCadastro, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

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