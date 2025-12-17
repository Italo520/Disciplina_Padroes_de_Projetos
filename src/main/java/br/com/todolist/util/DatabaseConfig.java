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
        String env = System.getenv("DB_URL");
        if (env != null)
            return env;

        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String name = System.getenv("DB_NAME");

        if (host != null && port != null && name != null) {
            return String.format("jdbc:postgresql://%s:%s/%s", host, port, name);
        }

        return getProperty("db.url");
    }

    public static String getDbUser() {
        String env = System.getenv("DB_USER");
        if (env != null)
            return env;
        return getProperty("db.user");
    }

    public static String getDbPassword() {
        String env = System.getenv("DB_PASSWORD");
        if (env != null)
            return env;
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
        String env = System.getenv("REDIS_HOST");
        if (env != null)
            return env;
        return getProperty("redis.host");
    }

    public static int getRedisPort() {
        String env = System.getenv("REDIS_PORT");
        if (env != null) {
            try {
                return Integer.parseInt(env);
            } catch (NumberFormatException e) {

            }
        }
        return Integer.parseInt(getProperty("redis.port"));
    }

    public static String getRedisPassword() {
        String env = System.getenv("REDIS_PASSWORD"); 

        if (env != null)
            return env;
        return getProperty("redis.password");
    }

    public static String getMongoUri() {
        String env = System.getenv("MONGO_URI");
        if (env != null)
            return env;

        String host = System.getenv("MONGO_HOST");
        String port = System.getenv("MONGO_PORT");
        String db = System.getenv("MONGO_DATABASE");
        String user = System.getenv("MONGO_USER");
        String pass = System.getenv("MONGO_PASSWORD");

        if (host != null && port != null && user != null && pass != null) {
            return String.format("mongodb://%s:%s@%s:%s", user, pass, host, port);
        }

        return getProperty("mongo.uri");
    }

    public static String getMongoDatabase() {
        String env = System.getenv("MONGO_DATABASE");
        if (env != null)
            return env;
        return getProperty("mongo.database");
    }
}