package br.com.todolist.unit.service;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.ITaskService;
import br.com.todolist.service.impl.ReportServiceImpl;
import br.com.todolist.util.Mensageiro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportServiceImpl - Unit Tests")
class ReportServiceImplTest {

    @Mock
    private ITaskService taskService;

    @Mock
    private Mensageiro mensageiro;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    @DisplayName("Deve enviar relatório de tarefas do dia por email")
    void shouldSendDailyReportByEmail() {
        // Arrange
        LocalDate dia = LocalDate.now();
        Usuario usuario = new Usuario("User", "user@test.com", "pass");
        Tarefa tarefa = new Tarefa("Task 1", "Desc", "user@test.com", dia, 1);

        when(taskService.listarTarefasPorDia(dia)).thenReturn(List.of(tarefa));
        when(mensageiro.enviarEmailComAnexo(eq(usuario.getEmail()), anyString(), anyString(), anyString()))
                .thenReturn(true);

        // Act
        boolean result = reportService.enviarRelatorioTarefasDoDiaPorEmail(dia, usuario);

        // Assert
        assertThat(result).isTrue();
        verify(taskService).listarTarefasPorDia(dia);
        verify(mensageiro).enviarEmailComAnexo(eq(usuario.getEmail()), contains("Relatório de Tarefas"),
                contains("Segue em anexo"), contains(".pdf"));
    }

    @Test
    @DisplayName("Deve gerar relatório de tarefas por mês (PDF)")
    void shouldGenerateMonthlyReport() {
        // Arrange
        YearMonth mes = YearMonth.now();
        String nomeArquivo = "relatorio_mensal.xlsx";
        Tarefa tarefa = new Tarefa("Task 1", "Desc", "user@test.com", LocalDate.now(), 1);

        when(taskService.listarTodasTarefas()).thenReturn(List.of(tarefa));

        // Act
        reportService.gerarRelatorioTarefasPorMes(mes, nomeArquivo);

        // Assert
        verify(taskService).listarTodasTarefas();
        // Cleanup created file
        new java.io.File(nomeArquivo).delete();
    }

    @Test
    @DisplayName("Deve lidar com lista vazia ao enviar email")
    void shouldHandleEmptyListWhenSendingEmail() {
        // Arrange
        LocalDate dia = LocalDate.now();
        Usuario usuario = new Usuario("User", "user@test.com", "pass");

        when(taskService.listarTarefasPorDia(dia)).thenReturn(Collections.emptyList());

        // Act
        boolean result = reportService.enviarRelatorioTarefasDoDiaPorEmail(dia, usuario);

        // Assert
        assertThat(result).isFalse();
        verify(mensageiro, never()).enviarEmail(anyString(), anyString(), anyString());
        verify(mensageiro, never()).enviarEmailComAnexo(anyString(), anyString(), anyString(), anyString());
    }
}
