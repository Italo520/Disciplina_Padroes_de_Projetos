package br.com.todolist.log;

/**
 * Interface para o repositório de logs.
 * Segue o princípio DIP (Dependency Inversion Principle).
 */
public interface ILogRepository {
    void salvarLog(LogEntry logEntry);
}
