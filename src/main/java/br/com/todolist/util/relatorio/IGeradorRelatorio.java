package br.com.todolist.util.relatorio;

import java.util.List;

public interface IGeradorRelatorio {
    void gerarRelatorio(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados);
}