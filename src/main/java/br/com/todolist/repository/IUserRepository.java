package br.com.todolist.repository;

import br.com.todolist.entity.Usuario;

/**
 * Interface para o repositório de usuários.
 * Estende a interface genérica Repository e define as operações de persistência para a entidade Usuario.
 */
public interface IUserRepository extends IRepository<Usuario, String> {

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * @param email o e-mail do usuário a ser buscado.
     * @return o usuário encontrado, ou null se não for encontrado.
     */
    Usuario buscarPorEmail(String email);
}
