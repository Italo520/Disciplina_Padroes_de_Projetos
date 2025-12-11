package br.com.todolist.controller;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.repository.UserRepositoryImpl;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.impl.UserServiceImpl;

/**
 * Controlador responsável pelas operações de autenticação e cadastro de
 * usuários.
 * Gerencia a comunicação entre a interface do usuário e o serviço de usuários.
 */
public class AuthController {

    private final IUserService userService;

    /**
     * Construtor padrão da classe AuthController.
     * Inicializa o serviço de usuário com uma implementação padrão de repositório.
     */
    /**
     * Construtor padrão da classe AuthController.
     * Inicializa o serviço de usuário com uma implementação padrão de repositório.
     */
    public AuthController() {
        this(new UserServiceImpl(new UserRepositoryImpl()));
    }

    /**
     * Construtor para injeção de dependência.
     *
     * @param userService O serviço de usuário a ser utilizado.
     */
    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Realiza o login do usuário no sistema.
     *
     * @param email    O endereço de e-mail do usuário.
     * @param password A senha do usuário.
     * @return O objeto Usuario se a autenticação for bem-sucedida, ou null caso
     *         contrário.
     */
    public Usuario login(String email, String password) {
        return userService.autenticarUsuario(email, password);
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param nome     O nome completo do usuário.
     * @param email    O endereço de e-mail do usuário.
     * @param password A senha do usuário.
     * @return true se o cadastro foi realizado com sucesso, false se o e-mail já
     *         estiver em uso.
     */
    public boolean cadastrarUsuario(String nome, String email, String password) {
        return userService.criarNovoUsuario(nome, email, password);
    }
}
