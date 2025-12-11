package br.com.todolist.integration.repository;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.DatabaseConnection;
import br.com.todolist.repository.UserRepositoryPostgres;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DisplayName("UserRepository - PostgreSQL Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryPostgresTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("todolist_test_user")
            .withUsername("test")
            .withPassword("test");

    private static EntityManagerFactory emf;
    private UserRepositoryPostgres repository;

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
        repository = new UserRepositoryPostgres();
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar usuário no PostgreSQL")
    void shouldSaveUser_When_ValidUserProvided() {
        // Arrange
        Usuario user = new Usuario("Test User", "test@example.com", "password123");

        // Act
        repository.salvar(user);

        // Assert
        Usuario found = repository.buscarPorId("test@example.com");
        assertThat(found).isNotNull();
        assertThat(found.getNome()).isEqualTo("Test User");
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar usuário por email")
    void shouldFindUserByEmail_When_UserExists() {
        // Arrange (User saved in previous test)
        String email = "test@example.com";

        // Act
        Usuario found = repository.buscarPorEmail(email);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(email);
    }

    @Test
    @Order(3)
    @DisplayName("Deve atualizar usuário")
    void shouldUpdateUser_When_UserExists() {
        // Arrange
        Usuario user = repository.buscarPorId("test@example.com");
        assertThat(user).isNotNull();
        user.setNome("Updated Name");

        // Act
        repository.atualizar(user);

        // Assert
        Usuario found = repository.buscarPorId("test@example.com");
        assertThat(found.getNome()).isEqualTo("Updated Name");
    }

    @Test
    @Order(4)
    @DisplayName("Deve buscar todos os usuários")
    void shouldFindAllUsers() {
        // Arrange
        Usuario user2 = new Usuario("User 2", "user2@example.com", "pass");
        repository.salvar(user2);

        // Act
        List<Usuario> users = repository.buscarTodos();

        // Assert
        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
        assertThat(users).extracting(Usuario::getEmail).contains("test@example.com", "user2@example.com");
    }

    @Test
    @Order(5)
    @DisplayName("Deve excluir usuário")
    void shouldDeleteUser_When_UserExists() {
        // Arrange
        Usuario user = repository.buscarPorId("user2@example.com");
        assertThat(user).isNotNull();

        // Act
        repository.excluir(user);

        // Assert
        Usuario found = repository.buscarPorId("user2@example.com");
        assertThat(found).isNull();
    }
}
