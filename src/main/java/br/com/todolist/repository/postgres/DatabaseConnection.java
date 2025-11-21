package br.com.todolist.repository.postgres;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton para gerenciar a conexão com o banco de dados via JPA.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private EntityManagerFactory entityManagerFactory;

    private DatabaseConnection() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("todolist-pu");
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
