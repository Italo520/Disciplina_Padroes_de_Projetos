package br.com.todolist.repository.json;
import br.com.todolist.repository.ITarefaRepository;

import br.com.todolist.entity.DadosUsuario;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
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

    /**
     * Construtor da classe TarefaRepositoryImpl.
     * Inicializa o gerenciador de persistência e carrega os dados existentes.
     */
    public TarefaRepositoryImpl() {
        this.persistencia = new GerenciadorDePersistenciaJson(ARQUIVO_DADOS);
        DadosUsuario dados = carregarDados();
        this.tarefas = dados.getTarefas();
        this.eventos = dados.getEventos();
    }

    /**
     * Carrega os dados do arquivo JSON.
     *
     * @return Um objeto DadosUsuario contendo as listas de tarefas e eventos.
     */
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

    /**
     * Salva os dados atuais (listas de tarefas e eventos) no arquivo JSON.
     */
    private void salvarDados() {
        persistencia.salvar(new DadosUsuario(tarefas, eventos));
    }

    /**
     * Adiciona uma nova tarefa à lista e persiste as alterações.
     *
     * @param tarefa A tarefa a ser salva.
     */
    @Override
    public void salvar(Tarefa tarefa) {
        tarefas.add(tarefa);
        salvarDados();
    }

    /**
     * Remove uma tarefa da lista e persiste as alterações.
     *
     * @param tarefa A tarefa a ser excluída.
     */
    @Override
    public void excluir(Tarefa tarefa) {
        tarefas.remove(tarefa);
        salvarDados();
    }

    /**
     * Atualiza os dados persistidos.
     * Como a manipulação é feita por referência na lista em memória,
     * este método apenas dispara o salvamento no arquivo.
     *
     * @param tarefa A tarefa atualizada.
     */
    @Override
    public void atualizar(Tarefa tarefa) {
        // A lógica de atualizar em lista simplesmente salva o estado atual
        salvarDados();
    }

    /**
     * Busca uma tarefa pelo seu título (utilizado como ID).
     *
     * @param id O título da tarefa.
     * @return A tarefa encontrada, ou null caso não exista.
     */
    @Override
    public Tarefa buscarPorId(String id) {
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getTitulo().equals(id)) {
                return tarefa;
            }
        }
        return null;
    }

    /**
     * Retorna uma lista com todas as tarefas cadastradas.
     *
     * @return Uma nova lista contendo todas as tarefas.
     */
    @Override
    public List<Tarefa> buscarTodos() {
        return new ArrayList<>(tarefas);
    }
}
