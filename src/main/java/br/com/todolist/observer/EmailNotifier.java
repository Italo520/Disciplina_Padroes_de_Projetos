package br.com.todolist.observer;

import br.com.todolist.entity.Itens;
import br.com.todolist.util.Mensageiro;

/**
 * Observador que envia notificações por e-mail.
 * Implementa a interface Observer e envia um e-mail quando um item é atualizado.
 */
public class EmailNotifier implements Observer<Itens> {

    private final Mensageiro mensageiro;

    public EmailNotifier() {
        this.mensageiro = new Mensageiro();
    }

    @Override
    public void update(Itens item) {
        String to = item.getCriado_por();
        String subject = "Notificação de Atualização: " + item.getTitulo();
        String body = "O item '" + item.getTitulo() + "' foi atualizado.";
        mensageiro.enviarEmail(to, subject, body);
    }
}
