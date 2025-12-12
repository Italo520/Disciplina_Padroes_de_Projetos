package br.com.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "subtarefas")
public class Subtarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "tarefa_id")
    @JsonIgnore
    private Tarefa tarefa;

    public Subtarefa() {
    }

    public Subtarefa(String titulo) {
        this.titulo = titulo;
        this.status = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void mudarStatus() {
        status = !status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString() {
        return titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Subtarefa copiar() {
        Subtarefa copia = new Subtarefa(this.titulo);
        copia.setId(this.id);
        copia.setStatus(this.status);
        return copia;
    }
}