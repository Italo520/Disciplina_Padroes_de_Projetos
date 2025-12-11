package br.com.todolist.ui.telasusuario;

import br.com.todolist.controller.AuthController;
import br.com.todolist.ui.service.DialogService;
import br.com.todolist.ui.service.SwingDialogService;
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

    /** Controlador de autenticação. */
    private final AuthController authController;

    /** Armazena o e-mail cadastrado com sucesso. */
    private String emailCadastrado = null;

    /** Serviço de diálogos. */
    private final DialogService dialogService;

    /**
     * Construtor da classe TelaCadastro.
     *
     * @param owner          O frame pai (geralmente a tela de login).
     * @param authController O controlador de autenticação para processar o
     *                       cadastro.
     */
    public TelaCadastro(Frame owner, AuthController authController) {
        this(owner, authController, new SwingDialogService());
    }

    /**
     * Construtor com injeção de dependência.
     *
     * @param owner          O frame pai.
     * @param authController O controlador de autenticação.
     * @param dialogService  O serviço de diálogos.
     */
    public TelaCadastro(Frame owner, AuthController authController, DialogService dialogService) {
        super(owner, "Criar Nova Conta", true);
        this.authController = authController;
        this.dialogService = dialogService;
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
        campoNome.setName("campoNome");
        campoNome.setBounds(550, 230, 250, 30);
        add(campoNome);

        // Campo Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 275, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setName("campoEmail");
        campoEmail.setBounds(550, 275, 250, 30);
        add(campoEmail);

        // Campo Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 320, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setName("campoSenha");
        campoSenha.setBounds(550, 320, 250, 30);
        add(campoSenha);

        // Botoes
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setName("botaoCadastrar");
        botaoCadastrar.setBounds(550, 380, 120, 30);
        add(botaoCadastrar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setName("botaoCancelar");
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
    public void realizarCadastro() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            dialogService.showError(this, "Todos os campos são obrigatórios.");
            return;
        }

        if (authController.cadastrarUsuario(nome, email, senha)) {
            dialogService.showInformation(this, "Usuário cadastrado com sucesso!");
            emailCadastrado = email;
            dispose();
        } else {
            dialogService.showError(this, "Este email já está em uso. Tente outro.");
        }
    }

    // Getters para testes
    public JTextField getCampoNome() {
        return campoNome;
    }

    public JTextField getCampoEmail() {
        return campoEmail;
    }

    public JPasswordField getCampoSenha() {
        return campoSenha;
    }

    public JButton getBotaoCadastrar() {
        return botaoCadastrar;
    }

    public JButton getBotaoCancelar() {
        return botaoCancelar;
    }
}
