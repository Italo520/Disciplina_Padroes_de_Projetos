package br.com.todolist.service.notification;

public class NotificadorWhatsApp implements INotificador {

    @Override
    public boolean enviar(String destinatario, String mensagem) {
        System.out.println("Enviando WhatsApp para " + destinatario + ": " + mensagem);
        return true;
    }
}
