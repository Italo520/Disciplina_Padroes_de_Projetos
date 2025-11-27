package br.com.todolist.util.notificacao;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementação concreta de INotificador para envio de notificações via e-mail.
 * Utiliza o servidor SMTP do Gmail.
 * 
 * Esta classe implementa o Padrão Strategy, permitindo que o tipo de
 * notificação
 * seja trocado facilmente sem modificar o código que utiliza o notificador.
 */
public class NotificadorEmail implements INotificador {

    private static final Logger LOGGER = Logger.getLogger(NotificadorEmail.class.getName());
    // Credenciais devem ser configuradas via variáveis de ambiente para segurança
    private static final String USERNAME = System.getenv("EMAIL_USERNAME") != null ? System.getenv("EMAIL_USERNAME")
            : "ads.ifpb.testes@gmail.com";
    // A senha deve ser revogada e não hardcoded. Usando variável de ambiente.
    private static final String PASSWORD = System.getenv("EMAIL_PASSWORD");
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    private final Session session;

    /**
     * Construtor da classe NotificadorEmail.
     * Configura as propriedades da sessão de e-mail (SMTP, autenticação, TLS).
     */
    public NotificadorEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        this.session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    /**
     * Envia uma notificação simples por e-mail (apenas texto).
     *
     * @param destinatario O endereço de e-mail do destinatário.
     * @param assunto      O assunto do e-mail.
     * @param mensagem     O corpo (texto) da mensagem.
     * @return true se o e-mail foi enviado com sucesso, false caso contrário.
     */
    @Override
    public boolean enviarNotificacao(String destinatario, String assunto, String mensagem) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(mensagem);
            Transport.send(message);
            LOGGER.info(() -> "Email enviado para " + destinatario + " com sucesso!");
            return true;
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, e, () -> "Erro ao enviar o email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Envia uma notificação por e-mail com um arquivo em anexo.
     *
     * @param destinatario   O endereço de e-mail do destinatário.
     * @param assunto        O assunto do e-mail.
     * @param mensagem       O corpo (texto) da mensagem.
     * @param caminhoArquivo O caminho do arquivo a ser anexado.
     * @return true se o e-mail foi enviado com sucesso, false caso contrário ou se
     *         o arquivo não existir.
     */
    @Override
    public boolean enviarNotificacaoComAnexo(String destinatario, String assunto, String mensagem,
            String caminhoArquivo) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);

            // Parte 1: O texto do e-mail
            MimeBodyPart textoBodyPart = new MimeBodyPart();
            textoBodyPart.setText(mensagem);

            // Parte 2: O anexo
            MimeBodyPart anexoBodyPart = new MimeBodyPart();
            File arquivoAnexo = new File(caminhoArquivo);

            if (arquivoAnexo.exists() && arquivoAnexo.isFile()) {
                DataSource source = new FileDataSource(arquivoAnexo);
                anexoBodyPart.setDataHandler(new DataHandler(source));
                anexoBodyPart.setFileName(arquivoAnexo.getName());
            } else {
                LOGGER.warning(
                        () -> "Aviso: O arquivo '" + caminhoArquivo + "' não foi encontrado. E-mail não será enviado.");
                return false;
            }

            // Cria o corpo da mensagem com as duas partes
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textoBodyPart);
            multipart.addBodyPart(anexoBodyPart);
            message.setContent(multipart);

            Transport.send(message);
            LOGGER.info(() -> "Email com anexo enviado para " + destinatario + " com sucesso!");
            return true;

        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, e, () -> "Erro ao enviar o email com anexo: " + e.getMessage());
            return false;
        }
    }
}
