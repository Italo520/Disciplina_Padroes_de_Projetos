package br.com.todolist;

import br.com.todolist.controller.AppController;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.service.util.DefaultItemFactory;
import br.com.todolist.service.util.IItemFactory;
import br.com.todolist.repository.UserRepositoryPostgres;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.impl.UserServiceImpl;
import br.com.todolist.ui.telasusuario.TelaLogin;
import br.com.todolist.util.Mensageiro;
import javax.swing.SwingUtilities;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

/**
 * Ponto de entrada da aplicação.
 * Responsável por configurar o tema, inicializar as dependências,
 * configurar o controlador principal e exibir a tela de login.
 */
public class Main {

    /**
     * Construtor privado para impedir a instanciação da classe principal.
     */
    private Main() {
    }

    /**
     * Método principal que inicia a execução da aplicação.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {

        // Configura o tema visual FlatLaf
        FlatCarbonIJTheme.setup();

        // Inicializa as dependências (Repositórios, Serviços, Utilitários)
        IUserRepository userRepository = new UserRepositoryPostgres();
        IUserService userService = new UserServiceImpl(userRepository);
        Mensageiro mensageiro = new Mensageiro();
        IItemFactory itemFactory = new DefaultItemFactory();

        // Inicializa a fachada (AppController) com as dependências injetadas
        AppController.init(userService, mensageiro, itemFactory);

        // Inicia a interface gráfica na Thread de Eventos do Swing
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}
