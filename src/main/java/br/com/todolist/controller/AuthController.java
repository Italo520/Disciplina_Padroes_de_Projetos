package br.com.todolist.controller;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.repository.UserRepositoryImpl;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.impl.UserServiceImpl;

public class AuthController {

    private final IUserService userService;

    public AuthController() {
        IUserRepository userRepository = new UserRepositoryImpl();
        this.userService = new UserServiceImpl(userRepository);
    }

    public Usuario login(String email, String password) {
        return userService.autenticarUsuario(email, password);
    }

    public boolean cadastrarUsuario(String nome, String email, String password) {
        return userService.criarNovoUsuario(nome, email, password);
    }
}