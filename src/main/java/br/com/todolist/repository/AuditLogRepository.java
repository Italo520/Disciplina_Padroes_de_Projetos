package br.com.todolist.repository;

import br.com.todolist.entity.AuditLog;

public interface AuditLogRepository {
    void save(AuditLog auditLog);
}
