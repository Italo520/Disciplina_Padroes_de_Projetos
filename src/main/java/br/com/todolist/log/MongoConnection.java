package br.com.todolist.log;

import br.com.todolist.util.DatabaseConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static MongoConnection instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private MongoConnection() {
        String mongoUri = DatabaseConfig.getMongoUri();
        String databaseName = DatabaseConfig.getMongoDatabase();
        this.mongoClient = MongoClients.create(mongoUri);
        this.database = mongoClient.getDatabase(databaseName);
    }

    public static synchronized MongoConnection getInstance() {
        if (instance == null) {
            instance = new MongoConnection();
        }

        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}