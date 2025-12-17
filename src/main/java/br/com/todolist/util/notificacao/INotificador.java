package br.com.todolist.util.notificacao;

public interface INotificador {

    boolean enviarNotificacao(String destinatario, String assunto, String mensagem);

    boolean enviarNotificacaoComAnexo(String destinatario, String assunto, String mensagem, String caminhoArquivo);
}