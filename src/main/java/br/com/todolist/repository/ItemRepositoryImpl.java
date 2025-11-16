package br.com.todolist.repository;

import br.com.todolist.entity.DadosUsuario;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRepositoryImpl implements ItemRepository<Itens> {

    private static final String ARQUIVO_DADOS = "arquivos/dados_globais.json";
    private final GerenciadorDePersistenciaJson persistencia;
    private final List<Tarefa> tarefas;
    private final List<Evento> eventos;

    public ItemRepositoryImpl() {
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
    public void salvar(Itens item) {
        if (item instanceof Tarefa) {
            tarefas.add((Tarefa) item);
        } else if (item instanceof Evento) {
            eventos.add((Evento) item);
        }
        salvarDados();
    }

    @Override
    public void excluir(Itens item) {
        if (item instanceof Tarefa) {
            tarefas.remove(item);
        } else if (item instanceof Evento) {
            eventos.remove(item);
        }
        salvarDados();
    }

    @Override
    public List<Itens> buscarTodos() {
        List<Itens> todos = new ArrayList<>();
        todos.addAll(tarefas);
        todos.addAll(eventos);
        return todos;
    }

    public List<Tarefa> buscarTodasTarefas() {
        return new ArrayList<>(tarefas);
    }

    public List<Evento> buscarTodosEventos() {
        return new ArrayList<>(eventos);
    }

    @Override
    public void atualizar(Itens item) {
        salvarDados();
    }
}