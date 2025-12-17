package br.com.todolist.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe utilitária para carregar as configurações de banco de dados.
 * Centraliza o acesso às propriedades armazenadas no arquivo
 * database.properties.
 */
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

    /**
     * Construtor privado para impedir instanciação.
     */
    private DatabaseConfig() {
    }

    /**
     * Obtém uma propriedade do arquivo de configuração ou de variável de ambiente.
     * Prioridade: Variável de ambiente > Arquivo de propriedades
     * 
     * Suporta sintaxe ${VAR_NAME:default_value} no arquivo properties.
     *
     * @param key A chave da propriedade.
     * @return O valor da propriedade.
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return null;
        }

        // Processa variáveis de ambiente usando a sintaxe de placeholder
        return processEnvironmentVariables(value);
    }

    /**
     * Processa e substitui variáveis de ambiente no valor da propriedade.
     * Suporta formato: ${VARIABLE_NAME:default_value}
     * 
     * @param value O valor da propriedade que pode conter variáveis.
     * @return O valor com variáveis substituídas.
     */
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

            // Tenta obter da variável de ambiente, senão usa o valor padrão
            String envValue = System.getenv(varName);
            String replacement = envValue != null ? envValue : defaultValue;

            result = result.substring(0, startIndex) + replacement + result.substring(endIndex + 1);
        }

        return result;
    }

    /**
     * Obtém o driver JDBC do PostgreSQL.
     *
     * @return O driver JDBC.
     */
    public static String getDbDriver() {
        return getProperty("db.driver");
    }

    /**
     * Obtém a URL de conexão do banco de dados PostgreSQL.
     *
     * @return A URL de conexão.
     */
    public static String getDbUrl() {
        String env = System.getenv("DB_URL");
        if (env != null)
            return env;

        // Monta a URL se as parciais estiverem disponíveis
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String name = System.getenv("DB_NAME");

        if (host != null && port != null && name != null) {
            return String.format("jdbc:postgresql://%s:%s/%s", host, port, name);
        }

        return getProperty("db.url");
    }

    /**
     * Obtém o usuário do banco de dados PostgreSQL.
     *
     * @return O nome de usuário.
     */
    public static String getDbUser() {
        String env = System.getenv("DB_USER");
        if (env != null)
            return env;
        return getProperty("db.user");
    }

    /**
     * Obtém a senha do banco de dados PostgreSQL.
     *
     * @return A senha do usuário.
     */
    public static String getDbPassword() {
        String env = System.getenv("DB_PASSWORD");
        if (env != null)
            return env;
        return getProperty("db.password");
    }

    /**
     * Obtém o dialeto do Hibernate.
     *
     * @return O dialeto do Hibernate.
     */
    public static String getHibernateDialect() {
        return getProperty("hibernate.dialect");
    }

    /**
     * Obtém a configuração de exibição de SQL do Hibernate.
     *
     * @return true se deve mostrar SQL, false caso contrário.
     */
    public static String getHibernateShowSql() {
        return getProperty("hibernate.show_sql");
    }

    /**
     * Obtém a configuração de formatação de SQL do Hibernate.
     *
     * @return true se deve formatar SQL, false caso contrário.
     */
    public static String getHibernateFormatSql() {
        return getProperty("hibernate.format_sql");
    }

    /**
     * Obtém a estratégia de criação/atualização do schema do Hibernate.
     *
     * @return A estratégia (update, create, create-drop, validate).
     */
    public static String getHibernateHbm2ddlAuto() {
        return getProperty("hibernate.hbm2ddl.auto");
    }

    /**
     * Obtém o host do Redis.
     *
     * @return O host do Redis.
     */
    public static String getRedisHost() {
        String env = System.getenv("REDIS_HOST");
        if (env != null)
            return env;
        return getProperty("redis.host");
    }

    /**
     * Obtém a porta do Redis.
     *
     * @return A porta do Redis.
     */
    public static int getRedisPort() {
        String env = System.getenv("REDIS_PORT");
        if (env != null) {
            try {
                return Integer.parseInt(env);
            } catch (NumberFormatException e) {
                // Fallback
            }
        }
        return Integer.parseInt(getProperty("redis.port"));
    }

    /**
     * Obtém a senha do Redis.
     *
     * @return A senha do Redis.
     */
    public static String getRedisPassword() {
        String env = System.getenv("REDIS_PASSWORD"); // Nota: docker-compose não define REDIS_PASSWORD explicitamente
                                                      // para a app, mas run-app não define também.
                                                      // Porém, o RedisCacheManager verifica se é nulo.
        if (env != null)
            return env;
        return getProperty("redis.password");
    }

    /**
     * Obtém a URI de conexão do MongoDB.
     *
     * @return A URI do MongoDB.
     */
    public static String getMongoUri() {
        String env = System.getenv("MONGO_URI");
        if (env != null)
            return env;

        // Monta URI se parciais existirem
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

    /**
     * Obtém o nome do banco de dados MongoDB.
     *
     * @return O nome do database.
     */
    public static String getMongoDatabase() {
        String env = System.getenv("MONGO_DATABASE");
        if (env != null)
            return env;
        return getProperty("mongo.database");
    }
}
