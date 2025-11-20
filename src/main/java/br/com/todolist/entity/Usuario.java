package br.com.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Classe que representa um Usuário no sistema.
 * Armazena as informações de autenticação e identificação.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    private String nome;
    @Id
    private String email;
    private String password;

    /**
     * Construtor padrão da classe Usuario.
     */
    public Usuario() {
    }

    /**
     * Construtor da classe Usuario.
     *
     * @param nome     O nome do usuário.
     * @param email    O e-mail do usuário.
     * @param password A senha (hash) do usuário.
     */
    public Usuario(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    /**
     * Obtém o nome do usuário.
     *
     * @return O nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do usuário.
     *
     * @param nome O novo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o e-mail do usuário.
     *
     * @return O e-mail do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do usuário.
     *
     * @param email O novo e-mail.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return A senha do usuário.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a senha do usuário.
     *
     * @param password A nova senha.
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
