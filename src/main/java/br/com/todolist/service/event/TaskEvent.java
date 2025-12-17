package br.com.todolist.service.event;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.log.AuditAction;

public class TaskEvent {
    private final AuditAction action;
    private final Tarefa tarefa;
    private final Tarefa oldTarefa;

    public TaskEvent(AuditAction action, Tarefa tarefa, Tarefa oldTarefa) {
        this.action = action;
        this.tarefa = tarefa;
        this.oldTarefa = oldTarefa;
    }

    public TaskEvent(AuditAction action, Tarefa tarefa) {
        this(action, tarefa, null);
    }

    public AuditAction getAction() {
        return action;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public Tarefa getOldTarefa() {
        return oldTarefa;
    }
}