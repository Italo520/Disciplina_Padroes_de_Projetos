package br.com.todolist.entity;

/**
 * Classe que representa uma subtarefa associada a uma tarefa principal.
 * Possui um título e um status de conclusão.
 */
public class Subtarefa {

    private String titulo;
    private boolean status;

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
        if (status == false) {
            status = true;
        } else {
            status = false;
        }
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

}
