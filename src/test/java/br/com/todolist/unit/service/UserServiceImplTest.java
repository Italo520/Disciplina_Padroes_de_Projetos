package br.com.todolist.unit.service;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl - Unit Tests")
class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Deve criar novo usuário com sucesso")
    void shouldCreateNewUserSuccessfully() {
        // Arrange
        String nome = "Test User";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.buscarPorEmail(email)).thenReturn(null);

        // Act
        boolean result = userService.criarNovoUsuario(nome, email, password);

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).buscarPorEmail(email);
        verify(userRepository).salvar(argThat(user -> user.getNome().equals(nome) &&
                user.getEmail().equals(email) &&
                BCrypt.checkpw(password, user.getPassword())));
    }

    @Test
    @DisplayName("Não deve criar usuário se email já existe")
    void shouldNotCreateUserIfEmailExists() {
        // Arrange
        String nome = "Test User";
        String email = "existing@example.com";
        String password = "password123";
        Usuario existingUser = new Usuario(nome, email, "hashedPassword");

        when(userRepository.buscarPorEmail(email)).thenReturn(existingUser);

        // Act
        boolean result = userService.criarNovoUsuario(nome, email, password);

        // Assert
        assertThat(result).isFalse();
        verify(userRepository).buscarPorEmail(email);
        verify(userRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso")
    void shouldAuthenticateUserSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario user = new Usuario("Test User", email, hashedPassword);

        when(userRepository.buscarPorEmail(email)).thenReturn(user);

        // Act
        Usuario result = userService.autenticarUsuario(email, password);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Não deve autenticar usuário inexistente")
    void shouldNotAuthenticateNonExistentUser() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepository.buscarPorEmail(email)).thenReturn(null);

        // Act
        Usuario result = userService.autenticarUsuario(email, password);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Não deve autenticar usuário com senha incorreta")
    void shouldNotAuthenticateUserWithWrongPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String wrongPassword = "wrongPassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario user = new Usuario("Test User", email, hashedPassword);

        when(userRepository.buscarPorEmail(email)).thenReturn(user);

        // Act
        Usuario result = userService.autenticarUsuario(email, wrongPassword);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve buscar usuário por email")
    void shouldFindUserByEmail() {
        // Arrange
        String email = "test@example.com";
        Usuario user = new Usuario("Test User", email, "hashedPassword");

        when(userRepository.buscarPorEmail(email)).thenReturn(user);

        // Act
        Usuario result = userService.buscarUsuarioPorEmail(email);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        verify(userRepository).buscarPorEmail(email);
    }
}
