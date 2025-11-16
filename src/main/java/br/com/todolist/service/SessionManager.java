package br.com.todolist.service;

import br.com.todolist.entity.Itens;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.ItemRepository;
import br.com.todolist.repository.ItemRepositoryImpl;

public class SessionManager {

    private static SessionManager instance;
    private Usuario usuarioLogado;
    private TaskService taskService;
    private EventService eventService;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
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