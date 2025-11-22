package br.com.todolist.log;

import br.com.todolist.util.DatabaseConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Classe Singleton para gerenciar a conexão com o MongoDB.
 */
public class MongoConnection {

    private static MongoConnection instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private MongoConnection() {
        // Carrega configurações do arquivo de propriedades
        String mongoUri = DatabaseConfig.getMongoUri();
        String databaseName = DatabaseConfig.getMongoDatabase();
        this.mongoClient = MongoClients.create(mongoUri);
        this.database = mongoClient.getDatabase(databaseName);
    }

    /**
     * Obtém a instância única de MongoConnection.
     *
     * @return A instância de MongoConnection.
     */
    public static synchronized MongoConnection getInstance() {
        if (instance == null) {
            instance = new MongoConnection();
        }
        return instance;
    }

    /**
     * Obtém o banco de dados do MongoDB.
     *
     * @return O objeto MongoDatabase.
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Fecha a conexão com o MongoDB.
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
