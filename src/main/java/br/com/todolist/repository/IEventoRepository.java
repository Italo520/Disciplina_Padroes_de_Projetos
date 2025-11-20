package br.com.todolist.repository;

import br.com.todolist.entity.Evento;

/**
 * Interface para o repositório de eventos.
 * Estende a interface genérica IRepository e define as operações de persistência para a entidade Evento.
 */
public interface IEventoRepository extends IRepository<Evento, String> {
    // Métodos específicos para o repositório de eventos, se houver, podem ser adicionados aqui.
}
