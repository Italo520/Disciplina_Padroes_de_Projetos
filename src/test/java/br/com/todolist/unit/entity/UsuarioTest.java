package br.com.todolist.unit.entity;

import br.com.todolist.entity.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Usuario - Unit Tests")
class UsuarioTest {

    @Test
    @DisplayName("Deve criar usuário com construtor completo")
    void shouldCreateUserWithFullConstructor() {
        Usuario usuario = new Usuario("Name", "email@test.com", "password");

        assertThat(usuario.getNome()).isEqualTo("Name");
        assertThat(usuario.getEmail()).isEqualTo("email@test.com");
        assertThat(usuario.getPassword()).isEqualTo("password");
    }

    @Test
    @DisplayName("Deve atualizar campos corretamente")
    void shouldUpdateFields() {
        Usuario usuario = new Usuario();
        usuario.setNome("Name");
        usuario.setEmail("email@test.com");
        usuario.setPassword("password");

        assertThat(usuario.getNome()).isEqualTo("Name");
        assertThat(usuario.getEmail()).isEqualTo("email@test.com");
        assertThat(usuario.getPassword()).isEqualTo("password");
    }
}
