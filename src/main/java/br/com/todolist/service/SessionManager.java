package br.com.todolist.service;

import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.ItemRepository;
import br.com.todolist.repository.ItemRepositoryImpl;

/**
 * Gerenciador de sessão do usuário.
 * Implementado como um Singleton para garantir que haja apenas uma instância da classe em toda a aplicação.
 */
public class SessionManager {

    private static final SessionManager instance = new SessionManager();
    private Usuario usuarioLogado;
    private TaskService taskService;
    private EventService eventService;

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
            ItemRepository<Itens> itemRepository = new ItemRepositoryImpl();
            this.taskService = new TaskServiceImpl(itemRepository, usuario.getEmail());
            this.eventService = new EventServiceImpl(itemRepository, usuario.getEmail());
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

    public TaskService getTaskService() {
        return taskService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public String getEmailUsuarioLogado() {
        return (usuarioLogado != null) ? usuarioLogado.getEmail() : null;
    }
}
