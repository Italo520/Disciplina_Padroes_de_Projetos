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

@Entity
@Table(name = "tarefas")
public class Tarefa extends Itens {
    private LocalDate dataConclusao;
    private int prioridade;
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Subtarefa> subtarefas;
    @Transient
    @com.fasterxml.jackson.annotation.JsonIgnore
    private IProgressCalculationStrategy progressCalculationStrategy;

    public Tarefa() {
        this.progressCalculationStrategy = new DefaultProgressCalculationStrategy();
    }

    public Tarefa(String titulo, String descricao, String criadoPor, LocalDate deadline, int prioridade) {
        super(titulo, descricao, "Tarefa", criadoPor, deadline);
        this.prioridade = prioridade;
        this.dataConclusao = null;
        this.subtarefas = new ArrayList<>();
        this.progressCalculationStrategy = new DefaultProgressCalculationStrategy();
    }

    public double obterPercentual() {
        if (progressCalculationStrategy == null) {
            progressCalculationStrategy = new DefaultProgressCalculationStrategy();
        }

        return progressCalculationStrategy.calcularProgresso(this);
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public List<Subtarefa> getSubtarefas() {
        return subtarefas;
    }

    public void setSubtarefas(List<Subtarefa> subtarefas) {
        this.subtarefas = subtarefas;
    }

    public void adicionarSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.add(subtarefa);
        subtarefa.setTarefa(this);
    }

    public void removerSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.remove(subtarefa);
        subtarefa.setTarefa(null);
    }

    public String toString() {
        return getTitulo();
    }

    public void setProgressCalculationStrategy(IProgressCalculationStrategy progressCalculationStrategy) {
        this.progressCalculationStrategy = progressCalculationStrategy;
    }

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