package br.com.todolist.service.util;

import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;

public class DefaultProgressCalculationStrategy implements IProgressCalculationStrategy {

    public DefaultProgressCalculationStrategy() {

    }

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