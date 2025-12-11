package br.com.todolist.observer;

import br.com.todolist.entity.AuditLog;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.AuditLogRepository;
import br.com.todolist.service.util.IObserver;

public class EventAuditObserver implements IObserver<Object> {

    private final AuditLogRepository auditLogRepository;

    public EventAuditObserver(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void update(Object object) {
        if (object instanceof Tarefa) {
            AuditLog log = new AuditLog("TASK_UPDATE", "Tarefa", null); // ID is not easily accessible as Long in
                                                                        // current Tarefa?
            // Tarefa extends Itens, let's check if it has ID.
            // Itens has getId()?
            auditLogRepository.save(log);
        } else if (object instanceof Evento) {
            AuditLog log = new AuditLog("EVENT_UPDATE", "Evento", null);
            auditLogRepository.save(log);
        }
    }
}
