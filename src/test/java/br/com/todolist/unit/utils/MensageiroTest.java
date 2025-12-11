package br.com.todolist.unit.utils;

import br.com.todolist.util.Mensageiro;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mensageiro - Unit Tests")
class MensageiroTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    private Mensageiro mensageiro;

    @BeforeEach
    void setUp() {
        // Configure Session to use GreenMail's SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.port", String.valueOf(greenMail.getSmtp().getPort()));
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.auth", "false"); // GreenMail doesn't require auth by default for tests

        Session session = Session.getInstance(props);
        mensageiro = new Mensageiro(session);
    }

    @Test
    @DisplayName("Deve enviar email simples com sucesso")
    void shouldSendSimpleEmail() throws MessagingException {
        // Arrange
        String to = "destinatario@test.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        boolean result = mensageiro.enviarEmail(to, subject, body);

        // Assert
        assertThat(result).isTrue();
        assertThat(greenMail.getReceivedMessages()).hasSize(1);
        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertThat(receivedMessage.getSubject()).isEqualTo(subject);
        assertThat(receivedMessage.getRecipients(Message.RecipientType.TO)[0].toString()).isEqualTo(to);
    }

    @Test
    @DisplayName("Deve enviar email com anexo com sucesso")
    void shouldSendEmailWithAttachment() throws MessagingException, IOException {
        // Arrange
        String to = "destinatario@test.com";
        String subject = "Attachment Test";
        String body = "Body with attachment";

        // Create a temporary file for attachment
        File tempFile = File.createTempFile("test-attachment", ".txt");
        tempFile.deleteOnExit();

        // Act
        boolean result = mensageiro.enviarEmailComAnexo(to, subject, body, tempFile.getAbsolutePath());

        // Assert
        assertThat(result).isTrue();
        assertThat(greenMail.getReceivedMessages()).hasSize(1);
        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertThat(receivedMessage.getSubject()).isEqualTo(subject);
        assertThat(receivedMessage.getContentType()).contains("multipart/mixed");
    }

    @Test
    @DisplayName("Deve falhar ao enviar email com anexo inexistente")
    void shouldFailToSendEmailWithNonExistentAttachment() {
        // Arrange
        String to = "destinatario@test.com";
        String subject = "Fail Attachment";
        String body = "Body";
        String invalidPath = "/path/to/non/existent/file.txt";

        // Act
        boolean result = mensageiro.enviarEmailComAnexo(to, subject, body, invalidPath);

        // Assert
        assertThat(result).isFalse();
        assertThat(greenMail.getReceivedMessages()).isEmpty();
    }
}
