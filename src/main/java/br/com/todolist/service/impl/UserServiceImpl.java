package br.com.todolist.service.impl;

import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.DadosInvalidosException;
import br.com.todolist.exception.DatabaseException;
import br.com.todolist.exception.UsuarioJaCadastradoException;
import br.com.todolist.repository.IUserRepository;
import br.com.todolist.service.IUserService;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String hashSenha(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    private boolean verificarSenha(String senhaFornecida, String hashSalvo) {
        return BCrypt.checkpw(senhaFornecida, hashSalvo);
    }

    @Override
    public void criarNovoUsuario(String nome, String email, String password) throws BusinessException {
        if (nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome, e-mail e senha são obrigatórios.");
        }

        if (userRepository.buscarPorEmail(email) != null) {
            throw new UsuarioJaCadastradoException(email);
        }

        String senhaHasheada = hashSenha(password);
        Usuario novoUsuario = new Usuario(nome, email, senhaHasheada);

        try {
            userRepository.salvar(novoUsuario);
        } catch (DatabaseException e) {

            Throwable cause = e.getCause();
            if (cause != null && cause.getMessage() != null && cause.getMessage().contains("ConstraintViolation")) {
                throw new UsuarioJaCadastradoException(email);
            }
            throw e; 
        }
    }

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

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return userRepository.buscarPorEmail(email);
    }
}