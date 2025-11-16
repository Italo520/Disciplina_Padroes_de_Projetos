package br.com.todolist.repository;

import br.com.todolist.entity.Itens;

/**
 * Interface para o repositório de itens.
 * Estende a interface genérica Repository e define as operações de persistência para a entidade Item.
 *
 * @param <T> o tipo do item, que deve ser uma subclasse de Itens.
 */
public interface ItemRepository<T extends Itens> extends Repository<T, String> {
    // Métodos específicos para o repositório de itens, se houver, podem ser adicionados aqui.
}
