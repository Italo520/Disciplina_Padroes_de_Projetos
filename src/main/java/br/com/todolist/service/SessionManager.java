package br.com.todolist.service;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.postgres.EventoRepositoryPostgres;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.repository.postgres.TarefaRepositoryPostgres;
import br.com.todolist.service.impl.EventServiceImpl;
import br.com.todolist.service.impl.TaskServiceImpl;

/**
 * Gerenciador de sessão do usuário.
 * Utiliza o padrão Singleton para garantir uma única instância global.
 * Mantém o estado do usuário logado e seus serviços associados (Tarefas e
 * Eventos).
 */
public class SessionManager {

    private static final SessionManager instance = new SessionManager();
    private Usuario usuarioLogado;
    private ITaskService taskService;
    private IEventService eventService;

    /**
     * Construtor privado para impedir a criação direta de instâncias.
     */
    private SessionManager() {
    }

    /**
     * Retorna a instância única do SessionManager.
     *
     * @return A instância singleton.
     */
    public static SessionManager getInstance() {
        return instance;
    }

    /**
     * Realiza o login do usuário no gerenciador de sessão.
     * Inicializa os serviços de Tarefa e Evento específicos para o usuário.
     *
     * @param usuario O usuário que efetuou login.
     */
    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
        if (usuario != null) {
            ITarefaRepository tarefaRepository = new TarefaRepositoryPostgres();
            IEventoRepository eventoRepository = new EventoRepositoryPostgres();
            this.taskService = new TaskServiceImpl(tarefaRepository, usuario.getEmail());
            this.eventService = new EventServiceImpl(eventoRepository, usuario.getEmail());
        } else {
            this.taskService = null;
            this.eventService = null;
        }
    }

    /**
     * Realiza o logout do usuário atual.
     * Limpa as referências ao usuário e seus serviços.
     */
    public void logout() {
        this.usuarioLogado = null;
        this.taskService = null;
        this.eventService = null;
    }

    /**
     * Obtém o usuário atualmente logado.
     *
     * @return O objeto Usuario logado, ou null se não houver sessão ativa.
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * Obtém o serviço de tarefas do usuário logado.
     *
     * @return A instância de ITaskService, ou null se não houver usuário logado.
     */
    public ITaskService getTaskService() {
        return taskService;
    }

    /**
     * Obtém o serviço de eventos do usuário logado.
     *
     * @return A instância de IEventService, ou null se não houver usuário logado.
     */
    public IEventService getEventService() {
        return eventService;
    }

    /**
     * Helper para obter o e-mail do usuário logado.
     *
     * @return O e-mail do usuário, ou null se não houver sessão ativa.
     */
    public String getEmailUsuarioLogado() {
        return (usuarioLogado != null) ? usuarioLogado.getEmail() : null;
    }
}
