package br.com.todolist.service.report;

import br.com.todolist.entity.Tarefa;
import java.util.List;

public interface IGeradorRelatorio {
    void gerar(String nomeArquivo, String titulo, List<Tarefa> tarefas);
}
