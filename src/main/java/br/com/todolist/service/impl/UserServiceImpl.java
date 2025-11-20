package br.com.todolist.service.impl;

import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.DadosInvalidosException;
import br.com.todolist.exception.DatabaseException;
import br.com.todolist.exception.UsuarioJaCadastradoException;
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
     * @throws UsuarioJaCadastradoException se o e-mail já estiver em uso.
     * @throws BusinessException se houver erro na validação.
     */
    @Override
    public void criarNovoUsuario(String nome, String email, String password) throws BusinessException {
        if (nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome, e-mail e senha são obrigatórios.");
        }

        // Validação prévia
        if (userRepository.buscarPorEmail(email) != null) {
            throw new UsuarioJaCadastradoException(email);
        }

        String senhaHasheada = hashSenha(password);
        Usuario novoUsuario = new Usuario(nome, email, senhaHasheada);

        try {
            userRepository.salvar(novoUsuario);
        } catch (DatabaseException e) {
            // Tradução de erro de banco para erro de negócio (ex: concorrência)
            Throwable cause = e.getCause();
            if (cause != null && cause.getMessage() != null && cause.getMessage().contains("ConstraintViolation")) {
                throw new UsuarioJaCadastradoException(email);
            }
            throw e; // Relança erro de banco se não for conhecido
        }
    }

    /**
     * Autentica um usuário verificando seu e-mail e senha.
     *
     * @param email    O e-mail do usuário.
     * @param password A senha fornecida.
     * @return O objeto Usuario se a autenticação for bem-sucedida.
     * @throws BusinessException se o usuário não for encontrado ou a senha estiver incorreta.
     */
    @Override
    public Usuario autenticarUsuario(String email, String password) throws BusinessException {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new DadosInvalidosException("E-mail e senha são obrigatórios.");
        }

        Usuario usuario = userRepository.buscarPorEmail(email);
        if (usuario != null && verificarSenha(password, usuario.getPassword())) {
            return usuario;
        }
        throw new BusinessException("Usuário ou senha inválidos.");
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
