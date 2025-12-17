package br.com.todolist.controller;

import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.repository.postgres.UserRepositoryPostgres;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.impl.UserServiceImpl;

public class AuthController {

    private final IUserService userService;

    public AuthController() {
        IUserRepository userRepository = new UserRepositoryPostgres();
        this.userService = new UserServiceImpl(userRepository);
    }

    public Usuario login(String email, String password) throws BusinessException {
        return userService.autenticarUsuario(email, password);
    }

    public void cadastrarUsuario(String nome, String email, String password) throws BusinessException {
        userService.criarNovoUsuario(nome, email, password);
    }
}