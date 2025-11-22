package br.com.todolist.repository;

import br.com.todolist.entity.Evento;
import java.util.List;

/**
 * Interface para o repositório de eventos.
 * Define as operações de persistência para a entidade Evento.
 */
public interface IEventoRepository {

    /**
     * Salva um evento no repositório.
     *
     * @param entity o evento a ser salvo.
     */
    void salvar(Evento entity);

    /**
     * Exclui um evento do repositório.
     *
     * @param entity o evento a ser excluído.
     */
    void excluir(Evento entity);

    /**
     * Atualiza um evento no repositório.
     *
     * @param entity o evento a ser atualizado.
     */
    void atualizar(Evento entity);

    /**
     * Busca um evento pelo seu identificador.
     *
     * @param id o identificador do evento.
     * @return o evento encontrado, ou null se não for encontrado.
     */
    Evento buscarPorId(String id);

    /**
     * Busca todos os eventos do repositório.
     *
     * @return uma lista com todos os eventos.
     */
    List<Evento> buscarTodos();
    // Métodos específicos para o repositório de eventos, se houver, podem ser
    // adicionados aqui.
}
