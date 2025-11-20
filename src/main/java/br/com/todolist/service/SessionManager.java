package br.com.todolist.service;

import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.EventoRepositoryImpl;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.repository.TarefaRepositoryImpl;
import br.com.todolist.service.IEventService;
import br.com.todolist.service.ITaskService;
import br.com.todolist.service.impl.EventServiceImpl;
import br.com.todolist.service.impl.TaskServiceImpl;

/**
 * Gerenciador de sessão do usuário.
 * Implementado como um Singleton para garantir que haja apenas uma instância da classe em toda a aplicação.
 */
public class SessionManager {

    private static final SessionManager instance = new SessionManager();
    private Usuario usuarioLogado;
    private ITaskService taskService;
    private IEventService eventService;

    private SessionManager() {}

    /**
     * Retorna a instância única do SessionManager.
     *
     * @return a instância do SessionManager.
     */
    public static SessionManager getInstance() {
        return instance;
    }

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
        if (usuario != null) {
            ITarefaRepository tarefaRepository = new TarefaRepositoryImpl();
            IEventoRepository eventoRepository = new EventoRepositoryImpl();
            this.taskService = new TaskServiceImpl(tarefaRepository, usuario.getEmail());
            this.eventService = new EventServiceImpl(eventoRepository, usuario.getEmail());
        } else {
            this.taskService = null;
            this.eventService = null;
        }
    }

    public void logout() {
        this.usuarioLogado = null;
        this.taskService = null;
        this.eventService = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public ITaskService getTaskService() {
        return taskService;
    }

    public IEventService getEventService() {
        return eventService;
    }

    public String getEmailUsuarioLogado() {
        return (usuarioLogado != null) ? usuarioLogado.getEmail() : null;
    }
}
