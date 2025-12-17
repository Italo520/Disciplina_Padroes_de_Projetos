package br.com.todolist.service.util;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import java.time.LocalDate;

public interface IItemFactory {

    Tarefa criarTarefa(String titulo, String descricao, String criadoPor, LocalDate deadline, int prioridade);

    Evento criarEvento(String titulo, String descricao, String criadoPor, LocalDate deadline);
}