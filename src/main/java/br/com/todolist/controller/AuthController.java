package br.com.todolist.controller;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.UserRepository;
import br.com.todolist.repository.UserRepositoryImpl;
import br.com.todolist.service.UserService;
import br.com.todolist.service.UserServiceImpl;

public class AuthController {

    private final UserService userService;

    public AuthController() {
        UserRepository userRepository = new UserRepositoryImpl();
        this.userService = new UserServiceImpl(userRepository);
    }

    public Usuario login(String email, String password) {
        return userService.autenticarUsuario(email, password);
    }

    public boolean cadastrarUsuario(String nome, String email, String password) {
        return userService.criarNovoUsuario(nome, email, password);
    }
}