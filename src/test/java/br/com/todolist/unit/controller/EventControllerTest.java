package br.com.todolist.unit.controller;

import br.com.todolist.controller.EventController;
import br.com.todolist.entity.Evento;
import br.com.todolist.service.IEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventController - Unit Tests")
class EventControllerTest {

    @Mock
    private IEventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    @DisplayName("Deve cadastrar evento delegando para o serviço")
    void shouldRegisterEvent() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        when(eventService.cadastrarEvento(evento)).thenReturn(true);

        // Act
        boolean result = eventController.cadastrarEvento(evento);

        // Assert
        assertThat(result).isTrue();
        verify(eventService).cadastrarEvento(evento);
    }

    @Test
    @DisplayName("Deve listar todos os eventos delegando para o serviço")
    void shouldListAllEvents() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        when(eventService.listarTodosEventos()).thenReturn(List.of(evento));

        // Act
        List<Evento> result = eventController.listarTodosEventos();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(evento);
        verify(eventService).listarTodosEventos();
    }

    @Test
    @DisplayName("Deve excluir evento delegando para o serviço")
    void shouldDeleteEvent() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());

        // Act
        eventController.excluirEvento(evento);

        // Assert
        verify(eventService).excluirEvento(evento);
    }

    @Test
    @DisplayName("Deve editar evento delegando para o serviço")
    void shouldEditEvent() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        String newTitle = "New Title";
        String newDesc = "New Desc";
        LocalDate newDate = LocalDate.now().plusDays(1);

        // Act
        eventController.editarEvento(evento, newTitle, newDesc, newDate);

        // Assert
        verify(eventService).editarEvento(evento, newTitle, newDesc, newDate);
    }

    @Test
    @DisplayName("Deve listar eventos por dia delegando para o serviço")
    void shouldListEventsByDay() {
        // Arrange
        LocalDate dia = LocalDate.now();
        Evento evento = new Evento("Title", "Desc", "user@test.com", dia);
        when(eventService.listarEventosPorDia(dia)).thenReturn(List.of(evento));

        // Act
        List<Evento> result = eventController.listarEventosPorDia(dia);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(evento);
        verify(eventService).listarEventosPorDia(dia);
    }

    @Test
    @DisplayName("Deve listar eventos por mês delegando para o serviço")
    void shouldListEventsByMonth() {
        // Arrange
        YearMonth mes = YearMonth.now();
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        when(eventService.listarEventosPorMes(mes)).thenReturn(List.of(evento));

        // Act
        List<Evento> result = eventController.listarEventosPorMes(mes);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(evento);
        verify(eventService).listarEventosPorMes(mes);
    }
}
