package br.com.todolist.log;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Implementação do repositório de logs utilizando MongoDB.
 */
public class MongoLogRepository implements ILogRepository {

    private final MongoCollection<Document> collection;

    public MongoLogRepository() {
        MongoDatabase database = MongoConnection.getInstance().getDatabase();
        this.collection = database.getCollection("logs");
    }

    @Override
    public void salvarLog(LogEntry logEntry) {
        Document doc = new Document();
        doc.append("timestamp", logEntry.getTimestamp().toString());
        doc.append("action", logEntry.getAction().toString());

        if (logEntry.getEntityType() != null) {
            doc.append("entityType", logEntry.getEntityType());
        }
        if (logEntry.getUserEmail() != null) {
            doc.append("userEmail", logEntry.getUserEmail());
        }
        if (logEntry.getOldData() != null) {
            doc.append("oldData", new Document(logEntry.getOldData()));
        }
        if (logEntry.getNewData() != null) {
            doc.append("newData", new Document(logEntry.getNewData()));
        }
        if (logEntry.getErrorMessage() != null) {
            doc.append("errorMessage", logEntry.getErrorMessage());
        }
        if (logEntry.getStackTrace() != null) {
            doc.append("stackTrace", logEntry.getStackTrace());
        }

        collection.insertOne(doc);
    }
}
