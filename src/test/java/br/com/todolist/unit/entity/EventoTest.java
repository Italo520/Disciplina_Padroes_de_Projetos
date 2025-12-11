package br.com.todolist.unit.entity;

import br.com.todolist.entity.Evento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Evento - Unit Tests")
class EventoTest {

    @Test
    @DisplayName("Deve criar evento com construtor completo")
    void shouldCreateEventWithFullConstructor() {
        LocalDate deadline = LocalDate.now();
        Evento evento = new Evento("Title", "Desc", "user@test.com", deadline);

        assertThat(evento.getTitulo()).isEqualTo("Title");
        assertThat(evento.getDescricao()).isEqualTo("Desc");
        assertThat(evento.getCriado_por()).isEqualTo("user@test.com");
        assertThat(evento.getDeadline()).isEqualTo(deadline);
    }

    @Test
    @DisplayName("Deve retornar título no toString")
    void shouldReturnTitleInToString() {
        Evento evento = new Evento();
        evento.setTitulo("My Event");

        assertThat(evento.toString()).isEqualTo("My Event");
    }
}
