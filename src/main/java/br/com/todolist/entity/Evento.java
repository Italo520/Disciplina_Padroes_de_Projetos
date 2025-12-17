package br.com.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "eventos")
public class Evento extends Itens {

    public Evento(String titulo, String descricao, String criadoPor, LocalDate deadline) {
        super(titulo, descricao, "Evento", criadoPor, deadline);
    }

    public Evento() {
    }

    @Override
    public String toString() {
        return getTitulo();
    }

    public Evento copiar() {
        Evento copia = new Evento(getTitulo(), getDescricao(), getCriadoPor(), getDeadline());
        copia.setTipo(getTipo());
        copia.setDataCadastro(getDataCadastro());
        return copia;
    }

}