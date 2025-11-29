package br.com.todolist.service.impl;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.DadosInvalidosException;
import br.com.todolist.exception.DatabaseException;
import br.com.todolist.log.AuditAction;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.service.ITaskService;
import br.com.todolist.service.event.TaskEvent;
import br.com.todolist.service.util.ITaskObserver;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do serviço de tarefas.
 * Contém a lógica de negócio para gerenciar tarefas e notificar observadores
 * sobre mudanças.
 * Filtra as tarefas pelo e-mail do usuário logado.
 */
public class TaskServiceImpl implements ITaskService {

    private final ITarefaRepository tarefaRepository;
    private final String emailUsuario;
    private final List<ITaskObserver> observers = new ArrayList<>();

    /**
     * Construtor da classe TaskServiceImpl.
     *
     * @param tarefaRepository O repositório de tarefas a ser utilizado.
     * @param emailUsuario     O e-mail do usuário cujas tarefas serão gerenciadas.
     */
    public TaskServiceImpl(ITarefaRepository tarefaRepository, String emailUsuario) {
        this.tarefaRepository = tarefaRepository;
        this.emailUsuario = emailUsuario;
    }

    /**
     * Cadastra uma nova tarefa para o usuário.
     * Notifica os observadores após o cadastro.
     *
     * @param tarefa A tarefa a ser cadastrada.
     * @throws BusinessException se houver erro na validação ou persistência.
     */
    @Override
    public void cadastrarTarefa(Tarefa tarefa) throws BusinessException {
        if (tarefa == null) {
            throw new DadosInvalidosException("Tarefa não pode ser nula.");
        }
        try {
            tarefaRepository.salvar(tarefa);
            notifyObservers(new TaskEvent(AuditAction.CREATE, tarefa));
        } catch (DatabaseException e) {
            throw new BusinessException("Erro ao salvar tarefa.", e);
        }
    }

    /**
     * Exclui uma tarefa, garantindo que pertença ao usuário logado.
     * Notifica os observadores após a exclusão.
     *
     * @param tarefa A tarefa a ser excluída.
     * @throws BusinessException se a tarefa não pertencer ao usuário ou houver
     *                           erro.
     */
    @Override
    public void excluirTarefa(Tarefa tarefa) throws BusinessException {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            try {
                tarefaRepository.excluir(tarefa);
                notifyObservers(new TaskEvent(AuditAction.DELETE, tarefa));
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao excluir tarefa.", e);
            }
        } else {
            throw new DadosInvalidosException("Tarefa não pertence ao usuário logado.");
        }
    }

    /**
     * Edita os detalhes de uma tarefa existente, se pertencer ao usuário.
     *
     * @param tarefaOriginal A tarefa original.
     * @param novoTitulo     O novo título.
     * @param novaDescricao  A nova descrição.
     * @param novoDeadline   O novo prazo.
     * @param novaPrioridade A nova prioridade.
     * @throws BusinessException se houver erro ao editar.
     */
    @Override
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) throws BusinessException {
        if (tarefaOriginal.getCriado_por().equals(emailUsuario)) {
            Tarefa oldTarefa = tarefaOriginal.copiar();

            tarefaOriginal.setTitulo(novoTitulo);
            tarefaOriginal.setDescricao(novaDescricao);
            tarefaOriginal.setDeadLine(novoDeadline);
            tarefaOriginal.setPrioridade(novaPrioridade);
            try {
                tarefaRepository.atualizar(tarefaOriginal);
                notifyObservers(new TaskEvent(AuditAction.UPDATE, tarefaOriginal, oldTarefa));
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao atualizar tarefa.", e);
            }
        } else {
            throw new DadosInvalidosException("Tarefa não pertence ao usuário logado.");
        }
    }

    /**
     * Atualiza o estado de uma tarefa (ex: conclusão, subtarefas).
     *
     * @param tarefa A tarefa com os dados atualizados.
     * @throws BusinessException se houver erro ao atualizar.
     */
    @Override
    public Tarefa atualizarTarefa(Tarefa tarefa) throws BusinessException {
        if (tarefa.getCriado_por().equals(emailUsuario)) {
            try {
                // O título é o ID da entidade Tarefa neste sistema (PK).
                Tarefa oldTarefa = tarefaRepository.buscarPorId(tarefa.getTitulo());
                if (oldTarefa != null) {
                    oldTarefa = oldTarefa.copiar();
                }
                Tarefa updatedTask = tarefaRepository.atualizar(tarefa);
                notifyObservers(new TaskEvent(AuditAction.UPDATE, updatedTask, oldTarefa));
                return updatedTask;
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao atualizar tarefa.", e);
            }
        } else {
            throw new DadosInvalidosException("Tarefa não pertence ao usuário logado.");
        }
    }

    /**
     * Lista todas as tarefas do usuário logado.
     *
     * @return Uma lista de tarefas filtrada pelo e-mail do usuário.
     */
    @Override
    public List<Tarefa> listarTodasTarefas() {
        return tarefaRepository.buscarTodos().stream()
                .filter(tarefa -> tarefa.getCriado_por().equals(emailUsuario))
                .toList();
    }

    /**
     * Lista as tarefas do usuário para um dia específico.
     *
     * @param dia O dia a ser consultado.
     * @return Uma lista de tarefas do dia.
     */
    @Override
    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return listarTodasTarefas().stream()
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .toList();
    }

    /**
     * Lista as tarefas críticas do usuário.
     * O critério de criticidade é baseado na proximidade do prazo e na prioridade.
     *
     * @return Uma lista de tarefas críticas.
     */
    @Override
    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return listarTodasTarefas().stream()
                .filter(tarefa -> ChronoUnit.DAYS.between(hoje, tarefa.getDeadline()) - tarefa.getPrioridade() < 0)
                .toList();
    }

    /**
     * Adiciona um observador para receber notificações sobre mudanças nas tarefas.
     *
     * @param observer O observador a ser adicionado.
     */
    @Override
    public void addObserver(ITaskObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove um observador.
     *
     * @param observer O observador a ser removido.
     */
    @Override
    public void removeObserver(ITaskObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores registrados sobre uma mudança em uma tarefa.
     *
     * @param event O evento de mudança da tarefa.
     */
    @Override
    public void notifyObservers(TaskEvent event) {
        for (ITaskObserver observer : observers) {
            observer.update(event);
        }
    }
}
