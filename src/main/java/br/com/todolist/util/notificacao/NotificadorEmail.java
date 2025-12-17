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

public class NotificadorEmail implements INotificador {

    private static final Logger LOGGER = Logger.getLogger(NotificadorEmail.class.getName());

    private static final String USERNAME = System.getenv("EMAIL_USERNAME") != null ? System.getenv("EMAIL_USERNAME")
            : "ads.ifpb.testes@gmail.com";

    private static final String PASSWORD = System.getenv("EMAIL_PASSWORD");
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    private final Session session;

    public NotificadorEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

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

    @Override
    public boolean enviarNotificacaoComAnexo(String destinatario, String assunto, String mensagem,
            String caminhoArquivo) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);

            MimeBodyPart textoBodyPart = new MimeBodyPart();
            textoBodyPart.setText(mensagem);

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