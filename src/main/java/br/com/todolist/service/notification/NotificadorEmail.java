package br.com.todolist.service.notification;

import br.com.todolist.util.Mensageiro;

public class NotificadorEmail implements INotificador {

    private final Mensageiro mensageiro;

    public NotificadorEmail(Mensageiro mensageiro) {
        this.mensageiro = mensageiro;
    }

    @Override
    public boolean enviar(String destinatario, String mensagem) {
        return mensageiro.enviarEmail(destinatario, "Notificação ToDoList", mensagem);
    }
}
