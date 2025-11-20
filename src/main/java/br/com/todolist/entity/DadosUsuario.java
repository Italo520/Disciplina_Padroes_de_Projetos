package br.com.todolist.entity;

import java.util.List;

/**
 * Classe que representa os dados de um usuário a serem persistidos.
 * Utilizada para agrupar tarefas e eventos de todos os usuários no arquivo JSON.
 */
public class DadosUsuario {
    private List<Tarefa> tarefas;
    private List<Evento> eventos;

    /**
     * Construtor padrão necessário para frameworks de serialização (como Jackson).
     */
    public DadosUsuario() {
    }

    /**
     * Construtor que inicializa a classe com listas de tarefas e eventos.
     *
     * @param tarefas Lista de tarefas de todos os usuários.
     * @param eventos Lista de eventos de todos os usuários.
     */
    public DadosUsuario(List<Tarefa> tarefas, List<Evento> eventos) {
        this.tarefas = tarefas;
        this.eventos = eventos;
    }

    /**
     * Obtém a lista de todas as tarefas armazenadas.
     *
     * @return Lista de objetos Tarefa.
     */
    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    /**
     * Obtém a lista de todos os eventos armazenados.
     *
     * @return Lista de objetos Evento.
     */
    public List<Evento> getEventos() {
        return eventos;
    }

    /**
     * Define a lista de tarefas.
     *
     * @param tarefas A nova lista de tarefas.
     */
    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    /**
     * Define a lista de eventos.
     *
     * @param eventos A nova lista de eventos.
     */
    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
}
