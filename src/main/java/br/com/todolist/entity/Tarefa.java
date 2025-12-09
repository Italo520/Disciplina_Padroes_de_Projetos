package br.com.todolist.entity;

import br.com.todolist.service.util.DefaultProgressCalculationStrategy;
import br.com.todolist.service.util.IProgressCalculationStrategy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Classe que representa uma Tarefa no sistema.
 * Estende a classe abstrata Itens e adiciona funcionalidades específicas como
 * prioridade,
 * data de conclusão, subtarefas e cálculo de progresso.
 */
@Entity
@Table(name = "tarefas")
public class Tarefa extends Itens {

    private LocalDate dataConclusao;
    private int prioridade;
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Subtarefa> subtarefas;
    @Transient
    private IProgressCalculationStrategy progressCalculationStrategy;

    /**
     * Construtor padrão da classe Tarefa.
     */
    public Tarefa() {
        this.progressCalculationStrategy = new DefaultProgressCalculationStrategy();
    }

    /**
     * Construtor da classe Tarefa.
     *
     * @param titulo     O título da tarefa.
     * @param descricao  A descrição da tarefa.
     * @param criado_por O e-mail do usuário que criou a tarefa.
     * @param deadline   A data limite da tarefa.
     * @param prioridade A prioridade da tarefa.
     */
    public Tarefa(String titulo, String descricao, String criadoPor, LocalDate deadline, int prioridade) {
        super(titulo, descricao, "Tarefa", criadoPor, deadline);
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
        if (progressCalculationStrategy == null) {
            progressCalculationStrategy = new DefaultProgressCalculationStrategy();
        }
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
        subtarefa.setTarefa(this);
    }

    /**
     * Remove uma subtarefa da lista.
     *
     * @param subtarefa A subtarefa a ser removida.
     */
    public void removerSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.remove(subtarefa);
        subtarefa.setTarefa(null);
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

    /**
     * Cria uma cópia profunda da tarefa.
     *
     * @return Uma nova instância de Tarefa com os mesmos dados.
     */
    public Tarefa copiar() {
        Tarefa copia = new Tarefa(getTitulo(), getDescricao(), getCriadoPor(), getDeadline(), getPrioridade());
        copia.setDataConclusao(this.dataConclusao);
        copia.setTipo(getTipo());
        copia.setDataCadastro(getDataCadastro());

        List<Subtarefa> subCopia = new ArrayList<>();
        if (this.subtarefas != null) {
            for (Subtarefa s : this.subtarefas) {
                Subtarefa sCopia = s.copiar();
                sCopia.setTarefa(copia);
                subCopia.add(sCopia);
            }
        }
        copia.setSubtarefas(subCopia);
        return copia;
    }
}
