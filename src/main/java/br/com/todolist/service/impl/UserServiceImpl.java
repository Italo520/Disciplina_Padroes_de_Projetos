package br.com.todolist.service.impl;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.service.IUserService;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementação do serviço de usuários.
 * Gerencia o cadastro, autenticação e recuperação de informações de usuários.
 * Utiliza BCrypt para hashing de senhas.
 */
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    /**
     * Construtor da classe UserServiceImpl.
     *
     * @param userRepository O repositório de usuários a ser utilizado.
     */
    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gera um hash seguro para a senha fornecida utilizando o algoritmo BCrypt.
     *
     * @param senhaPura A senha em texto plano.
     * @return O hash da senha.
     */
    private String hashSenha(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    /**
     * Verifica se a senha fornecida corresponde ao hash salvo.
     *
     * @param senhaFornecida A senha em texto plano fornecida pelo usuário.
     * @param hashSalvo      O hash da senha armazenado no banco de dados.
     * @return true se a senha for válida, false caso contrário.
     */
    private boolean verificarSenha(String senhaFornecida, String hashSalvo) {
        return BCrypt.checkpw(senhaFornecida, hashSalvo);
    }

    /**
     * Cria um novo usuário no sistema.
     * Verifica se o e-mail já está cadastrado antes de prosseguir.
     *
     * @param nome     O nome do usuário.
     * @param email    O e-mail do usuário.
     * @param password A senha do usuário (será hasheada antes de salvar).
     * @return true se o usuário foi criado com sucesso, false se o e-mail já estiver em uso.
     */
    @Override
    public boolean criarNovoUsuario(String nome, String email, String password) {
        if (userRepository.buscarPorEmail(email) != null) {
            return false;
        }
        String senhaHasheada = hashSenha(password);
        Usuario novoUsuario = new Usuario(nome, email, senhaHasheada);
        userRepository.salvar(novoUsuario);
        return true;
    }

    /**
     * Autentica um usuário verificando seu e-mail e senha.
     *
     * @param email    O e-mail do usuário.
     * @param password A senha fornecida.
     * @return O objeto Usuario se a autenticação for bem-sucedida, ou null caso contrário.
     */
    @Override
    public Usuario autenticarUsuario(String email, String password) {
        Usuario usuario = userRepository.buscarPorEmail(email);
        if (usuario != null && verificarSenha(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return O objeto Usuario encontrado, ou null se não existir.
     */
    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return userRepository.buscarPorEmail(email);
    }
}
