package br.com.todolist.unit.service;

import br.com.todolist.entity.Evento;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventServiceImpl - Unit Tests")
class EventServiceImplTest {

    @Mock
    private IEventoRepository eventoRepository;

    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventoRepository, "user@test.com");
    }

    @Test
    @DisplayName("Deve cadastrar evento com sucesso")
    void shouldRegisterEventSuccessfully() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        when(eventoRepository.buscarTodos()).thenReturn(Collections.emptyList());

        // Act
        boolean result = eventService.cadastrarEvento(evento);

        // Assert
        assertThat(result).isTrue();
        verify(eventoRepository).salvar(evento);
    }

    @Test
    @DisplayName("Deve listar todos os eventos")
    void shouldListAllEvents() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        when(eventoRepository.buscarTodos()).thenReturn(List.of(evento));

        // Act
        List<Evento> result = eventService.listarTodosEventos();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(evento);
    }

    @Test
    @DisplayName("Deve excluir evento")
    void shouldDeleteEvent() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());

        // Act
        eventService.excluirEvento(evento);

        // Assert
        verify(eventoRepository).excluir(evento);
    }

    @Test
    @DisplayName("Deve editar evento")
    void shouldEditEvent() {
        // Arrange
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        String newTitle = "New Title";
        String newDesc = "New Desc";
        LocalDate newDate = LocalDate.now().plusDays(1);

        // Act
        eventService.editarEvento(evento, newTitle, newDesc, newDate);

        // Assert
        assertThat(evento.getTitulo()).isEqualTo(newTitle);
        assertThat(evento.getDescricao()).isEqualTo(newDesc);
        assertThat(evento.getDeadline()).isEqualTo(newDate);
        verify(eventoRepository).atualizar(evento);
    }

    @Test
    @DisplayName("Deve listar eventos por dia")
    void shouldListEventsByDay() {
        // Arrange
        LocalDate dia = LocalDate.now();
        Evento evento = new Evento("Title", "Desc", "user@test.com", dia);
        when(eventoRepository.buscarTodos()).thenReturn(List.of(evento));

        // Act
        List<Evento> result = eventService.listarEventosPorDia(dia);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(evento);
    }

    @Test
    @DisplayName("Deve listar eventos por mês")
    void shouldListEventsByMonth() {
        // Arrange
        YearMonth mes = YearMonth.now();
        Evento evento = new Evento("Title", "Desc", "user@test.com", LocalDate.now());
        when(eventoRepository.buscarTodos()).thenReturn(List.of(evento));

        // Act
        List<Evento> result = eventService.listarEventosPorMes(mes);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(evento);
    }
}
