package br.com.todolist.repository;

import br.com.todolist.entity.Usuario;
import java.util.List;

/**
 * Interface para o repositório de usuários.
 * Define as operações de persistência para a entidade Usuario.
 */
public interface IUserRepository {

    /**
     * Salva um usuário no repositório.
     *
     * @param entity o usuário a ser salvo.
     */
    void salvar(Usuario entity);

    /**
     * Exclui um usuário do repositório.
     *
     * @param entity o usuário a ser excluído.
     */
    void excluir(Usuario entity);

    /**
     * Atualiza um usuário no repositório.
     *
     * @param entity o usuário a ser atualizado.
     */
    void atualizar(Usuario entity);

    /**
     * Busca um usuário pelo seu identificador.
     *
     * @param id o identificador do usuário.
     * @return o usuário encontrado, ou null se não for encontrado.
     */
    Usuario buscarPorId(String id);

    /**
     * Busca todos os usuários do repositório.
     *
     * @return uma lista com todos os usuários.
     */
    List<Usuario> buscarTodos();

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return O usuário encontrado, ou null se não existir.
     */
    Usuario buscarPorEmail(String email);
}
