package br.com.todolist.log;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.event.TaskEvent;
import br.com.todolist.service.util.IObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Observador responsável por auditar ações em Tarefas.
 * Persiste as mudanças no MongoDB.
 */
public class TaskAuditObserver implements IObserver<TaskEvent> {

    private final ILogRepository logRepository;

    public TaskAuditObserver(ILogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void update(TaskEvent event) {
        Map<String, Object> oldData = null;
        Map<String, Object> newData = null;

        if (event.getAction() == AuditAction.DELETE) {
            oldData = mapTarefa(event.getTarefa());
            newData = null;
        } else if (event.getAction() == AuditAction.CREATE) {
            oldData = null;
            newData = mapTarefa(event.getTarefa());
        } else if (event.getAction() == AuditAction.UPDATE) {
            if (event.getOldTarefa() != null) {
                oldData = mapTarefa(event.getOldTarefa());
            }
            if (event.getTarefa() != null) {
                newData = mapTarefa(event.getTarefa());
            }
        }

        LogEntry entry = new LogEntry(
                event.getAction(),
                "Tarefa",
                event.getTarefa().getCriado_por(),
                oldData,
                newData
        );

        logRepository.salvarLog(entry);
    }

    private Map<String, Object> mapTarefa(Tarefa t) {
        Map<String, Object> map = new HashMap<>();
        map.put("titulo", t.getTitulo());
        map.put("descricao", t.getDescricao());
        map.put("deadline", t.getDeadline() != null ? t.getDeadline().toString() : null);
        map.put("prioridade", t.getPrioridade());
        map.put("dataConclusao", t.getDataConclusao() != null ? t.getDataConclusao().toString() : null);
        return map;
    }
}
