package br.com.todolist.service.util;

import br.com.todolist.entity.Tarefa;

public interface IProgressCalculationStrategy {
    double calcularProgresso(Tarefa tarefa);
}