package br.com.todolist.service.report;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.util.Central;
import java.util.ArrayList;
import java.util.List;

public class GeradorRelatorioPDF implements IGeradorRelatorio {

    @Override
    public void gerar(String nomeArquivo, String titulo, List<Tarefa> tarefas) {
        String[] cabecalhos = { "Título", "Descrição", "Prioridade" };
        List<String[]> dados = new ArrayList<>();
        for (Tarefa t : tarefas) {
            dados.add(new String[] { t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade()) });
        }
        Central.gerarPdf(nomeArquivo, titulo, cabecalhos, dados);
    }
}
