package br.com.todolist.exception;

public class UsuarioJaCadastradoException extends BusinessException {
    public UsuarioJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado no sistema.");
    }
}