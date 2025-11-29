package br.com.todolist.ui.auth;

import br.com.todolist.controller.AppController;
import br.com.todolist.exception.BusinessException;
import javax.swing.*;
import java.awt.*;

/**
 * Tela de cadastro de novos usuários.
 * Exibida como um diálogo modal sobre a tela de login.
 */
public class TelaCadastro extends JDialog {

    /** Campo de texto para o nome do usuário. */
    private JTextField campoNome;

    /** Campo de texto para o e-mail do usuário. */
    private JTextField campoEmail;

    /** Campo de senha para a senha do usuário. */
    private JPasswordField campoSenha;

    /** Botão para realizar o cadastro. */
    private JButton botaoCadastrar;

    /** Botão para cancelar o cadastro. */
    private JButton botaoCancelar;

    /** Armazena o e-mail cadastrado com sucesso. */
    private String emailCadastrado = null;

    /**
     * Construtor da classe TelaCadastro.
     *
     * @param owner O frame pai (geralmente a tela de login).
     */
    public TelaCadastro(Frame owner) {
        super(owner, "Criar Nova Conta", true);
        configurarLayout();
        configurarAcoes();
    }

    /**
     * Retorna o e-mail do usuário cadastrado com sucesso.
     * Útil para preencher automaticamente o campo de e-mail na tela de login.
     *
     * @return O e-mail cadastrado, ou null se o cadastro não foi concluído.
     */
    public String getEmailCadastrado() {
        return emailCadastrado;
    }

    /**
     * Configura o layout e adiciona os componentes à janela.
     */
    private void configurarLayout() {
        setTitle("Criar Nova Conta");
        setSize(1280, 720);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        // Campo Nome
        JLabel labelNome = new JLabel("Nome:");
        labelNome.setBounds(440, 230, 100, 30);
        add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(550, 230, 250, 30);
        add(campoNome);

        // Campo Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 275, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 275, 250, 30);
        add(campoEmail);

        // Campo Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 320, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 320, 250, 30);
        add(campoSenha);

        // Botoes
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(550, 380, 120, 30);
        add(botaoCadastrar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(680, 380, 120, 30);
        add(botaoCancelar);
    }

    /**
     * Define as ações para os botões.
     */
    private void configurarAcoes() {
        botaoCadastrar.addActionListener(e -> realizarCadastro());
        botaoCancelar.addActionListener(e -> dispose());
    }

    /**
     * Processa a solicitação de cadastro.
     * Valida os campos e chama o controlador para criar o usuário.
     */
    private void realizarCadastro() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        try {
            AppController.getInstance().cadastrarUsuario(nome, email, senha);
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            emailCadastrado = email;
            dispose();
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
