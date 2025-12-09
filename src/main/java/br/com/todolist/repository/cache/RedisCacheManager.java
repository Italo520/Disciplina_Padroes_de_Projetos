package br.com.todolist.repository.cache;

import br.com.todolist.log.LogService;
import br.com.todolist.util.DatabaseConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Gerenciador de Cache Redis (Singleton).
 * Encapsula a conexão com o Redis usando JedisPool.
 * Lida apenas com chaves e valores String (JSON).
 */
public class RedisCacheManager {

    private static RedisCacheManager instance;
    private final JedisPool jedisPool;

    /**
     * Construtor privado. Inicializa o pool de conexões com o Redis.
     * Usa configurações do arquivo database.properties.
     */
    private RedisCacheManager() {
        // Carrega configurações do arquivo de propriedades
        String redisHost = DatabaseConfig.getRedisHost();
        int redisPort = DatabaseConfig.getRedisPort();
        String redisPassword = DatabaseConfig.getRedisPassword();

        if (redisPassword != null && !redisPassword.isEmpty()) {
            // Timeout padrão de 2000ms
            this.jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort, 2000, redisPassword);
        } else {
            this.jedisPool = new JedisPool(redisHost, redisPort);
        }
    }

    /**
     * Retorna a instância única do RedisCacheManager.
     *
     * @return A instância Singleton.
     */
    public static synchronized RedisCacheManager getInstance() {
        if (instance == null) {
            instance = new RedisCacheManager();
        }
        return instance;
    }

    /**
     * Salva um valor no cache com um tempo de vida (TTL).
     *
     * @param key        A chave para armazenamento.
     * @param jsonValue  O valor em formato JSON.
     * @param ttlSeconds O tempo de vida em segundos.
     */
    public void salvar(String key, String jsonValue, int ttlSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, ttlSeconds, jsonValue);
        } catch (Exception e) {
            LogService.getInstance().logError(e);
        }
    }

    /**
     * Busca um valor no cache pela chave.
     *
     * @param key A chave a ser buscada.
     * @return O valor (JSON) armazenado, ou null se não existir ou ocorrer erro.
     */
    public String buscar(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            LogService.getInstance().logError(e);
            return null;
        }
    }

    /**
     * Remove uma chave do cache.
     *
     * @param key A chave a ser removida.
     */
    public void remover(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            LogService.getInstance().logError(e);
        }
    }
}
