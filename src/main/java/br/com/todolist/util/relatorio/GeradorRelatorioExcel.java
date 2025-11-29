package br.com.todolist.util.relatorio;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Implementação concreta de IGeradorRelatorioAvancado para geração de
 * relatórios em formato Excel (XLSX).
 * Utiliza a biblioteca Apache POI para criação de planilhas.
 * 
 * Esta classe implementa o Padrão Strategy, permitindo que o tipo de relatório
 * seja trocado facilmente sem modificar o código que utiliza o gerador.
 * 
 * Segue o Princípio da Responsabilidade Única (SRP) - sua única
 * responsabilidade
 * é gerar relatórios em formato Excel.
 */
public class GeradorRelatorioExcel implements IGeradorRelatorioAvancado {

    /**
     * Gera um arquivo Excel (XLSX) básico com os dados fornecidos.
     * O título será usado como nome da planilha.
     *
     * @param nomeArquivo O caminho/nome do arquivo Excel a ser gerado.
     * @param titulo      O nome da aba (planilha) dentro do arquivo.
     * @param cabecalhos  Um array de strings com os cabeçalhos das colunas.
     * @param dados       Uma lista de arrays de strings com os dados principais.
     */
    @Override
    public void gerarRelatorio(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados) {
        try (Workbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream = new FileOutputStream(nomeArquivo)) {

            Sheet sheet = workbook.createSheet(titulo);

            // Adiciona os cabeçalhos
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
            }

            // Adiciona os dados
            int rowNum = 1;
            for (String[] linhaDados : dados) {
                Row row = sheet.createRow(rowNum++);
                for (int j = 0; j < linhaDados.length; j++) {
                    row.createCell(j).setCellValue(linhaDados[j]);
                }
            }

            // Ajusta a largura das colunas
            for (int i = 0; i < cabecalhos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            System.out.println("Excel gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar o Excel: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar o Excel: " + e.getMessage(), e);
        }
    }

    /**
     * Gera um arquivo Excel (XLSX) com os dados fornecidos e uma coluna extra.
     *
     * @param nomeArquivo  O caminho/nome do arquivo Excel a ser gerado.
     * @param nomePlanilha O nome da aba (planilha) dentro do arquivo.
     * @param cabecalhos   Um array de strings com os cabeçalhos das colunas.
     * @param dados        Uma lista de arrays de strings com os dados principais.
     * @param colunaExtra  Uma lista de strings para uma coluna adicional (ex:
     *                     percentual de conclusão).
     */
    @Override
    public void gerarRelatorioComColunaExtra(String nomeArquivo, String nomePlanilha, String[] cabecalhos,
            List<String[]> dados, List<String> colunaExtra) {
        try (Workbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream = new FileOutputStream(nomeArquivo)) {

            Sheet sheet = workbook.createSheet(nomePlanilha);

            // Valida se a quantidade de linhas de dados e da coluna extra é a mesma
            if (dados.size() != colunaExtra.size()) {
                System.err.println(
                        "Erro: O número de linhas de dados não corresponde ao número de itens na coluna extra.");
                return;
            }

            // Adiciona os cabeçalhos
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
            }

            // Adiciona os dados e a coluna extra
            int rowNum = 1;
            for (int i = 0; i < dados.size(); i++) {
                String[] linhaDados = dados.get(i);
                String textoExtra = colunaExtra.get(i);

                Row row = sheet.createRow(rowNum++);
                // Popula as células com os dados da lista
                for (int j = 0; j < linhaDados.length; j++) {
                    row.createCell(j).setCellValue(linhaDados[j]);
                }
                // Adiciona a coluna extra na última posição
                row.createCell(linhaDados.length).setCellValue(textoExtra);
            }

            // Ajusta a largura das colunas
            for (int i = 0; i < cabecalhos.length + 1; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            System.out.println("Excel gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar o Excel: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar o Excel: " + e.getMessage(), e);
        }
    }
}
