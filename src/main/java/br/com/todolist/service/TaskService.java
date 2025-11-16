package br.com.todolist.service;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.observer.Subject;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para o serviço de tarefas.
 * Define os métodos que devem ser implementados pelas classes de serviço de tarefas.
 */
public interface TaskService extends Subject<Tarefa> {

    /**
     * Cadastra uma nova tarefa.
     *
     * @param tarefa a tarefa a ser cadastrada.
     */
    void cadastrarTarefa(Tarefa tarefa);

    /**
     * Exclui uma tarefa.
     *
     * @param tarefa a tarefa a ser excluída.
     */
    void excluirTarefa(Tarefa tarefa);

    /**
     * Edita uma tarefa.
     *
     * @param tarefaOriginal a tarefa original.
     * @param novoTitulo o novo título da tarefa.
     * @param novaDescricao a nova descrição da tarefa.
     * @param novoDeadline o novo prazo da tarefa.
     * @param novaPrioridade a nova prioridade da tarefa.
     */
    void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade);

    /**
     * Atualiza uma tarefa.
     *
     * @param tarefa a tarefa a ser atualizada.
     */
    void atualizarTarefa(Tarefa tarefa);

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
}
