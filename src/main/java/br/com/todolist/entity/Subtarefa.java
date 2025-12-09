package br.com.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe que representa uma subtarefa associada a uma tarefa principal.
 * Possui um título e um status de conclusão.
 */
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

    /**
     * Construtor padrão da classe Subtarefa.
     */
    public Subtarefa() {
    }

    /**
     * Construtor da classe Subtarefa.
     * Inicializa a subtarefa com status "não concluído" (false).
     *
     * @param titulo O título da subtarefa.
     */
    public Subtarefa(String titulo) {
        this.titulo = titulo;
        this.status = false;
    }

    /**
     * Obtém o título da subtarefa.
     *
     * @return O título da subtarefa.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título da subtarefa.
     *
     * @param titulo O novo título da subtarefa.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Alterna o status da subtarefa entre concluído e não concluído.
     */
    public void mudarStatus() {
        status = !status;
    }

    /**
     * Verifica o status de conclusão da subtarefa.
     *
     * @return true se estiver concluída, false caso contrário.
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Define o status de conclusão da subtarefa.
     *
     * @param status O novo status (true para concluído, false para não concluído).
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Retorna uma representação em String da subtarefa (o título).
     *
     * @return O título da subtarefa.
     */
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

    /**
     * Cria uma cópia da subtarefa.
     *
     * @return Uma nova instância de Subtarefa com os mesmos dados.
     */
    public Subtarefa copiar() {
        Subtarefa copia = new Subtarefa(this.titulo);
        copia.setId(this.id);
        copia.setStatus(this.status);
        return copia;
    }

}
