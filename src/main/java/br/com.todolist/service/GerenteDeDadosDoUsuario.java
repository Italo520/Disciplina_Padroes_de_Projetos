package br.com.todolist.service;

import br.com.todolist.entity.DadosUsuario;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.GerenciadorDePersistenciaJson;
import java.util.ArrayList;
import java.util.List;

public class GerenteDeDadosDoUsuario {

    private static final String ARQUIVO_DADOS = "arquivos/dados_globais.json";
    private final GerenciadorDePersistenciaJson persistencia;
    private List<Tarefa> tarefas;
    private List<Evento> eventos;

    public GerenteDeDadosDoUsuario() {
        this.persistencia = new GerenciadorDePersistenciaJson(ARQUIVO_DADOS);
        carregarDados();
    }

    private void carregarDados() {
        DadosUsuario dados = (DadosUsuario) persistencia.carregar(DadosUsuario.class);
        if (dados != null) {
            this.tarefas = dados.getTarefas() != null ? dados.getTarefas() : new ArrayList<>();
            this.eventos = dados.getEventos() != null ? dados.getEventos() : new ArrayList<>();
        } else {
            this.tarefas = new ArrayList<>();
            this.eventos = new ArrayList<>();
        }
    }

    public void salvarDados() {
        DadosUsuario dadosParaSalvar = new DadosUsuario(tarefas, eventos);
        persistencia.salvar(dadosParaSalvar);
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
}