package br.com.todolist.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "database.properties";
    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Arquivo de configuração não encontrado: " + CONFIG_FILE);
            }

            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao carregar arquivo de configuração: " + e.getMessage(), e);
        }
    }

    private DatabaseConfig() {
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return null;
        }

        return processEnvironmentVariables(value);
    }

    private static String processEnvironmentVariables(String value) {
        if (value == null || !value.contains("${")) {
            return value;
        }

        String result = value;
        int startIndex;
        while ((startIndex = result.indexOf("${")) != -1) {
            int endIndex = result.indexOf("}", startIndex);
            if (endIndex == -1) {
                break;
            }

            String varExpression = result.substring(startIndex + 2, endIndex);
            String[] parts = varExpression.split(":", 2);
            String varName = parts[0];
            String defaultValue = parts.length > 1 ? parts[1] : "";
            String envValue = System.getenv(varName);
            String replacement = envValue != null ? envValue : defaultValue;
            result = result.substring(0, startIndex) + replacement + result.substring(endIndex + 1);
        }

        return result;
    }

    public static String getDbDriver() {
        return getProperty("db.driver");
    }

    public static String getDbUrl() {
        return getProperty("db.url");
    }

    public static String getDbUser() {
        return getProperty("db.user");
    }

    public static String getDbPassword() {
        return getProperty("db.password");
    }

    public static String getHibernateDialect() {
        return getProperty("hibernate.dialect");
    }

    public static String getHibernateShowSql() {
        return getProperty("hibernate.show_sql");
    }

    public static String getHibernateFormatSql() {
        return getProperty("hibernate.format_sql");
    }

    public static String getHibernateHbm2ddlAuto() {
        return getProperty("hibernate.hbm2ddl.auto");
    }

    public static String getRedisHost() {
        return getProperty("redis.host");
    }

    public static int getRedisPort() {
        return Integer.parseInt(getProperty("redis.port"));
    }

    public static String getRedisPassword() {
        return getProperty("redis.password");
    }

    public static String getMongoUri() {
        return getProperty("mongo.uri");
    }

    public static String getMongoDatabase() {
        return getProperty("mongo.database");
    }
}