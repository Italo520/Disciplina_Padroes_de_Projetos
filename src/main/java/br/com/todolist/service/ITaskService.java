package br.com.todolist.service;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.event.TaskEvent;
import br.com.todolist.service.util.ITaskObserver;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para o serviço de tarefas.
 * Define os métodos que devem ser implementados pelas classes de serviço de
 * tarefas.
 */
public interface ITaskService {

    /**
     * Cadastra uma nova tarefa.
     *
     * @param tarefa a tarefa a ser cadastrada.
     * @throws BusinessException se houver erro na validação ou persistência.
     */
    void cadastrarTarefa(Tarefa tarefa) throws BusinessException;

    /**
     * Exclui uma tarefa.
     *
     * @param tarefa a tarefa a ser excluída.
     * @throws BusinessException se houver erro ao excluir.
     */
    void excluirTarefa(Tarefa tarefa) throws BusinessException;

    /**
     * Edita uma tarefa.
     *
     * @param tarefaOriginal a tarefa original.
     * @param novoTitulo     o novo título da tarefa.
     * @param novaDescricao  a nova descrição da tarefa.
     * @param novoDeadline   o novo prazo da tarefa.
     * @param novaPrioridade a nova prioridade da tarefa.
     * @throws BusinessException se houver erro ao editar.
     */
    void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) throws BusinessException;

    /**
     * Atualiza uma tarefa.
     *
     * @param tarefa a tarefa a ser atualizada.
     * @throws BusinessException se houver erro ao atualizar.
     */
    void atualizarTarefa(Tarefa tarefa) throws BusinessException;

    /**
     * Lista todas as tarefas.
     *
     * @return uma lista com todas as tarefas.
     */
    List<Tarefa> listarTodasTarefas();

    /**
     * Lista as tarefas de um dia específico.
     *
     * @param dia o dia para o qual as tarefas devem ser listadas.
     * @return uma lista com as tarefas do dia.
     */
    List<Tarefa> listarTarefasPorDia(LocalDate dia);

    /**
     * Lista as tarefas consideradas críticas.
     *
     * @return uma lista com as tarefas críticas.
     */
    List<Tarefa> listarTarefasCriticas();

    /**
     * Adiciona um observador à lista de observadores.
     *
     * @param observer o observador a ser adicionado.
     */
    void addObserver(ITaskObserver observer);

    /**
     * Remove um observador da lista de observadores.
     *
     * @param observer o observador a ser removido.
     */
    void removeObserver(ITaskObserver observer);

    /**
     * Notifica todos os observadores sobre uma atualização.
     *
     * @param object o objeto que foi atualizado.
     */
    void notifyObservers(TaskEvent object);
}
