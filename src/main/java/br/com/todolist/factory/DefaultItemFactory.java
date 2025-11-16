package br.com.todolist.factory;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import java.time.LocalDate;

/**
 * Implementação padrão da fábrica de itens.
 * Encapsula a lógica de criação de tarefas e eventos.
 */
public class DefaultItemFactory implements ItemFactory {

    @Override
    public Tarefa criarTarefa(String titulo, String descricao, String criadoPor, LocalDate deadline, int prioridade) {
        return new Tarefa(titulo, descricao, criadoPor, deadline, prioridade);
    }

    @Override
    public Evento criarEvento(String titulo, String descricao, String criadoPor, LocalDate deadline) {
        return new Evento(titulo, descricao, criadoPor, deadline);
    }
}
