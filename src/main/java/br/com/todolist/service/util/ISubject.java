package br.com.todolist.service.util;

/**
 * Interface para o padrão Observer.
 * Define os métodos que devem ser implementados por todos os sujeitos (objetos observados).
 *
 * @param <T> o tipo do objeto que está sendo observado.
 */
public interface ISubject<T> {

    /**
     * Adiciona um observador à lista de observadores.
     *
     * @param observer o observador a ser adicionado.
     */
    void addObserver(IObserver<T> observer);

    /**
     * Remove um observador da lista de observadores.
     *
     * @param observer o observador a ser removido.
     */
    void removeObserver(IObserver<T> observer);

    /**
     * Notifica todos os observadores sobre uma atualização.
     *
     * @param object o objeto que foi atualizado.
     */
    void notifyObservers(T object);
}
