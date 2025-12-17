package br.com.todolist.service;

import br.com.todolist.entity.Usuario;

public class SessionManager {

    private static final SessionManager instance = new SessionManager();
    private Usuario usuarioLogado;
    private ITaskService taskService;
    private IEventService eventService;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void registrarSessao(Usuario usuario, ITaskService taskService, IEventService eventService) {
        this.usuarioLogado = usuario;
        this.taskService = taskService;
        this.eventService = eventService;
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