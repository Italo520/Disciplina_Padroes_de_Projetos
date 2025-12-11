package br.com.todolist.ui.service;

import java.awt.Component;

public interface DialogService {
    void showMessage(Component parent, String message, String title, int messageType);

    void showError(Component parent, String message);

    void showInformation(Component parent, String message);
}
