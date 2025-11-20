package br.com.todolist.service;

import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.UsuarioJaCadastradoException;

/**
 * Interface para o serviço de usuários.
 * Define os métodos que devem ser implementados pelas classes de serviço de usuários.
 */
public interface IUserService {

    /**
     * Cria um novo usuário.
     *
     * @param nome o nome do usuário.
     * @param email o e-mail do usuário.
     * @param password a senha do usuário.
     * @throws UsuarioJaCadastradoException se o e-mail já estiver em uso.
     * @throws BusinessException se houver erro na validação ou persistência.
     */
    void criarNovoUsuario(String nome, String email, String password) throws BusinessException;

    /**
     * Autentica um usuário.
     *
     * @param email o e-mail do usuário.
     * @param password a senha do usuário.
     * @return o usuário autenticado.
     * @throws BusinessException se a autenticação falhar (ex: usuário não encontrado ou senha inválida).
     */
    Usuario autenticarUsuario(String email, String password) throws BusinessException;

    /**
     * Busca um usuário pelo seu e-mail.
     *
     * @param email o e-mail do usuário.
     * @return o usuário encontrado, ou null se não for encontrado.
     */
    Usuario buscarUsuarioPorEmail(String email);
}
