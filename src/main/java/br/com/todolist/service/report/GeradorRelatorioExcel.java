package br.com.todolist.service.report;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.util.Central;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GeradorRelatorioExcel implements IGeradorRelatorio {

    @Override
    public void gerar(String nomeArquivo, String titulo, List<Tarefa> tarefas) {
        String[] cabecalhos = { "Título", "Descrição", "Prioridade", "Prazo", "Conclusão (%)" };
        List<String[]> dados = new ArrayList<>();
        List<String> colunaExtra = new ArrayList<>();

        for (Tarefa t : tarefas) {
            String prazo = t.getDeadline() != null ? t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "N/A";
            dados.add(new String[] {
                    t.getTitulo(),
                    t.getDescricao(),
                    String.valueOf(t.getPrioridade()),
                    prazo
            });
            colunaExtra.add(String.format("%.0f%%", t.obterPercentual()));
        }
        Central.gerarExcel(nomeArquivo, titulo, cabecalhos, dados, colunaExtra);
    }
}
