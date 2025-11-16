package br.com.todolist.repository;

import java.util.List;

/**
 * Interface genérica para repositórios.
 * Define as operações básicas de persistência que todos os repositórios devem implementar.
 *
 * @param <T> o tipo da entidade.
 * @param <ID> o tipo do identificador da entidade.
 */
public interface Repository<T, ID> {

    /**
     * Salva uma entidade no repositório.
     *
     * @param entity a entidade a ser salva.
     */
    void salvar(T entity);

    /**
     * Exclui uma entidade do repositório.
     *
     * @param entity a entidade a ser excluída.
     */
    void excluir(T entity);

    /**
     * Atualiza uma entidade no repositório.
     *
     * @param entity a entidade a ser atualizada.
     */
    void atualizar(T entity);

    /**
     * Busca uma entidade pelo seu identificador.
     *
     * @param id o identificador da entidade.
     * @return a entidade encontrada, ou null se não for encontrada.
     */
    T buscarPorId(ID id);

    /**
     * Busca todas as entidades do repositório.
     *
     * @return uma lista com todas as entidades.
     */
    List<T> buscarTodos();
}
