package br.com.todolist.log;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entidade que representa uma entrada de log (Auditoria ou Erro).
 */
public class LogEntry {

    private LocalDateTime timestamp;
    private AuditAction action;
    private String entityType;
    private String userEmail;
    private Map<String, Object> oldData;
    private Map<String, Object> newData;
    private String errorMessage;
    private String stackTrace;

    /**
     * Construtor padrão.
     */
    public LogEntry() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Construtor para logs de auditoria.
     */
    public LogEntry(AuditAction action, String entityType, String userEmail, Map<String, Object> oldData, Map<String, Object> newData) {
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.entityType = entityType;
        this.userEmail = userEmail;
        this.oldData = oldData;
        this.newData = newData;
    }

    /**
     * Construtor para logs de erro.
     */
    public LogEntry(AuditAction action, String errorMessage, String stackTrace) {
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Map<String, Object> getOldData() {
        return oldData;
    }

    public void setOldData(Map<String, Object> oldData) {
        this.oldData = oldData;
    }

    public Map<String, Object> getNewData() {
        return newData;
    }

    public void setNewData(Map<String, Object> newData) {
        this.newData = newData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
