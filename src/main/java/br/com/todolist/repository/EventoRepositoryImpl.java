package br.com.todolist.repository;

import br.com.todolist.entity.DadosUsuario;
import br.com.todolist.entity.Evento;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import br.com.todolist.entity.Tarefa;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do repositório de eventos.
 * Responsável por persistir e recuperar dados de eventos em um arquivo JSON.
 */
public class EventoRepositoryImpl implements IEventoRepository {

    @Override
    public List<Evento> buscarPorDia(LocalDate dia) {
        return Collections.emptyList();
    }

    @Override
    public List<Evento> buscarPorMes(YearMonth mes) {
        return Collections.emptyList();
    }

    private static final String ARQUIVO_DADOS = "arquivos/dados_globais.json";
    private final GerenciadorDePersistenciaJson persistencia;
    private List<Tarefa> tarefas; // Mantido para salvar o arquivo completo
    private List<Evento> eventos;

    /**
     * Construtor da classe EventoRepositoryImpl.
     * Inicializa o gerenciador de persistência e carrega os dados existentes.
     */
    public EventoRepositoryImpl() {
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
     * Adiciona um novo evento à lista e persiste as alterações.
     *
     * @param evento O evento a ser salvo.
     */
    @Override
    public void salvar(Evento evento) {
        eventos.add(evento);
        salvarDados();
    }

    /**
     * Remove um evento da lista e persiste as alterações.
     *
     * @param evento O evento a ser excluído.
     */
    @Override
    public void excluir(Evento evento) {
        eventos.remove(evento);
        salvarDados();
    }

    /**
     * Atualiza os dados persistidos.
     * Como a manipulação é feita por referência na lista em memória,
     * este método apenas dispara o salvamento no arquivo.
     *
     * @param evento O evento atualizado.
     */
    @Override
    public void atualizar(Evento evento) {
        // A lógica de atualizar em lista simplesmente salva o estado atual
        salvarDados();
    }

    /**
     * Busca um evento pelo seu título (utilizado como ID).
     *
     * @param id O título do evento.
     * @return O evento encontrado, ou null caso não exista.
     */
    @Override
    public Evento buscarPorId(String id) {
        for (Evento evento : eventos) {
            if (evento.getTitulo().equals(id)) {
                return evento;
            }
        }
        return null;
    }

    /**
     * Retorna uma lista com todos os eventos cadastrados.
     *
     * @return Uma nova lista contendo todos os eventos.
     */
    @Override
    public List<Evento> buscarTodos() {
        return new ArrayList<>(eventos);
    }
}
