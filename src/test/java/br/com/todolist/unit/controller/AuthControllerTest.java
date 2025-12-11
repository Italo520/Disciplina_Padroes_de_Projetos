package br.com.todolist.unit.controller;

import br.com.todolist.controller.AuthController;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController - Unit Tests")
class AuthControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void shouldLoginSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        Usuario usuario = new Usuario("Test User", email, password);
        when(userService.autenticarUsuario(email, password)).thenReturn(usuario);

        // Act
        Usuario result = authController.login(email, password);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        verify(userService).autenticarUsuario(email, password);
    }

    @Test
    @DisplayName("Deve falhar no login com credenciais inválidas")
    void shouldFailLoginWithInvalidCredentials() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongpassword";
        when(userService.autenticarUsuario(email, password)).thenReturn(null);

        // Act
        Usuario result = authController.login(email, password);

        // Assert
        assertThat(result).isNull();
        verify(userService).autenticarUsuario(email, password);
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        String nome = "New User";
        String email = "new@example.com";
        String password = "password";
        when(userService.criarNovoUsuario(nome, email, password)).thenReturn(true);

        // Act
        boolean result = authController.cadastrarUsuario(nome, email, password);

        // Assert
        assertThat(result).isTrue();
        verify(userService).criarNovoUsuario(nome, email, password);
    }

    @Test
    @DisplayName("Deve falhar ao cadastrar usuário existente")
    void shouldFailRegisteringExistingUser() {
        // Arrange
        String nome = "Existing User";
        String email = "existing@example.com";
        String password = "password";
        when(userService.criarNovoUsuario(nome, email, password)).thenReturn(false);

        // Act
        boolean result = authController.cadastrarUsuario(nome, email, password);

        // Assert
        assertThat(result).isFalse();
        verify(userService).criarNovoUsuario(nome, email, password);
    }
}
