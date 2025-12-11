package br.com.todolist.ui.telasusuario;

import br.com.todolist.controller.AuthController;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.SessionManager;
import br.com.todolist.ui.service.DialogService;
import br.com.todolist.ui.service.NavigationService;
import br.com.todolist.ui.service.SwingDialogService;
import br.com.todolist.ui.service.SwingNavigationService;
import br.com.todolist.ui.telaPrincipal.TelaPrincipal;
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

    /** Controlador de autenticação. */
    private final AuthController authController;

    /** Serviço de diálogos. */
    private final DialogService dialogService;

    /** Serviço de navegação. */
    private final NavigationService navigationService;

    /**
     * Construtor padrão da classe TelaLogin.
     * Inicializa o controlador de autenticação e a interface gráfica.
     */
    public TelaLogin() {
        this(new AuthController(), new SwingDialogService(), new SwingNavigationService());
    }

    /**
     * Construtor com injeção de dependência.
     * 
     * @param authController    O controlador de autenticação.
     * @param dialogService     O serviço de diálogos.
     * @param navigationService O serviço de navegação.
     */
    public TelaLogin(AuthController authController, DialogService dialogService, NavigationService navigationService) {
        super("Login - ToDo List");
        this.authController = authController;
        this.dialogService = dialogService;
        this.navigationService = navigationService;
        configurarLayout();
        configurarAcoes();
    }

    /**
     * Configura o layout e os componentes da tela de login.
     */
    private void configurarLayout() {
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
        campoEmail.setName("campoEmail");
        campoEmail.setBounds(550, 260, 250, 30);
        add(campoEmail);

        // Campo de Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 305, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setName("campoSenha");
        campoSenha.setBounds(550, 305, 250, 30);
        add(campoSenha);

        // Botoes
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setName("botaoEntrar");
        botaoEntrar.setBounds(550, 365, 120, 30);
        add(botaoEntrar);

        botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.setName("botaoCriarConta");
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
    public void realizarLogin() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        if (email.trim().isEmpty() || senha.trim().isEmpty()) {
            dialogService.showError(this, "Email e senha são obrigatórios.");
            return;
        }

        Usuario usuario = authController.login(email, senha);
        if (usuario != null) {
            SessionManager.getInstance().login(usuario);
            navigationService.navigateToMain(this);
        } else {
            dialogService.showError(this, "Email ou senha incorretos.");
        }
    }

    /**
     * Abre a tela de cadastro de novos usuários.
     * Após o fechamento, preenche o campo de e-mail se o cadastro for bem-sucedido.
     */
    public void abrirTelaDeCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(this, authController, dialogService);
        telaCadastro.setVisible(true);
        String emailNovo = telaCadastro.getEmailCadastrado();
        if (emailNovo != null) {
            campoEmail.setText(emailNovo);
            campoSenha.setText("");
            campoSenha.requestFocus();
        }
    }

    // Getters para testes
    public JTextField getCampoEmail() {
        return campoEmail;
    }

    public JPasswordField getCampoSenha() {
        return campoSenha;
    }

    public JButton getBotaoEntrar() {
        return botaoEntrar;
    }

    public JButton getBotaoCriarConta() {
        return botaoCriarConta;
    }
}
