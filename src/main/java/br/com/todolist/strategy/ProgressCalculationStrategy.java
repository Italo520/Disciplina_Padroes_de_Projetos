package br.com.todolist.strategy;

import br.com.todolist.entity.Tarefa;

/**
 * Interface para a estratégia de cálculo de progresso de tarefas.
 * Define o método que deve ser implementado por todas as estratégias de cálculo de progresso.
 */
public interface ProgressCalculationStrategy {

    /**
     * Calcula o progresso de uma tarefa.
     *
     * @param tarefa a tarefa para a qual o progresso será calculado.
     * @return o progresso da tarefa, em percentual.
     */
    double calcularProgresso(Tarefa tarefa);
}
