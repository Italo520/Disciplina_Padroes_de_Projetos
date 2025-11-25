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

        // Processa variáveis de ambiente no formato ${VAR_NAME:default}
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
        return getProperty("db.url");
    }

    /**
     * Obtém o usuário do banco de dados PostgreSQL.
     *
     * @return O nome de usuário.
     */
    public static String getDbUser() {
        return getProperty("db.user");
    }

    /**
     * Obtém a senha do banco de dados PostgreSQL.
     *
     * @return A senha do usuário.
     */
    public static String getDbPassword() {
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
        return getProperty("redis.host");
    }

    /**
     * Obtém a porta do Redis.
     *
     * @return A porta do Redis.
     */
    public static int getRedisPort() {
        return Integer.parseInt(getProperty("redis.port"));
    }

    /**
     * Obtém a URI de conexão do MongoDB.
     *
     * @return A URI do MongoDB.
     */
    public static String getMongoUri() {
        return getProperty("mongo.uri");
    }

    /**
     * Obtém o nome do banco de dados MongoDB.
     *
     * @return O nome do database.
     */
    public static String getMongoDatabase() {
        return getProperty("mongo.database");
    }
}
