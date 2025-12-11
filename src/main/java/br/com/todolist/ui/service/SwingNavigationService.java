package br.com.todolist.ui.service;

import br.com.todolist.ui.telaPrincipal.TelaPrincipal;
import br.com.todolist.ui.telasusuario.TelaLogin;
import java.awt.Component;
import java.awt.Window;
import javax.swing.SwingUtilities;

public class SwingNavigationService implements NavigationService {

    @Override
    public void navigateToMain(Component currentScreen) {
        new TelaPrincipal().setVisible(true);
        closeCurrent(currentScreen);
    }

    @Override
    public void navigateToLogin(Component currentScreen) {
        new TelaLogin().setVisible(true);
        closeCurrent(currentScreen);
    }

    private void closeCurrent(Component currentScreen) {
        if (currentScreen instanceof Window) {
            ((Window) currentScreen).dispose();
        } else {
            Window window = SwingUtilities.getWindowAncestor(currentScreen);
            if (window != null) {
                window.dispose();
            }
        }
    }
}
