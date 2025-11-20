package br.com.todolist.service.impl;

import br.com.todolist.entity.Usuario;
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
    public boolean criarNovoUsuario(String nome, String email, String password) {
        if (userRepository.buscarPorEmail(email) != null) {
            return false;
        }
        String senhaHasheada = hashSenha(password);
        Usuario novoUsuario = new Usuario(nome, email, senhaHasheada);
        userRepository.salvar(novoUsuario);
        return true;
    }

    @Override
    public Usuario autenticarUsuario(String email, String password) {
        Usuario usuario = userRepository.buscarPorEmail(email);
        if (usuario != null && verificarSenha(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return userRepository.buscarPorEmail(email);
    }
}