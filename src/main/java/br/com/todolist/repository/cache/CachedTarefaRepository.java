package br.com.todolist.repository.cache;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.ITarefaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Decorator para adicionar cache Redis ao repositório de Tarefas.
 */
public class CachedTarefaRepository implements ITarefaRepository {

    private static final Logger LOGGER = Logger.getLogger(CachedTarefaRepository.class.getName());
    private static final String CACHE_KEY_PREFIX = "tarefa:";
    private static final int TTL = 300; // 5 minutos

    private final ITarefaRepository decoratedRepository;
    private final RedisCacheManager cacheManager;
    private final ObjectMapper objectMapper;

    public CachedTarefaRepository(ITarefaRepository decoratedRepository) {
        this.decoratedRepository = decoratedRepository;
        this.cacheManager = RedisCacheManager.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Tarefa buscarPorId(String id) {
        String key = CACHE_KEY_PREFIX + id;
        String json = cacheManager.buscar(key);

        if (json != null) {
            try {
                return objectMapper.readValue(json, Tarefa.class);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e, () -> "Erro ao deserializar Tarefa do cache: " + e.getMessage());
                // Se falhar na deserialização, fallback para o banco
            }
        }

        Tarefa tarefa = decoratedRepository.buscarPorId(id);
        if (tarefa != null) {
            try {
                String jsonValue = objectMapper.writeValueAsString(tarefa);
                cacheManager.salvar(key, jsonValue, TTL);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e, () -> "Erro ao serializar Tarefa para o cache: " + e.getMessage());
            }
        }
        return tarefa;
    }

    @Override
    public void salvar(Tarefa entity) {
        decoratedRepository.salvar(entity);
        cacheManager.remover(CACHE_KEY_PREFIX + entity.getTitulo());
    }

    @Override
    public Tarefa atualizar(Tarefa entity) {
        Tarefa updated = decoratedRepository.atualizar(entity);
        cacheManager.remover(CACHE_KEY_PREFIX + entity.getTitulo());
        return updated;
    }

    @Override
    public void excluir(Tarefa entity) {
        decoratedRepository.excluir(entity);
        cacheManager.remover(CACHE_KEY_PREFIX + entity.getTitulo());
    }

    @Override
    public List<Tarefa> buscarTodos() {
        return decoratedRepository.buscarTodos();
    }
}
