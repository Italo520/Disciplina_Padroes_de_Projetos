package br.com.todolist.log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Serviço centralizado de logs.
 * Facilita o log de erros a partir de qualquer ponto da aplicação.
 */
public class LogService {

    private static LogService instance;
    private final ILogRepository logRepository;

    private LogService() {
        this.logRepository = new MongoLogRepository();
    }

    /**
     * Obtém a instância única do LogService.
     *
     * @return A instância do LogService.
     */
    public static synchronized LogService getInstance() {
        if (instance == null) {
            instance = new LogService();
        }
        return instance;
    }

    /**
     * Registra um erro no log.
     *
     * @param e A exceção a ser logada.
     */
    public void logError(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        LogEntry entry = new LogEntry(AuditAction.ERROR, e.getMessage(), sw.toString());
        logRepository.salvarLog(entry);
    }

    public ILogRepository getRepository() {
        return logRepository;
    }
}
