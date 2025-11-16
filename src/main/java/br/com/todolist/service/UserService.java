package br.com.todolist.service;

import br.com.todolist.entity.Usuario;

/**
 * Interface para o serviço de usuários.
 * Define os métodos que devem ser implementados pelas classes de serviço de usuários.
 */
public interface UserService {

    /**
     * Cria um novo usuário.
     *
     * @param nome o nome do usuário.
     * @param email o e-mail do usuário.
     * @param password a senha do usuário.
     * @return true se o usuário foi criado com sucesso, false caso contrário.
     */
    boolean criarNovoUsuario(String nome, String email, String password);

    /**
     * Autentica um usuário.
     *
     * @param email o e-mail do usuário.
     * @param password a senha do usuário.
     * @return o usuário autenticado, ou null se a autenticação falhar.
     */
    Usuario autenticarUsuario(String email, String password);

    /**
     * Busca um usuário pelo seu e-mail.
     *
     * @param email o e-mail do usuário.
     * @return o usuário encontrado, ou null se não for encontrado.
     */
    Usuario buscarUsuarioPorEmail(String email);
}
