package br.com.todolist.service.util;

/**
 * Interface para o padrão Observer.
 * Define o método que deve ser implementado por todos os observadores.
 *
 * @param <T> o tipo do objeto que está sendo observado.
 */
public interface IObserver<T> {

    /**
     * Método chamado quando o objeto observado é atualizado.
     *
     * @param object o objeto que foi atualizado.
     */
    void update(T object);
}
