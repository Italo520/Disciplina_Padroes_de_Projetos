package br.com.todolist.controller;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.ITaskService;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador responsável pelo gerenciamento de tarefas.
 * Intermedeia as operações entre a interface gráfica e o serviço de tarefas.
 */
public class TaskController {

    private final ITaskService taskService;

    /**
     * Construtor da classe TaskController.
     *
     * @param taskService O serviço de tarefas a ser utilizado pelo controlador.
     */
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Cadastra uma nova tarefa.
     *
     * @param tarefa O objeto Tarefa a ser cadastrado.
     * @throws BusinessException se houver erro na validação ou persistência.
     */
    public void cadastrarTarefa(Tarefa tarefa) throws BusinessException {
        taskService.cadastrarTarefa(tarefa);
    }

    /**
     * Lista todas as tarefas cadastradas.
     *
     * @return Uma lista contendo todas as tarefas.
     */
    public List<Tarefa> listarTodasTarefas() {
        return taskService.listarTodasTarefas();
    }

    /**
     * Exclui uma tarefa existente.
     *
     * @param tarefa A tarefa a ser excluída.
     * @throws BusinessException se houver erro ao excluir.
     */
    public void excluirTarefa(Tarefa tarefa) throws BusinessException {
        taskService.excluirTarefa(tarefa);
    }

    /**
     * Edita os dados de uma tarefa existente.
     *
     * @param tarefaOriginal O objeto Tarefa original que será modificado.
     * @param novoTitulo     O novo título da tarefa.
     * @param novaDescricao  A nova descrição da tarefa.
     * @param novoDeadline   A nova data limite da tarefa.
     * @param novaPrioridade A nova prioridade da tarefa.
     * @throws BusinessException se houver erro ao editar.
     */
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) throws BusinessException {
        taskService.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
    }

    /**
     * Atualiza o estado de uma tarefa no sistema.
     * Geralmente utilizado para persistir mudanças no status ou subtarefas.
     *
     * @param tarefa A tarefa com os dados atualizados.
     * @return A tarefa atualizada.
     * @throws BusinessException se houver erro ao atualizar.
     */
    public Tarefa atualizarTarefa(Tarefa tarefa) throws BusinessException {
        return taskService.atualizarTarefa(tarefa);
    }

    /**
     * Lista as tarefas agendadas para um dia específico.
     *
     * @param dia A data para a qual se deseja listar as tarefas.
     * @return Uma lista de tarefas agendadas para o dia especificado.
     */
    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return taskService.listarTarefasPorDia(dia);
    }

    /**
     * Lista as tarefas consideradas críticas (ex: alta prioridade ou prazo
     * próximo).
     *
     * @return Uma lista de tarefas críticas.
     */
    public List<Tarefa> listarTarefasCriticas() {
        return taskService.listarTarefasCriticas();
    }
}
