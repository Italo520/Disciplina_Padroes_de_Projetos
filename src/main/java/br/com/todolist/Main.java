package br.com.todolist;

import br.com.todolist.controller.AppController;
import br.com.todolist.repository.UserRepository;
import br.com.todolist.factory.DefaultItemFactory;
import br.com.todolist.factory.ItemFactory;
import br.com.todolist.repository.UserRepositoryImpl;
import br.com.todolist.service.UserService;
import br.com.todolist.service.UserServiceImpl;
import br.com.todolist.ui.telasusuario.TelaLogin;
import br.com.todolist.util.Mensageiro;
import javax.swing.SwingUtilities;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

/**
 * Ponto de entrada da aplicação.
 * Responsável por inicializar o sistema e exibir a tela de login.
 */
public class Main {
    public static void main(String[] args) {

        FlatCarbonIJTheme.setup();

        // Inicializa as dependências
        UserRepository userRepository = new UserRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);
        Mensageiro mensageiro = new Mensageiro();
        ItemFactory itemFactory = new DefaultItemFactory();

        // Inicializa a fachada (AppController)
        AppController.init(userService, mensageiro, itemFactory);

        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}
