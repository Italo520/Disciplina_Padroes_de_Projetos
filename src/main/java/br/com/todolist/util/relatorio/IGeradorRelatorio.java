package br.com.todolist.util.relatorio;

import java.util.List;

/**
 * Interface que define o contrato para geração de relatórios.
 * Permite que diferentes tipos de relatórios (PDF, Excel, CSV, etc.)
 * sejam implementados seguindo o mesmo contrato.
 * 
 * Esta interface segue o Padrão Strategy, permitindo que o tipo de relatório
 * seja alterado em tempo de execução sem modificar o código cliente.
 */
public interface IGeradorRelatorio {

    /**
     * Gera um relatório com os dados fornecidos.
     *
     * @param nomeArquivo O caminho/nome do arquivo a ser gerado.
     * @param titulo      O título do relatório.
     * @param cabecalhos  Um array de strings com os cabeçalhos das colunas.
     * @param dados       Uma lista de arrays de strings, onde cada array representa
     *                    uma linha.
     */
    void gerarRelatorio(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados);
}
