package br.com.todolist.unit.repository;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.UserRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserRepositoryImpl - Unit Tests (Legacy JSON)")
class UserRepositoryImplTest {

    private UserRepositoryImpl repository;
    private static final String ARQUIVO_USUARIOS = "arquivos/usuarios.json";

    @BeforeEach
    void setUp() {
        deleteFile();
        repository = new UserRepositoryImpl();
    }

    @AfterEach
    void tearDown() {
        deleteFile();
    }

    private void deleteFile() {
        File file = new File(ARQUIVO_USUARIOS);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Deve salvar usuário em JSON")
    void shouldSaveUser() {
        Usuario user = new Usuario("Test", "test@json.com", "pass");
        repository.salvar(user);

        Usuario found = repository.buscarPorEmail("test@json.com");
        assertThat(found).isNotNull();
        assertThat(found.getNome()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Deve buscar todos os usuários")
    void shouldFindAllUsers() {
        Usuario user1 = new Usuario("User 1", "u1@json.com", "p1");
        Usuario user2 = new Usuario("User 2", "u2@json.com", "p2");
        repository.salvar(user1);
        repository.salvar(user2);

        List<Usuario> users = repository.buscarTodos();
        assertThat(users).hasSize(2);
        assertThat(users).extracting(Usuario::getEmail).contains("u1@json.com", "u2@json.com");
    }

    @Test
    @DisplayName("Deve excluir usuário")
    void shouldDeleteUser() {
        Usuario user = new Usuario("Delete Me", "delete@json.com", "pass");
        repository.salvar(user);

        repository.excluir(user);

        Usuario found = repository.buscarPorEmail("delete@json.com");
        assertThat(found).isNull();
    }
}
