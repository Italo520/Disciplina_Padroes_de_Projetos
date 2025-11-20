package br.com.todolist.exception;

/**
 * Exceção lançada quando tenta-se cadastrar um usuário com e-mail já existente.
 */
public class UsuarioJaCadastradoException extends BusinessException {
    public UsuarioJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado no sistema.");
    }
}
