package br.com.todolist.util.relatorio;

import java.util.List;

/**
 * Interface estendida de IGeradorRelatorio que permite geração de relatórios
 * com suporte a colunas extras/adicionais.
 * 
 * Útil para formatos que suportam funcionalidades mais avançadas,
 * como planilhas Excel com múltiplas abas ou colunas calculadas.
 */
public interface IGeradorRelatorioAvancado extends IGeradorRelatorio {

    /**
     * Gera um relatório com os dados fornecidos e uma coluna extra.
     *
     * @param nomeArquivo O caminho/nome do arquivo a ser gerado.
     * @param titulo      O título do relatório (ou nome da planilha para Excel).
     * @param cabecalhos  Um array de strings com os cabeçalhos das colunas.
     * @param dados       Uma lista de arrays de strings, onde cada array representa
     *                    uma linha.
     * @param colunaExtra Uma lista de strings para uma coluna adicional.
     */
    void gerarRelatorioComColunaExtra(String nomeArquivo, String titulo, String[] cabecalhos,
            List<String[]> dados, List<String> colunaExtra);
}
