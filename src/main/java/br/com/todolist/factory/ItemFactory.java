package br.com.todolist.factory;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import java.time.LocalDate;

/**
 * Interface para a fábrica de itens.
 * Define os métodos que devem ser implementados pelas fábricas de itens.
 */
public interface ItemFactory {

    /**
     * Cria uma nova tarefa.
     *
     * @param titulo o título da tarefa.
     * @param descricao a descrição da tarefa.
     * @param criadoPor o e-mail do usuário que criou a tarefa.
     * @param deadline o prazo da tarefa.
     * @param prioridade a prioridade da tarefa.
     * @return a tarefa criada.
     */
    Tarefa criarTarefa(String titulo, String descricao, String criadoPor, LocalDate deadline, int prioridade);

    /**
     * Cria um novo evento.
     *
     * @param titulo o título do evento.
     * @param descricao a descrição do evento.
     * @param criadoPor o e-mail do usuário que criou o evento.
     * @param deadline o prazo do evento.
     * @return o evento criado.
     */
    Evento criarEvento(String titulo, String descricao, String criadoPor, LocalDate deadline);
}
