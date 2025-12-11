package br.com.todolist.ui.service;

import java.awt.Component;
import javax.swing.JOptionPane;

public class SwingDialogService implements DialogService {

    @Override
    public void showMessage(Component parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    @Override
    public void showError(Component parent, String message) {
        showMessage(parent, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showInformation(Component parent, String message) {
        showMessage(parent, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
