package br.com.todolist.service.notification;

public interface INotificador {
    boolean enviar(String destinatario, String mensagem);
}
