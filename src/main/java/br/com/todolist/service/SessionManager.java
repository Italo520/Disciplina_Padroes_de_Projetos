package br.com.todolist.service;

import br.com.todolist.entity.Usuario;

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
     * Registra a sessão do usuário com os serviços já configurados.
     *
     * @param usuario      O usuário logado.
     * @param taskService  O serviço de tarefas configurado (com cache/logs).
     * @param eventService O serviço de eventos configurado (com cache/logs).
     */
    public void registrarSessao(Usuario usuario, ITaskService taskService, IEventService eventService) {
        this.usuarioLogado = usuario;
        this.taskService = taskService;
        this.eventService = eventService;
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
