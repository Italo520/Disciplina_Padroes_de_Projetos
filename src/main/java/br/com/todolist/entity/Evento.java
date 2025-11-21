package br.com.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Classe que representa um Evento no sistema.
 * Estende a classe abstrata Itens.
 */
@Entity
@Table(name = "eventos")
public class Evento extends Itens {

    /**
     * Construtor da classe Evento.
     *
     * @param titulo       O título do evento.
     * @param descricao    A descrição do evento.
     * @param criado_por   O e-mail do usuário que criou o evento.
     * @param deadline     A data do evento.
     */
    public Evento(String titulo, String descricao, String criado_por, LocalDate deadline) {
        super(titulo, descricao, "Evento", criado_por, deadline);
    }

    /**
     * Construtor padrão da classe Evento.
     */
    public Evento() {
    }

    /**
     * Retorna uma representação em String do evento (apenas o título).
     *
     * @return O título do evento.
     */
    public String toString() {
        return getTitulo();
    }

    /**
     * Obtém o título do evento.
     *
     * @return O título do evento.
     */
    public String getTitulo() {
        return super.getTitulo();
    }

    /**
     * Define o título do evento.
     *
     * @param titulo O novo título.
     */
    public void setTitulo(String titulo) {
        super.setTitulo(titulo);
    }

    /**
     * Obtém a descrição do evento.
     *
     * @return A descrição do evento.
     */
    public String getDescricao() {
        return super.getDescricao();
    }

    /**
     * Define a descrição do evento.
     *
     * @param descricao A nova descrição.
     */
    public void setDescricao(String descricao) {
        super.setDescricao(descricao);
    }

    /**
     * Obtém a data de cadastro do evento.
     *
     * @return A data de cadastro.
     */
    public LocalDate getDataCadastro() {
        return super.getDataCadastro();
    }

    /**
     * Define a data de cadastro do evento.
     *
     * @param dataCadastro A nova data de cadastro.
     */
    public void setDataCadastro(LocalDate dataCadastro) {
        super.setDataCadastro(dataCadastro);
    }

    /**
     * Obtém a data do evento (deadline).
     *
     * @return A data do evento.
     */
    public LocalDate getDeadline() {
        return super.getDeadline();
    }

    /**
     * Define a data do evento (deadline).
     *
     * @param deadline A nova data do evento.
     */
    public void setDeadLine(LocalDate deadline) {
        super.setDeadLine(deadline);
    }

    /**
     * Cria uma cópia do evento.
     *
     * @return Uma nova instância de Evento com os mesmos dados.
     */
    public Evento copiar() {
        Evento copia = new Evento(getTitulo(), getDescricao(), getCriado_por(), getDeadline());
        copia.setTipo(getTipo());
        copia.setDataCadastro(getDataCadastro());
        return copia;
    }

}
