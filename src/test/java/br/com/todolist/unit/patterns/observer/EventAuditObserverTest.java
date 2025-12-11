package br.com.todolist.unit.patterns.observer;

import br.com.todolist.entity.AuditLog;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.observer.EventAuditObserver;
import br.com.todolist.repository.AuditLogRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventAuditObserver - Observer Pattern Tests")
class EventAuditObserverTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private EventAuditObserver eventAuditObserver;

    @Test
    @DisplayName("Deve registrar log quando Tarefa for atualizada")
    void shouldLog_When_TarefaUpdated() {
        // Arrange
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa Teste");

        // Act
        eventAuditObserver.update(tarefa);

        // Assert
        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());

        AuditLog log = captor.getValue();
        assertThat(log.getEntityType()).isEqualTo("Tarefa");
        assertThat(log.getEventType()).isEqualTo("TASK_UPDATE");
    }

    @Test
    @DisplayName("Deve registrar log quando Evento for atualizado")
    void shouldLog_When_EventoUpdated() {
        // Arrange
        Evento evento = new Evento();
        evento.setTitulo("Evento Teste");

        // Act
        eventAuditObserver.update(evento);

        // Assert
        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());

        AuditLog log = captor.getValue();
        assertThat(log.getEntityType()).isEqualTo("Evento");
        assertThat(log.getEventType()).isEqualTo("EVENT_UPDATE");
    }
}
