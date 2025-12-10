package br.com.todolist.unit.patterns.factory;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.util.DefaultItemFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DefaultItemFactory - Factory Pattern Tests")
class DefaultItemFactoryTest {

    private DefaultItemFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DefaultItemFactory();
    }

    @Test
    @DisplayName("Deve criar Tarefa com atributos corretos")
    void shouldCreateTarefa_When_ValidDataProvided() {
        // Arrange
        String titulo = "Nova Tarefa";
        String descricao = "Descrição da tarefa";
        String criadoPor = "user@test.com";
        LocalDate deadline = LocalDate.now().plusDays(5);
        int prioridade = 1;

        // Act
        Tarefa tarefa = factory.criarTarefa(titulo, descricao, criadoPor, deadline, prioridade);

        // Assert
        assertThat(tarefa).isNotNull();
        assertThat(tarefa.getTitulo()).isEqualTo(titulo);
        assertThat(tarefa.getDescricao()).isEqualTo(descricao);
        assertThat(tarefa.getCriado_por()).isEqualTo(criadoPor);
        assertThat(tarefa.getDeadline()).isEqualTo(deadline);
        assertThat(tarefa.getPrioridade()).isEqualTo(prioridade);
        assertThat(tarefa.getTipo()).isEqualTo("Tarefa");
    }

    @Test
    @DisplayName("Deve criar Evento com atributos corretos")
    void shouldCreateEvento_When_ValidDataProvided() {
        // Arrange
        String titulo = "Novo Evento";
        String descricao = "Descrição do evento";
        String criadoPor = "user@test.com";
        LocalDate deadline = LocalDate.now().plusDays(10);

        // Act
        Evento evento = factory.criarEvento(titulo, descricao, criadoPor, deadline);

        // Assert
        assertThat(evento).isNotNull();
        assertThat(evento.getTitulo()).isEqualTo(titulo);
        assertThat(evento.getDescricao()).isEqualTo(descricao);
        assertThat(evento.getCriado_por()).isEqualTo(criadoPor);
        assertThat(evento.getDeadline()).isEqualTo(deadline);
        assertThat(evento.getTipo()).isEqualTo("Evento");
    }
}
