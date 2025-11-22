package br.com.todolist.util.notificacao;

/**
 * Interface que define o contrato para envio de notificações.
 * Permite que diferentes tipos de notificação (e-mail, WhatsApp, SMS, etc.)
 * sejam implementados seguindo o mesmo contrato.
 * 
 * Esta interface segue o Padrão Strategy, permitindo que o tipo de notificação
 * seja alterado em tempo de execução sem modificar o código cliente.
 */
public interface INotificador {

    /**
     * Envia uma notificação simples.
     *
     * @param destinatario O identificador do destinatário (e-mail, telefone, etc.).
     * @param assunto      O assunto da notificação.
     * @param mensagem     O conteúdo da mensagem.
     * @return true se a notificação foi enviada com sucesso, false caso contrário.
     */
    boolean enviarNotificacao(String destinatario, String assunto, String mensagem);

    /**
     * Envia uma notificação com anexo.
     *
     * @param destinatario   O identificador do destinatário (e-mail, telefone,
     *                       etc.).
     * @param assunto        O assunto da notificação.
     * @param mensagem       O conteúdo da mensagem.
     * @param caminhoArquivo O caminho do arquivo a ser anexado.
     * @return true se a notificação foi enviada com sucesso, false caso contrário.
     */
    boolean enviarNotificacaoComAnexo(String destinatario, String assunto, String mensagem, String caminhoArquivo);
}
