package br.com.todolist.util.relatorio;

import java.util.List;

public interface IGeradorRelatorioAvancado extends IGeradorRelatorio {
    void gerarRelatorioComColunaExtra(String nomeArquivo, String titulo, String[] cabecalhos,
            List<String[]> dados, List<String> colunaExtra);
}