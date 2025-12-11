package br.com.todolist.integration.repository;

import br.com.todolist.entity.Evento;
import br.com.todolist.repository.DatabaseConnection;
import br.com.todolist.repository.EventoRepositoryPostgres;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DisplayName("EventoRepository - PostgreSQL Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventoRepositoryPostgresTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("todolist_test_evento")
            .withUsername("test")
            .withPassword("test");

    private static EntityManagerFactory emf;
    private EventoRepositoryPostgres repository;

    @BeforeAll
    static void setUpAll() {
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        properties.put("jakarta.persistence.jdbc.user", postgres.getUsername());
        properties.put("jakarta.persistence.jdbc.password", postgres.getPassword());
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");

        emf = Persistence.createEntityManagerFactory("todolist-pu", properties);
        DatabaseConnection.getInstance().setEntityManagerFactory(emf);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        DatabaseConnection.getInstance().close();
    }

    @BeforeEach
    void setUp() {
        repository = new EventoRepositoryPostgres();
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar evento no PostgreSQL")
    void shouldSaveEvent_When_ValidEventProvided() {
        // Arrange
        Evento evento = new Evento("Meeting", "Team Sync", "user@test.com", LocalDate.now());

        // Act
        repository.salvar(evento);

        // Assert
        Evento found = repository.buscarPorId("Meeting");
        assertThat(found).isNotNull();
        assertThat(found.getTitulo()).isEqualTo("Meeting");
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar todos os eventos")
    void shouldFindAllEvents() {
        // Arrange
        Evento evento2 = new Evento("Workshop", "Java", "user@test.com", LocalDate.now().plusDays(1));
        repository.salvar(evento2);

        // Act
        List<Evento> events = repository.buscarTodos();

        // Assert
        assertThat(events).hasSizeGreaterThanOrEqualTo(2);
        assertThat(events).extracting(Evento::getTitulo).contains("Meeting", "Workshop");
    }

    @Test
    @Order(3)
    @DisplayName("Deve buscar eventos por dia")
    void shouldFindEventsByDay() {
        // Arrange
        LocalDate today = LocalDate.now();
        // "Meeting" was saved with today's date in test 1

        // Act
        List<Evento> events = repository.buscarPorDia(today);

        // Assert
        // This will fail until we implement the real logic
        assertThat(events).isNotEmpty();
        assertThat(events).extracting(Evento::getTitulo).contains("Meeting");
    }

    @Test
    @Order(4)
    @DisplayName("Deve buscar eventos por mês")
    void shouldFindEventsByMonth() {
        // Arrange
        YearMonth currentMonth = YearMonth.now();
        // Both "Meeting" and "Workshop" are in the current month

        // Act
        List<Evento> events = repository.buscarPorMes(currentMonth);

        // Assert
        // This will fail until we implement the real logic
        assertThat(events).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @Order(5)
    @DisplayName("Deve atualizar evento")
    void shouldUpdateEvent() {
        // Arrange
        Evento evento = repository.buscarPorId("Meeting");
        assertThat(evento).isNotNull();
        evento.setDescricao("Updated Desc");

        // Act
        repository.atualizar(evento);

        // Assert
        Evento found = repository.buscarPorId("Meeting");
        assertThat(found.getDescricao()).isEqualTo("Updated Desc");
    }

    @Test
    @Order(6)
    @DisplayName("Deve excluir evento")
    void shouldDeleteEvent() {
        // Arrange
        Evento evento = repository.buscarPorId("Workshop");
        assertThat(evento).isNotNull();

        // Act
        repository.excluir(evento);

        // Assert
        Evento found = repository.buscarPorId("Workshop");
        assertThat(found).isNull();
    }
}
