package br.com.todolist.repository;

import br.com.todolist.entity.DadosUsuario;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import com.fasterxml.jackson.core.type.TypeReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do repositório de eventos.
 * Responsável por persistir e recuperar dados de eventos em um arquivo JSON.
 */
public class EventoRepositoryImpl implements IEventoRepository {

    private static final String ARQUIVO_DADOS = "arquivos/dados_globais.json";
    private final GerenciadorDePersistenciaJson persistencia;
    private List<Tarefa> tarefas; // Mantido para salvar o arquivo completo
    private List<Evento> eventos;

    public EventoRepositoryImpl() {
        this.persistencia = new GerenciadorDePersistenciaJson(ARQUIVO_DADOS);
        DadosUsuario dados = carregarDados();
        this.tarefas = dados.getTarefas();
        this.eventos = dados.getEventos();
    }

    private DadosUsuario carregarDados() {
        DadosUsuario dados = persistencia.carregar(DadosUsuario.class);
        if (dados == null) {
            return new DadosUsuario(new ArrayList<>(), new ArrayList<>());
        }
        if (dados.getTarefas() == null) {
            dados.setTarefas(new ArrayList<>());
        }
        if (dados.getEventos() == null) {
            dados.setEventos(new ArrayList<>());
        }
        return dados;
    }

    private void salvarDados() {
        persistencia.salvar(new DadosUsuario(tarefas, eventos));
    }

    @Override
    public void salvar(Evento evento) {
        eventos.add(evento);
        salvarDados();
    }

    @Override
    public void excluir(Evento evento) {
        eventos.remove(evento);
        salvarDados();
    }

    @Override
    public void atualizar(Evento evento) {
        // A lógica de atualizar em lista simplesmente salva o estado atual
        salvarDados();
    }

    @Override
    public Evento buscarPorId(String id) {
        for (Evento evento : eventos) {
            if (evento.getTitulo().equals(id)) {
                return evento;
            }
        }
        return null;
    }

    @Override
    public List<Evento> buscarTodos() {
        return new ArrayList<>(eventos);
    }
}
