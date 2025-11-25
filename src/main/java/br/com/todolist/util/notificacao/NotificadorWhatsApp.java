package br.com.todolist.util.notificacao;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementação concreta de INotificador para envio de notificações via
 * WhatsApp.
 * 
 * NOTA: Esta é uma implementação de exemplo que demonstra como adicionar
 * um novo tipo de notificação sem modificar o código existente.
 * Para uma implementação real, seria necessário integrar com a API do WhatsApp
 * Business.
 * 
 * Esta classe implementa o Padrão Strategy, permitindo que o tipo de
 * notificação
 * seja trocado facilmente sem modificar o código que utiliza o notificador.
 */
public class NotificadorWhatsApp implements INotificador {

    private static final Logger LOGGER = Logger.getLogger(NotificadorWhatsApp.class.getName());

    /**
     * Envia uma notificação simples via WhatsApp.
     * 
     * NOTA: Implementação de exemplo. Em produção, seria necessário
     * integrar com a API do WhatsApp Business.
     *
     * @param destinatario O número de telefone do destinatário (formato
     *                     internacional).
     * @param assunto      O assunto da notificação (usado como primeira linha da
     *                     mensagem).
     * @param mensagem     O conteúdo da mensagem.
     * @return true se a notificação foi enviada com sucesso, false caso contrário.
     */
    @Override
    public boolean enviarNotificacao(String destinatario, String assunto, String mensagem) {
        try {
            // Aqui seria implementada a lógica de integração com a API do WhatsApp
            // Por exemplo, usando a API do Twilio, WhatsApp Business API, etc.

            LOGGER.info(() -> String.format(
                    "=== NOTIFICAÇÃO WHATSAPP ===%nPara: %s%nAssunto: %s%nMensagem: %s%n===========================",
                    destinatario, assunto, mensagem));

            // Simulação de envio bem-sucedido
            LOGGER.info(() -> "Mensagem WhatsApp enviada para " + destinatario + " com sucesso!");
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e, () -> "Erro ao enviar mensagem WhatsApp: " + e.getMessage());
            return false;
        }
    }

    /**
     * Envia uma notificação via WhatsApp com anexo.
     * 
     * NOTA: Implementação de exemplo. Em produção, seria necessário
     * integrar com a API do WhatsApp Business para envio de mídia.
     *
     * @param destinatario   O número de telefone do destinatário (formato
     *                       internacional).
     * @param assunto        O assunto da notificação.
     * @param mensagem       O conteúdo da mensagem.
     * @param caminhoArquivo O caminho do arquivo a ser anexado.
     * @return true se a notificação foi enviada com sucesso, false caso contrário.
     */
    @Override
    public boolean enviarNotificacaoComAnexo(String destinatario, String assunto, String mensagem,
            String caminhoArquivo) {
        try {
            // Aqui seria implementada a lógica de envio de arquivo via WhatsApp

            LOGGER.info(() -> String.format(
                    "=== NOTIFICAÇÃO WHATSAPP COM ANEXO ===%nPara: %s%nAssunto: %s%nMensagem: %s%nArquivo: %s%n======================================",
                    destinatario, assunto, mensagem, caminhoArquivo));

            // Simulação de envio bem-sucedido
            LOGGER.info(() -> "Mensagem WhatsApp com anexo enviada para " + destinatario + " com sucesso!");
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e, () -> "Erro ao enviar mensagem WhatsApp com anexo: " + e.getMessage());
            return false;
        }
    }
}
