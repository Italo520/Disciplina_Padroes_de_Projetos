package br.com.todolist.integration.service;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.DatabaseConnection;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.repository.TarefaRepositoryPostgres;
import br.com.todolist.service.impl.TaskServiceImpl;
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
@DisplayName("TaskService - Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServiceIntegrationTest {

    @Container
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("todolist_test_service")
            .withUsername("test")
            .withPassword("test");

    private static EntityManagerFactory emf;
    private TaskServiceImpl taskService;
    private ITarefaRepository repository;

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
        // Inicializa o serviço para o usuário "user@test.com"
        taskService = new TaskServiceImpl(repository, "user@test.com");
    }

    @Test
    @Order(1)
    @DisplayName("Deve cadastrar e recuperar tarefa do usuário")
    void shouldRegisterAndRetrieveTask() {
        // Arrange
        Tarefa task = new Tarefa("Service Task", "Description", "user@test.com", LocalDate.now().plusDays(2), 1);

        // Act
        taskService.cadastrarTarefa(task);

        // Assert
        List<Tarefa> tasks = taskService.listarTodasTarefas();
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).extracting(Tarefa::getTitulo).contains("Service Task");
    }

    @Test
    @Order(2)
    @DisplayName("Não deve listar tarefas de outros usuários")
    void shouldNotListTasksFromOtherUsers() {
        // Arrange - Cria tarefa para outro usuário diretamente no repositório
        Tarefa otherUserTask = new Tarefa("Other User Task", "Desc", "other@test.com", LocalDate.now(), 1);
        repository.salvar(otherUserTask);

        // Act
        List<Tarefa> myTasks = taskService.listarTodasTarefas();

        // Assert
        assertThat(myTasks).extracting(Tarefa::getTitulo).doesNotContain("Other User Task");
        assertThat(myTasks).extracting(Tarefa::getTitulo).contains("Service Task"); // From previous test
    }

    @Test
    @Order(3)
    @DisplayName("Deve editar tarefa existente")
    void shouldEditTask() {
        // Arrange
        Tarefa task = taskService.listarTodasTarefas().stream()
                .filter(t -> t.getTitulo().equals("Service Task"))
                .findFirst()
                .orElseThrow();

        LocalDate newDeadline = LocalDate.now().plusDays(5);

        // Act
        taskService.editarTarefa(task, "Service Task Updated", "New Desc", newDeadline, 3);

        // Assert
        Tarefa updated = repository.buscarPorId("Service Task Updated"); // ID is title? Yes, currently.
        assertThat(updated).isNotNull();
        assertThat(updated.getDescricao()).isEqualTo("New Desc");
        assertThat(updated.getPrioridade()).isEqualTo(3);
    }
}
