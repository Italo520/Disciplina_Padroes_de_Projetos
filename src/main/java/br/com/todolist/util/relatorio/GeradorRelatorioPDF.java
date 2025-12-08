package br.com.todolist.util.relatorio;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.IOException;
import java.util.List;

/**
 * Implementação concreta de IGeradorRelatorio para geração de relatórios em
 * formato PDF.
 * Utiliza a biblioteca iText para criação de documentos PDF.
 * 
 * Esta classe implementa o Padrão Strategy, permitindo que o tipo de relatório
 * seja trocado facilmente sem modificar o código que utiliza o gerador.
 * 
 * Segue o Princípio da Responsabilidade Única (SRP) - sua única
 * responsabilidade
 * é gerar relatórios em formato PDF.
 */
public class GeradorRelatorioPDF implements IGeradorRelatorio {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(GeradorRelatorioPDF.class.getName());

    /**
     * Gera um arquivo PDF contendo uma tabela com os dados fornecidos.
     *
     * @param nomeArquivo O caminho/nome do arquivo PDF a ser gerado.
     * @param titulo      O título a ser exibido no topo do documento.
     * @param cabecalhos  Um array de strings com os cabeçalhos da tabela.
     * @param dados       Uma lista de arrays de strings, onde cada array representa
     *                    uma linha da tabela.
     */
    @Override
    public void gerarRelatorio(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados) {
        try (PdfWriter writer = new PdfWriter(nomeArquivo)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adiciona o título
            document.add(new Paragraph(titulo).setFontSize(20).setTextAlignment(TextAlignment.CENTER));

            // Adiciona um espaço
            document.add(new Paragraph("\n"));

            // Adiciona a tabela com os cabeçalhos
            Table table = new Table(cabecalhos.length);
            for (String cabecalho : cabecalhos) {
                table.addHeaderCell(cabecalho);
            }

            // Adiciona os dados
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
