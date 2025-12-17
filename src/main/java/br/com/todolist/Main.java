package br.com.todolist;

import br.com.todolist.controller.AppController;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.service.util.DefaultItemFactory;
import br.com.todolist.service.util.IItemFactory;
import br.com.todolist.repository.postgres.UserRepositoryPostgres;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.impl.UserServiceImpl;
import br.com.todolist.ui.auth.TelaLogin;
import br.com.todolist.util.notificacao.INotificador;
import br.com.todolist.util.notificacao.NotificadorEmail;
import javax.swing.SwingUtilities;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

public class Main {

    private Main() {
    }

    public static void main(String[] args) {

        FlatCarbonIJTheme.setup();

        IUserRepository userRepository = new UserRepositoryPostgres();
        IUserService userService = new UserServiceImpl(userRepository);
        INotificador notificador = new NotificadorEmail();
        IItemFactory itemFactory = new DefaultItemFactory();

        AppController.init(userService, notificador, itemFactory);

        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}