package br.com.todolist.log;

import br.com.todolist.entity.Evento;
import br.com.todolist.service.event.CalendarEvent;
import br.com.todolist.service.util.IEventObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Observador responsável por auditar ações em Eventos.
 * Persiste as mudanças no MongoDB.
 */
public class EventAuditObserver implements IEventObserver {

    private final ILogRepository logRepository;

    public EventAuditObserver(ILogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void update(CalendarEvent event) {
        Map<String, Object> oldData = null;
        Map<String, Object> newData = null;

        if (event.getAction() == AuditAction.DELETE) {
            oldData = mapEvento(event.getEvento());
        } else if (event.getAction() == AuditAction.CREATE) {
            newData = mapEvento(event.getEvento());
        } else if (event.getAction() == AuditAction.UPDATE) {
            if (event.getOldEvento() != null) {
                oldData = mapEvento(event.getOldEvento());
            }
            if (event.getEvento() != null) {
                newData = mapEvento(event.getEvento());
            }
        }

        LogEntry entry = new LogEntry(
                event.getAction(),
                "Evento",
                event.getEvento().getCriadoPor(),
                oldData,
                newData);

        logRepository.salvarLog(entry);
    }

    private Map<String, Object> mapEvento(Evento e) {
        Map<String, Object> map = new HashMap<>();
        map.put("titulo", e.getTitulo());
        map.put("descricao", e.getDescricao());
        map.put("deadline", e.getDeadline() != null ? e.getDeadline().toString() : null);
        return map;
    }
}
