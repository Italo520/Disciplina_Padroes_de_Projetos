package br.com.todolist.entity;

import br.com.todolist.service.util.DefaultProgressCalculationStrategy;
import br.com.todolist.service.util.IProgressCalculationStrategy;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Classe que representa uma Tarefa no sistema.
 * Estende a classe abstrata Itens e adiciona funcionalidades específicas como prioridade,
 * data de conclusão, subtarefas e cálculo de progresso.
 */
public class Tarefa extends Itens {

    private LocalDate dataConclusao;
    private int prioridade;
    private List<Subtarefa> subtarefas;
    private IProgressCalculationStrategy progressCalculationStrategy;

    /**
     * Construtor padrão da classe Tarefa.
     */
    public Tarefa() {
    }

    /**
     * Construtor da classe Tarefa.
     *
     * @param titulo       O título da tarefa.
     * @param descricao    A descrição da tarefa.
     * @param criado_por   O e-mail do usuário que criou a tarefa.
     * @param deadline     A data limite da tarefa.
     * @param prioridade   A prioridade da tarefa.
     */
    public Tarefa(String titulo, String descricao, String criado_por, LocalDate deadline, int prioridade) {
        super(titulo, descricao, "Tarefa", criado_por, deadline);
        this.prioridade = prioridade;
        this.dataConclusao = null;
        this.subtarefas = new ArrayList<>();
        this.progressCalculationStrategy = new DefaultProgressCalculationStrategy();
    }

    /**
     * Calcula o percentual de conclusão da tarefa.
     * Utiliza a estratégia de cálculo definida (Strategy Pattern).
     *
     * @return O percentual de conclusão (0.0 a 100.0).
     */
    public double obterPercentual() {
        return progressCalculationStrategy.calcularProgresso(this);
    }

    /**
     * Obtém a data de conclusão da tarefa.
     *
     * @return A data de conclusão, ou null se não estiver concluída.
     */
    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    /**
     * Define a data de conclusão da tarefa.
     *
     * @param dataConclusao A nova data de conclusão.
     */
    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    /**
     * Obtém a prioridade da tarefa.
     *
     * @return A prioridade da tarefa.
     */
    public int getPrioridade() {
        return prioridade;
    }

    /**
     * Define a prioridade da tarefa.
     *
     * @param prioridade A nova prioridade.
     */
    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * Obtém a lista de subtarefas associadas à tarefa.
     *
     * @return A lista de subtarefas.
     */
    public List<Subtarefa> getSubtarefas() {
        return subtarefas;
    }

    /**
     * Define a lista de subtarefas associadas à tarefa.
     *
     * @param subtarefas A nova lista de subtarefas.
     */
    public void setSubtarefas(List<Subtarefa> subtarefas) {
        this.subtarefas = subtarefas;
    }

    /**
     * Adiciona uma nova subtarefa à lista.
     *
     * @param subtarefa A subtarefa a ser adicionada.
     */
    public void adicionarSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.add(subtarefa);
    }

    /**
     * Remove uma subtarefa da lista.
     *
     * @param subtarefa A subtarefa a ser removida.
     */
    public void removerSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.remove(subtarefa);
    }

    /**
     * Retorna uma representação em String da tarefa (o título).
     *
     * @return O título da tarefa.
     */
    public String toString() {
        return getTitulo();
    }

    /**
     * Define a estratégia de cálculo de progresso da tarefa.
     * Permite alterar dinamicamente como o progresso é calculado.
     *
     * @param progressCalculationStrategy A nova estratégia de cálculo.
     */
    public void setProgressCalculationStrategy(IProgressCalculationStrategy progressCalculationStrategy) {
        this.progressCalculationStrategy = progressCalculationStrategy;
    }
}
