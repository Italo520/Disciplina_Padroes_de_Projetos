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
     * @param titulo    O título do evento.
     * @param descricao A descrição do evento.
     * @param criadoPor O e-mail do usuário que criou o evento.
     * @param deadline  A data do evento.
     */
    public Evento(String titulo, String descricao, String criadoPor, LocalDate deadline) {
        super(titulo, descricao, "Evento", criadoPor, deadline);
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
    @Override
    public String toString() {
        return getTitulo();
    }

    /**
     * Cria uma cópia do evento.
     *
     * @return Uma nova instância de Evento com os mesmos dados.
     */
    public Evento copiar() {
        Evento copia = new Evento(getTitulo(), getDescricao(), getCriadoPor(), getDeadline());
        copia.setTipo(getTipo());
        copia.setDataCadastro(getDataCadastro());
        return copia;
    }

}
