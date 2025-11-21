package br.com.todolist.repository.cache;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.ITarefaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

/**
 * Decorator para adicionar cache Redis ao repositório de Tarefas.
 */
public class CachedTarefaRepository implements ITarefaRepository {

    private final ITarefaRepository decoratedRepository;
    private final RedisCacheManager cacheManager;
    private final ObjectMapper objectMapper;
    private static final int TTL = 300; // 5 minutos

    public CachedTarefaRepository(ITarefaRepository decoratedRepository) {
        this.decoratedRepository = decoratedRepository;
        this.cacheManager = RedisCacheManager.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Tarefa buscarPorId(String id) {
        String key = "tarefa:" + id;
        String json = cacheManager.buscar(key);

        if (json != null) {
            try {
                return objectMapper.readValue(json, Tarefa.class);
            } catch (Exception e) {
                System.err.println("Erro ao deserializar Tarefa do cache: " + e.getMessage());
                // Se falhar na deserialização, fallback para o banco
            }
        }

        Tarefa tarefa = decoratedRepository.buscarPorId(id);
        if (tarefa != null) {
            try {
                String jsonValue = objectMapper.writeValueAsString(tarefa);
                cacheManager.salvar(key, jsonValue, TTL);
            } catch (Exception e) {
                System.err.println("Erro ao serializar Tarefa para o cache: " + e.getMessage());
            }
        }
        return tarefa;
    }

    @Override
    public void salvar(Tarefa entity) {
        decoratedRepository.salvar(entity);
        cacheManager.remover("tarefa:" + entity.getTitulo());
    }

    @Override
    public void atualizar(Tarefa entity) {
        decoratedRepository.atualizar(entity);
        cacheManager.remover("tarefa:" + entity.getTitulo());
    }

    @Override
    public void excluir(Tarefa entity) {
        decoratedRepository.excluir(entity);
        cacheManager.remover("tarefa:" + entity.getTitulo());
    }

    @Override
    public List<Tarefa> buscarTodos() {
        return decoratedRepository.buscarTodos();
    }
}
