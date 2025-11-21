package br.com.todolist.repository.cache;

import br.com.todolist.log.LogService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Gerenciador de Cache Redis (Singleton).
 * Encapsula a conexão com o Redis usando JedisPool.
 * Lida apenas com chaves e valores String (JSON).
 */
public class RedisCacheManager {

    private static RedisCacheManager instance;
    private final JedisPool jedisPool;

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    /**
     * Construtor privado. Inicializa o pool de conexões com o Redis.
     * Assume localhost e porta padrão 6379.
     */
    private RedisCacheManager() {
        // Configuração simples para localhost
        this.jedisPool = new JedisPool(REDIS_HOST, REDIS_PORT);
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
