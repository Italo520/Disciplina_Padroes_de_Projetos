package br.com.todolist.entity;

import java.time.LocalDate;

/**
 * Classe abstrata que serve como base para itens do sistema, como Tarefas e Eventos.
 * Contém atributos comuns como título, descrição, tipo, criador e datas.
 */
public abstract class Itens {
    private String titulo;
    private String descricao;
    private String tipo;
    private String criado_por;
    private LocalDate dataCadastro;
    private LocalDate deadline;

    /**
     * Construtor completo da classe Itens.
     *
     * @param titulo       O título do item.
     * @param descricao    A descrição do item.
     * @param tipo         O tipo do item (ex: "Tarefa", "Evento").
     * @param criado_por   O e-mail do usuário que criou o item.
     * @param deadline     A data limite ou data do evento.
     */
    public Itens(String titulo, String descricao, String tipo, String criado_por, LocalDate deadline) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.criado_por = criado_por;
        this.deadline = deadline;
        this.dataCadastro = LocalDate.now();
    }

    /**
     * Construtor padrão necessário para frameworks de serialização.
     */
    public Itens() {
    }

    /**
     * Obtém o título do item.
     *
     * @return O título do item.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título do item.
     *
     * @param titulo O novo título do item.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtém a descrição do item.
     *
     * @return A descrição do item.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do item.
     *
     * @param descricao A nova descrição do item.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém o tipo do item.
     *
     * @return O tipo do item.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo do item.
     *
     * @param tipo O novo tipo do item.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtém o e-mail do criador do item.
     *
     * @return O e-mail do criador.
     */
    public String getCriado_por() {
        return criado_por;
    }

    /**
     * Define o e-mail do criador do item.
     *
     * @param criado_por O novo e-mail do criador.
     */
    public void setCriado_por(String criado_por) {
        this.criado_por = criado_por;
    }

    /**
     * Obtém a data de cadastro do item.
     *
     * @return A data de cadastro.
     */
    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    /**
     * Define a data de cadastro do item.
     *
     * @param dataCadastro A nova data de cadastro.
     */
    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
     * Obtém a data limite (deadline) do item.
     *
     * @return A data limite.
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Define a data limite (deadline) do item.
     *
     * @param deadline A nova data limite.
     */
    public void setDeadLine(LocalDate deadline) {
        this.deadline = deadline;
    }
}
