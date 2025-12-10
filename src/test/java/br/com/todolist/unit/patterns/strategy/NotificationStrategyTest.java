package br.com.todolist.unit.patterns.strategy;

import br.com.todolist.service.notification.INotificador;
import br.com.todolist.service.notification.NotificadorEmail;
import br.com.todolist.service.notification.NotificadorWhatsApp;
import br.com.todolist.util.Mensageiro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notification - Strategy Pattern Tests")
class NotificationStrategyTest {

    @Mock
    private Mensageiro mensageiro;

    @InjectMocks
    private NotificadorEmail notificadorEmail;

    @Test
    @DisplayName("Deve enviar email usando NotificadorEmail")
    void shouldSendEmail_When_UsingEmailStrategy() {
        // Arrange
        String destinatario = "test@example.com";
        String mensagem = "Olá, teste!";
        when(mensageiro.enviarEmail(eq(destinatario), anyString(), eq(mensagem))).thenReturn(true);

        // Act
        boolean resultado = notificadorEmail.enviar(destinatario, mensagem);

        // Assert
        assertThat(resultado).isTrue();
        verify(mensageiro).enviarEmail(eq(destinatario), anyString(), eq(mensagem));
    }

    @Test
    @DisplayName("Deve enviar whatsapp usando NotificadorWhatsApp")
    void shouldSendWhatsApp_When_UsingWhatsAppStrategy() {
        // Arrange
        INotificador notificador = new NotificadorWhatsApp();
        String destinatario = "123456789";
        String mensagem = "Olá, zap!";

        // Act
        boolean resultado = notificador.enviar(destinatario, mensagem);

        // Assert
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("Deve permitir troca de estratégia")
    void shouldAllowStrategySwitching() {
        // Arrange
        INotificador estrategiaAtual;

        // Act & Assert - Email
        estrategiaAtual = notificadorEmail;
        assertThat(estrategiaAtual).isInstanceOf(NotificadorEmail.class);

        // Act & Assert - WhatsApp
        estrategiaAtual = new NotificadorWhatsApp();
        assertThat(estrategiaAtual).isInstanceOf(NotificadorWhatsApp.class);
    }
}
