package br.com.todolist.repository.cache;

import br.com.todolist.entity.Evento;
import br.com.todolist.repository.IEventoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CachedEventoRepository implements IEventoRepository {

    private static final Logger LOGGER = Logger.getLogger(CachedEventoRepository.class.getName());
    private static final String CACHE_KEY_PREFIX = "evento:";
    private static final int TTL = 300; 

    private final IEventoRepository decoratedRepository;
    private final RedisCacheManager cacheManager;
    private final ObjectMapper objectMapper;

    public CachedEventoRepository(IEventoRepository decoratedRepository) {
        this.decoratedRepository = decoratedRepository;
        this.cacheManager = RedisCacheManager.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Evento buscarPorId(Long id) {
        String key = CACHE_KEY_PREFIX + id;
        String json = cacheManager.buscar(key);

        if (json != null) {
            try {
                return objectMapper.readValue(json, Evento.class);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e, () -> "Erro ao deserializar Evento do cache: " + e.getMessage());
            }
        }

        Evento evento = decoratedRepository.buscarPorId(id);
        if (evento != null) {
            try {
                String jsonValue = objectMapper.writeValueAsString(evento);
                cacheManager.salvar(key, jsonValue, TTL);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e, () -> "Erro ao serializar Evento para o cache: " + e.getMessage());
            }
        }
        return evento;
    }

    @Override
    public void salvar(Evento entity) {
        decoratedRepository.salvar(entity);
        if (entity.getId() != null) {
            cacheManager.remover(CACHE_KEY_PREFIX + entity.getId());
        }
    }

    @Override
    public void atualizar(Evento entity) {
        decoratedRepository.atualizar(entity);
        if (entity.getId() != null) {
            cacheManager.remover(CACHE_KEY_PREFIX + entity.getId());
        }
    }

    @Override
    public void excluir(Evento entity) {
        decoratedRepository.excluir(entity);
        if (entity.getId() != null) {
            cacheManager.remover(CACHE_KEY_PREFIX + entity.getId());
        }
    }

    @Override
    public List<Evento> buscarTodos() {
        return decoratedRepository.buscarTodos();
    }
}