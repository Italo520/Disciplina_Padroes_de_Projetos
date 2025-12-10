package br.com.todolist.unit.patterns.strategy;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.report.GeradorRelatorioExcel;
import br.com.todolist.service.report.GeradorRelatorioPDF;
import br.com.todolist.service.report.IGeradorRelatorio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Report - Strategy Pattern Tests")
class ReportStrategyTest {

    private static final String PDF_FILE = "test_report.pdf";
    private static final String EXCEL_FILE = "test_report.xlsx";

    @AfterEach
    void tearDown() {
        new File(PDF_FILE).delete();
        new File(EXCEL_FILE).delete();
    }

    @Test
    @DisplayName("Deve gerar PDF usando GeradorRelatorioPDF")
    void shouldGeneratePDF_When_UsingPDFStrategy() {
        // Arrange
        IGeradorRelatorio strategy = new GeradorRelatorioPDF();
        List<Tarefa> tarefas = criarTarefasExemplo();

        // Act
        strategy.gerar(PDF_FILE, "Relatório Teste", tarefas);

        // Assert
        File file = new File(PDF_FILE);
        assertThat(file.exists()).isTrue();
        assertThat(file.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Deve gerar Excel usando GeradorRelatorioExcel")
    void shouldGenerateExcel_When_UsingExcelStrategy() {
        // Arrange
        IGeradorRelatorio strategy = new GeradorRelatorioExcel();
        List<Tarefa> tarefas = criarTarefasExemplo();

        // Act
        strategy.gerar(EXCEL_FILE, "Planilha Teste", tarefas);

        // Assert
        File file = new File(EXCEL_FILE);
        assertThat(file.exists()).isTrue();
        assertThat(file.length()).isGreaterThan(0);
    }

    private List<Tarefa> criarTarefasExemplo() {
        List<Tarefa> tarefas = new ArrayList<>();
        tarefas.add(new Tarefa("Tarefa 1", "Desc 1", "user@test.com", LocalDate.now(), 1));
        tarefas.add(new Tarefa("Tarefa 2", "Desc 2", "user@test.com", LocalDate.now().plusDays(1), 2));
        return tarefas;
    }
}
