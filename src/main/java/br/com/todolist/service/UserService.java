package br.com.todolist.service;

import br.com.todolist.entity.Usuario;

public interface UserService {
    boolean criarNovoUsuario(String nome, String email, String password);
    Usuario autenticarUsuario(String email, String password);
    Usuario buscarUsuarioPorEmail(String email);
}