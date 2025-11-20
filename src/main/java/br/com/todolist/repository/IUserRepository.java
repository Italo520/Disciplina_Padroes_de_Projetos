package br.com.todolist.repository;

import br.com.todolist.entity.Usuario;

/**
 * Interface para o repositório de usuários.
 * Estende a interface genérica IRepository e define as operações de persistência para a entidade Usuario.
 */
public interface IUserRepository extends IRepository<Usuario, String> {

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return O usuário encontrado, ou null se não existir.
     */
    Usuario buscarPorEmail(String email);
}
