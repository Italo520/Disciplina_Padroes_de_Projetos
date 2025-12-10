package br.com.todolist.unit.patterns.singleton;

import br.com.todolist.entity.Usuario;
import br.com.todolist.service.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SessionManager - Singleton Pattern Tests")
class SessionManagerTest {

    @BeforeEach
    void setUp() {
        // Garante que a sessão esteja limpa antes de cada teste
        SessionManager.getInstance().logout();
    }

    @Test
    @DisplayName("Deve retornar sempre a mesma instância (Singleton)")
    void shouldReturnSameInstance_When_GetInstanceCalledMultipleTimes() {
        // Act
        SessionManager instance1 = SessionManager.getInstance();
        SessionManager instance2 = SessionManager.getInstance();

        // Assert
        assertThat(instance1)
                .isNotNull()
                .isSameAs(instance2);
    }

    @Test
    @DisplayName("Deve iniciar sessão com usuário válido")
    void shouldLogin_When_ValidUserProvided() {
        // Arrange
        Usuario user = new Usuario("Test User", "test@example.com", "password");

        // Act
        SessionManager.getInstance().login(user);

        // Assert
        assertThat(SessionManager.getInstance().getUsuarioLogado())
                .isNotNull()
                .isEqualTo(user);
        assertThat(SessionManager.getInstance().getEmailUsuarioLogado())
                .isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Deve limpar sessão ao fazer logout")
    void shouldLogout_When_LogoutCalled() {
        // Arrange
        Usuario user = new Usuario("Test User", "test@example.com", "password");
        SessionManager.getInstance().login(user);

        // Act
        SessionManager.getInstance().logout();

        // Assert
        assertThat(SessionManager.getInstance().getUsuarioLogado()).isNull();
        assertThat(SessionManager.getInstance().getTaskService()).isNull();
        assertThat(SessionManager.getInstance().getEventService()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com login de usuário nulo")
    void shouldHandleNullUser_When_LoginCalledWithNull() {
        // Act
        SessionManager.getInstance().login(null);

        // Assert
        assertThat(SessionManager.getInstance().getUsuarioLogado()).isNull();
        assertThat(SessionManager.getInstance().getTaskService()).isNull();
    }
}
