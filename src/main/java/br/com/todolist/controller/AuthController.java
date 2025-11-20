package br.com.todolist.controller;

import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.repository.UserRepositoryPostgres;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.impl.UserServiceImpl;

/**
 * Controlador responsável pelas operações de autenticação e cadastro de usuários.
 * Gerencia a comunicação entre a interface do usuário e o serviço de usuários.
 */
public class AuthController {

    private final IUserService userService;

    /**
     * Construtor padrão da classe AuthController.
     * Inicializa o serviço de usuário com a implementação de repositório PostgreSQL.
     */
    public AuthController() {
        IUserRepository userRepository = new UserRepositoryPostgres();
        this.userService = new UserServiceImpl(userRepository);
    }

    /**
     * Realiza o login do usuário no sistema.
     *
     * @param email    O endereço de e-mail do usuário.
     * @param password A senha do usuário.
     * @return O objeto Usuario se a autenticação for bem-sucedida.
     * @throws BusinessException se a autenticação falhar.
     */
    public Usuario login(String email, String password) throws BusinessException {
        return userService.autenticarUsuario(email, password);
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param nome     O nome completo do usuário.
     * @param email    O endereço de e-mail do usuário.
     * @param password A senha do usuário.
     * @throws BusinessException se houver erro no cadastro (ex: e-mail duplicado).
     */
    public void cadastrarUsuario(String nome, String email, String password) throws BusinessException {
        userService.criarNovoUsuario(nome, email, password);
    }
}
