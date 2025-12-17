package br.com.todolist.log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogService {

    private static LogService instance;
    private final ILogRepository logRepository;

    private LogService() {
        this.logRepository = new MongoLogRepository();
    }

    public static synchronized LogService getInstance() {
        if (instance == null) {
            instance = new LogService();
        }
        return instance;
    }

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