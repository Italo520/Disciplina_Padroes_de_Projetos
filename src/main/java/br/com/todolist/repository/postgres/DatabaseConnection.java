package br.com.todolist.repository.postgres;

import br.com.todolist.util.DatabaseConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private EntityManagerFactory entityManagerFactory;

    private DatabaseConnection() {

        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", DatabaseConfig.getDbDriver());
        properties.put("jakarta.persistence.jdbc.url", DatabaseConfig.getDbUrl());
        properties.put("jakarta.persistence.jdbc.user", DatabaseConfig.getDbUser());
        properties.put("jakarta.persistence.jdbc.password", DatabaseConfig.getDbPassword());
        properties.put("hibernate.dialect", DatabaseConfig.getHibernateDialect());
        properties.put("hibernate.show_sql", DatabaseConfig.getHibernateShowSql());
        properties.put("hibernate.format_sql", DatabaseConfig.getHibernateFormatSql());
        properties.put("hibernate.hbm2ddl.auto", DatabaseConfig.getHibernateHbm2ddlAuto());

        this.entityManagerFactory = Persistence.createEntityManagerFactory("todolist-pu", properties);
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