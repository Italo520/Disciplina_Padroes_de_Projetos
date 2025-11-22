package br.com.todolist.service.util;

import br.com.todolist.service.event.CalendarEvent;

/**
 * Interface para o padrão Observer específico para eventos de calendário.
 * Define o método que deve ser implementado por observadores de eventos.
 */
public interface IEventObserver {

    /**
     * Método chamado quando um evento de calendário é atualizado.
     *
     * @param event o evento que foi atualizado.
     */
    void update(CalendarEvent event);
}
