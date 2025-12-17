package br.com.todolist.util.relatorio;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeradorRelatorioExcel implements IGeradorRelatorioAvancado {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(GeradorRelatorioExcel.class.getName());

    @Override
    public void gerarRelatorio(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados) {
        try (Workbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream = new FileOutputStream(nomeArquivo)) {

            Sheet sheet = workbook.createSheet(titulo);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
            }

            int rowNum = 1;
            for (String[] linhaDados : dados) {
                Row row = sheet.createRow(rowNum++);
                for (int j = 0; j < linhaDados.length; j++) {
                    row.createCell(j).setCellValue(linhaDados[j]);
                }
            }

            for (int i = 0; i < cabecalhos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            LOGGER.info(() -> "Excel gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e, () -> "Erro ao gerar o Excel: " + e.getMessage());
        }
    }

    @Override
    public void gerarRelatorioComColunaExtra(String nomeArquivo, String nomePlanilha, String[] cabecalhos,
            List<String[]> dados, List<String> colunaExtra) {
        try (Workbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream = new FileOutputStream(nomeArquivo)) {

            Sheet sheet = workbook.createSheet(nomePlanilha);

            if (dados.size() != colunaExtra.size()) {
                LOGGER.severe("Erro: O número de linhas de dados não corresponde ao número de itens na coluna extra.");
                return;
            }

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
            }

            int rowNum = 1;
            for (int i = 0; i < dados.size(); i++) {
                String[] linhaDados = dados.get(i);
                String textoExtra = colunaExtra.get(i);

                Row row = sheet.createRow(rowNum++);

                for (int j = 0; j < linhaDados.length; j++) {
                    row.createCell(j).setCellValue(linhaDados[j]);
                }

                row.createCell(linhaDados.length).setCellValue(textoExtra);
            }

            for (int i = 0; i < cabecalhos.length + 1; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            LOGGER.info(() -> "Excel gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e, () -> "Erro ao gerar o Excel: " + e.getMessage());
        }
    }
}