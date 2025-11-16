package br.com.todolist.strategy;

import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;

/**
 * Estratégia padrão para cálculo de progresso de tarefas.
 * Calcula o progresso com base na porcentagem de subtarefas concluídas.
 */
public class DefaultProgressCalculationStrategy implements ProgressCalculationStrategy {

    @Override
    public double calcularProgresso(Tarefa tarefa) {
        if (tarefa.getSubtarefas().isEmpty()) {
            return tarefa.getDataConclusao() != null ? 100.0 : 0.0;
        }

        long subtarefasConcluidas = 0;
        for (Subtarefa subtarefa : tarefa.getSubtarefas()) {
            if (subtarefa.isStatus()) {
                subtarefasConcluidas++;
            }
        }

        return ((double) subtarefasConcluidas / tarefa.getSubtarefas().size()) * 100;
    }
}
