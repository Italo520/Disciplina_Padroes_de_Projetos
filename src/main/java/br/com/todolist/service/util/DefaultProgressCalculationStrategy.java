package br.com.todolist.service.util;

import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;

/**
 * Estratégia padrão para cálculo de progresso de tarefas.
 * Implementa a interface IProgressCalculationStrategy.
 */
public class DefaultProgressCalculationStrategy implements IProgressCalculationStrategy {

    /**
     * Construtor padrão da classe DefaultProgressCalculationStrategy.
     */
    public DefaultProgressCalculationStrategy() {
    }

    /**
     * Calcula o progresso de uma tarefa com base em suas subtarefas.
     * Se a tarefa não tiver subtarefas, o progresso é 100% se concluída, ou 0% caso contrário.
     * Se tiver subtarefas, o progresso é a proporção de subtarefas marcadas como concluídas.
     *
     * @param tarefa A tarefa a ser avaliada.
     * @return O percentual de conclusão (0.0 a 100.0).
     */
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
