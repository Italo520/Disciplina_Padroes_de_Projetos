package br.com.todolist.service;

import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.UsuarioJaCadastradoException;

public interface IUserService {

    void criarNovoUsuario(String nome, String email, String password) throws BusinessException;

    Usuario autenticarUsuario(String email, String password) throws BusinessException;

    Usuario buscarUsuarioPorEmail(String email);
}