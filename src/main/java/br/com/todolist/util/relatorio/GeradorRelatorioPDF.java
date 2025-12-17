package br.com.todolist.util.relatorio;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.IOException;
import java.util.List;

public class GeradorRelatorioPDF implements IGeradorRelatorio {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(GeradorRelatorioPDF.class.getName());

    @Override
    public void gerarRelatorio(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados) {
        try (PdfWriter writer = new PdfWriter(nomeArquivo)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph(titulo).setFontSize(20).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            Table table = new Table(cabecalhos.length);
            for (String cabecalho : cabecalhos) {
                table.addHeaderCell(cabecalho);
            }

            for (String[] linha : dados) {
                for (String celula : linha) {
                    table.addCell(celula);
                }
            }

            document.add(table);
            document.close();
            LOGGER.info(() -> "PDF gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e, () -> "Erro ao gerar o PDF: " + e.getMessage());
        }
    }
}