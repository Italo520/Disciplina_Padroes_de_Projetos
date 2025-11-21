package br.com.todolist.service.event;

import br.com.todolist.entity.Evento;
import br.com.todolist.log.AuditAction;

/**
 * Evento disparado quando ocorre uma ação em um Evento.
 * Encapsula a ação, o evento atual e, em caso de edição, a versão anterior.
 */
public class CalendarEvent {
    private final AuditAction action;
    private final Evento evento;
    private final Evento oldEvento;

    public CalendarEvent(AuditAction action, Evento evento, Evento oldEvento) {
        this.action = action;
        this.evento = evento;
        this.oldEvento = oldEvento;
    }

    public CalendarEvent(AuditAction action, Evento evento) {
        this(action, evento, null);
    }

    public AuditAction getAction() {
        return action;
    }

    public Evento getEvento() {
        return evento;
    }

    public Evento getOldEvento() {
        return oldEvento;
    }
}
