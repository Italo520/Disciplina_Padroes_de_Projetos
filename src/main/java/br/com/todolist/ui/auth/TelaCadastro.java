package br.com.todolist.ui.auth;

import br.com.todolist.controller.AppController;
import br.com.todolist.exception.BusinessException;
import javax.swing.*;

public class TelaCadastro extends JPanel {

    private TelaLogin telaLogin;

    private JTextField campoNome;

    private JTextField campoEmail;

    private JPasswordField campoSenha;

    private JButton botaoCadastrar;

    private JButton botaoCancelar;

    public TelaCadastro(TelaLogin telaLogin) {
        this.telaLogin = telaLogin;
        configurarLayout();
        configurarAcoes();
    }

    private void configurarLayout() {
        setSize(1280, 720);
        setLayout(null);

        JLabel labelNome = new JLabel("Nome:");
        labelNome.setBounds(440, 230, 100, 30);
        add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(550, 230, 250, 30);
        add(campoNome);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 275, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 275, 250, 30);
        add(campoEmail);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 320, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 320, 250, 30);
        add(campoSenha);

        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(550, 380, 120, 30);
        add(botaoCadastrar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(680, 380, 120, 30);
        add(botaoCancelar);
    }

    private void configurarAcoes() {
        botaoCadastrar.addActionListener(e -> realizarCadastro());
        botaoCancelar.addActionListener(e -> telaLogin.exibirPainelLogin(null));
    }

    private void realizarCadastro() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        try {
            AppController.getInstance().cadastrarUsuario(nome, email, senha);
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            telaLogin.exibirPainelLogin(email);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}