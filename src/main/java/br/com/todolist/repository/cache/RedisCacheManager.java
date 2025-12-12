package br.com.todolist.repository.cache;

import br.com.todolist.log.LogService;
import br.com.todolist.util.DatabaseConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCacheManager {
    private static RedisCacheManager instance;
    private final JedisPool jedisPool;

    private RedisCacheManager() {
        String redisHost = DatabaseConfig.getRedisHost();
        int redisPort = DatabaseConfig.getRedisPort();
        String redisPassword = DatabaseConfig.getRedisPassword();
        if (redisPassword != null && !redisPassword.isEmpty()) {
            this.jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort, 2000, redisPassword);
        } else {
            this.jedisPool = new JedisPool(redisHost, redisPort);
        }
    }

    public static synchronized RedisCacheManager getInstance() {
        if (instance == null) {
            instance = new RedisCacheManager();
        }

        return instance;
    }

    public void salvar(String key, String jsonValue, int ttlSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, ttlSeconds, jsonValue);
        } catch (Exception e) {
            e.printStackTrace();
            LogService.getInstance().logError(e);
        }
    }

    public String buscar(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            LogService.getInstance().logError(e);
            return null;
        }
    }

    public void remover(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            LogService.getInstance().logError(e);
        }
    }
}