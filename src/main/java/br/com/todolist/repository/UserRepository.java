package br.com.todolist.repository;

import br.com.todolist.entity.Usuario;
import java.util.List;

public interface UserRepository {
    void salvar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    List<Usuario> buscarTodos();
}