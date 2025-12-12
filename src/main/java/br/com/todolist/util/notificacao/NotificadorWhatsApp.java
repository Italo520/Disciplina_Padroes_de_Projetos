package br.com.todolist.util.notificacao;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificadorWhatsApp implements INotificador {
    private static final Logger LOGGER = Logger.getLogger(NotificadorWhatsApp.class.getName());
    @Override
    public boolean enviarNotificacao(String destinatario, String assunto, String mensagem) {
        try {
            LOGGER.info(() -> String.format(
                    "=== NOTIFICAÇÃO WHATSAPP ===%nPara: %s%nAssunto: %s%nMensagem: %s%n===========================",
                    destinatario, assunto, mensagem));
            LOGGER.info(() -> "Mensagem WhatsApp enviada para " + destinatario + " com sucesso!");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e, () -> "Erro ao enviar mensagem WhatsApp: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean enviarNotificacaoComAnexo(String destinatario, String assunto, String mensagem,
            String caminhoArquivo) {
        try {
            LOGGER.info(() -> String.format(
                    "=== NOTIFICAÇÃO WHATSAPP COM ANEXO ===%nPara: %s%nAssunto: %s%nMensagem: %s%nArquivo: %s%n======================================",
                    destinatario, assunto, mensagem, caminhoArquivo));
            LOGGER.info(() -> "Mensagem WhatsApp com anexo enviada para " + destinatario + " com sucesso!");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e, () -> "Erro ao enviar mensagem WhatsApp com anexo: " + e.getMessage());
            return false;
        }
    }
}