package br.com.todolist.integration.repository;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.DatabaseConnection;
import br.com.todolist.repository.TarefaRepositoryPostgres;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DisplayName("TaskRepository - PostgreSQL Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskRepositoryIT {

    @Container
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("todolist_test")
            .withUsername("test")
            .withPassword("test");

    private static EntityManagerFactory emf;
    private TarefaRepositoryPostgres repository;

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
        repository = new TarefaRepositoryPostgres();
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar tarefa no PostgreSQL")
    void shouldSaveTask_When_ValidTaskProvided() {
        // Arrange
        Tarefa task = new Tarefa("Integration Task", "Desc", "user@test.com", LocalDate.now().plusDays(1), 1);

        // Act
        repository.salvar(task);

        // Assert
        Tarefa found = repository.buscarPorId("Integration Task");
        assertThat(found).isNotNull();
        assertThat(found.getTitulo()).isEqualTo("Integration Task");
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar todas as tarefas")
    void shouldFindAllTasks_When_TasksExist() {
        // Arrange
        Tarefa task2 = new Tarefa("Task 2", "Desc", "user@test.com", LocalDate.now().plusDays(1), 2);
        repository.salvar(task2);

        // Act
        List<Tarefa> tasks = repository.buscarTodos();

        // Assert
        assertThat(tasks).hasSizeGreaterThanOrEqualTo(2); // Task 1 from previous test + Task 2
        assertThat(tasks).extracting(Tarefa::getTitulo).contains("Integration Task", "Task 2");
    }

    @Test
    @Order(3)
    @DisplayName("Deve excluir tarefa")
    void shouldDeleteTask_When_TaskExists() {
        // Arrange
        Tarefa task = repository.buscarPorId("Task 2");
        assertThat(task).isNotNull();

        // Act
        repository.excluir(task);

        // Assert
        Tarefa found = repository.buscarPorId("Task 2");
        assertThat(found).isNull();
    }

    @Test
    @Order(4)
    @DisplayName("Deve buscar tarefas por dia")
    void shouldFindTasksByDay() {
        // Arrange
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        // "Integration Task" was saved with tomorrow's date in test 1

        // Act
        List<Tarefa> tasks = repository.buscarPorDia(tomorrow);

        // Assert
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).extracting(Tarefa::getTitulo).contains("Integration Task");
    }

    @Test
    @Order(5)
    @DisplayName("Deve buscar tarefas críticas")
    void shouldFindCriticalTasks() {
        // Arrange
        // "Integration Task" has priority 1 (Critical)
        // "Task 2" has priority 2 (Not critical if threshold is 1)

        // Act
        List<Tarefa> criticalTasks = repository.buscarTarefasCriticas(1);

        // Assert
        assertThat(criticalTasks).isNotEmpty();
        assertThat(criticalTasks).extracting(Tarefa::getTitulo).contains("Integration Task");
        assertThat(criticalTasks).extracting(Tarefa::getTitulo).doesNotContain("Task 2");
    }
}
