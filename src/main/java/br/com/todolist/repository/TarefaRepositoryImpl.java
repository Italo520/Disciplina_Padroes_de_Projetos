package br.com.todolist.repository;

import br.com.todolist.entity.DadosUsuario;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import com.fasterxml.jackson.core.type.TypeReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do repositório de tarefas.
 * Responsável por persistir e recuperar dados de tarefas em um arquivo JSON.
 */
public class TarefaRepositoryImpl implements ITarefaRepository {

    private static final String ARQUIVO_DADOS = "arquivos/dados_globais.json";
    private final GerenciadorDePersistenciaJson persistencia;
    private List<Tarefa> tarefas;
    private List<Evento> eventos; // Mantido para salvar o arquivo completo

    public TarefaRepositoryImpl() {
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
    public void salvar(Tarefa tarefa) {
        tarefas.add(tarefa);
        salvarDados();
    }

    @Override
    public void excluir(Tarefa tarefa) {
        tarefas.remove(tarefa);
        salvarDados();
    }

    @Override
    public void atualizar(Tarefa tarefa) {
        // A lógica de atualizar em lista simplesmente salva o estado atual
        salvarDados();
    }

    @Override
    public Tarefa buscarPorId(String id) {
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getTitulo().equals(id)) {
                return tarefa;
            }
        }
        return null;
    }

    @Override
    public List<Tarefa> buscarTodos() {
        return new ArrayList<>(tarefas);
    }
}
