package br.com.todolist;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.cache.RedisCacheManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class MainTesteCache {
    public static void main(String[] args) {
        try {
            System.out.println("=== INICIANDO TESTE DE CACHE ===");
            System.out.println("1. Testando conexao Redis...");
            RedisCacheManager cache = RedisCacheManager.getInstance();
            cache.salvar("teste:ping", "pong", 60);
            String valor = cache.buscar("teste:ping");
            System.out.println("   Redis Ping Resultado: " + valor);
            if (!"pong".equals(valor)) {
                throw new RuntimeException("Falha no Redis: valor retornado diferente de pong");
            }

            System.out.println("2. Testando serializacao Jackson...");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            Tarefa t = new Tarefa("TituloTeste", "DescricaoTeste", "email@teste.com", LocalDate.now(), 1);
            t.setId(1L);
            List<Tarefa> lista = Collections.singletonList(t);
            String json = mapper.writeValueAsString(lista);
            System.out.println("   JSON Gerado: " + json);
            System.out.println("3. Testando salvar JSON no Redis...");
            cache.salvar("teste:tarefa", json, 60);
            String jsonCache = cache.buscar("teste:tarefa");
            System.out.println("   JSON recuperado do Cache: " + jsonCache);
            if (jsonCache == null || !jsonCache.equals(json)) {
                throw new RuntimeException("Falha no Cache: JSON recuperado diferente do original");
            }

            System.out.println("4. Testando deserializacao...");
            List<Tarefa> l2 = mapper.readValue(jsonCache,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Tarefa>>() {
                    });
            System.out.println("   Tarefa deserializada: " + l2.get(0).getTitulo());
            System.out.println("=== TESTE CONCLUIDO COM SUCESSO ===");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("=== ERRO NO TESTE ===");
            e.printStackTrace();
            System.exit(1);
        }
    }
}