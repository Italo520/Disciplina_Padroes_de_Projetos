package br.com.todolist.repository;

import br.com.todolist.entity.Usuario;
import java.util.List;

public interface IUserRepository {
    void salvar(Usuario entity);
    void excluir(Usuario entity);
    void atualizar(Usuario entity);
    Usuario buscarPorId(String id);
    List<Usuario> buscarTodos();
    Usuario buscarPorEmail(String email);
}