package br.com.todolist.repository.cache;

import br.com.todolist.entity.Evento;
import br.com.todolist.repository.IEventoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

/**
 * Decorator para adicionar cache Redis ao repositório de Eventos.
 */
public class CachedEventoRepository implements IEventoRepository {

    private final IEventoRepository decoratedRepository;
    private final RedisCacheManager cacheManager;
    private final ObjectMapper objectMapper;
    private static final int TTL = 300; // 5 minutos

    public CachedEventoRepository(IEventoRepository decoratedRepository) {
        this.decoratedRepository = decoratedRepository;
        this.cacheManager = RedisCacheManager.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Evento buscarPorId(String id) {
        String key = "evento:" + id;
        String json = cacheManager.buscar(key);

        if (json != null) {
            try {
                return objectMapper.readValue(json, Evento.class);
            } catch (Exception e) {
                System.err.println("Erro ao deserializar Evento do cache: " + e.getMessage());
            }
        }

        Evento evento = decoratedRepository.buscarPorId(id);
        if (evento != null) {
            try {
                String jsonValue = objectMapper.writeValueAsString(evento);
                cacheManager.salvar(key, jsonValue, TTL);
            } catch (Exception e) {
                System.err.println("Erro ao serializar Evento para o cache: " + e.getMessage());
            }
        }
        return evento;
    }

    @Override
    public void salvar(Evento entity) {
        decoratedRepository.salvar(entity);
        cacheManager.remover("evento:" + entity.getTitulo());
    }

    @Override
    public void atualizar(Evento entity) {
        decoratedRepository.atualizar(entity);
        cacheManager.remover("evento:" + entity.getTitulo());
    }

    @Override
    public void excluir(Evento entity) {
        decoratedRepository.excluir(entity);
        cacheManager.remover("evento:" + entity.getTitulo());
    }

    @Override
    public List<Evento> buscarTodos() {
        return decoratedRepository.buscarTodos();
    }
}
