package br.com.todolist.service.util;

import br.com.todolist.service.event.CalendarEvent;

public interface IEventObserver {
    void update(CalendarEvent event);
}