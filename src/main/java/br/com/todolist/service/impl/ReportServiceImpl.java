package br.com.todolist.service.impl;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.IReportService;
import br.com.todolist.service.ITaskService;
import br.com.todolist.util.notificacao.INotificador;
import br.com.todolist.util.relatorio.IGeradorRelatorio;
import br.com.todolist.util.relatorio.IGeradorRelatorioAvancado;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl implements IReportService {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(ReportServiceImpl.class.getName());
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private final ITaskService taskService;
    private final INotificador notificador;
    private final IGeradorRelatorio geradorPDF;
    private final IGeradorRelatorioAvancado geradorExcel;

    public ReportServiceImpl(ITaskService taskService, INotificador notificador,
            IGeradorRelatorio geradorPDF, IGeradorRelatorioAvancado geradorExcel) {
        this.taskService = taskService;
        this.notificador = notificador;
        this.geradorPDF = geradorPDF;
        this.geradorExcel = geradorExcel;
    }

    @Override
    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia, Usuario usuario) {
        List<Tarefa> tarefas = taskService.listarTarefasPorDia(dia);
        if (tarefas == null || tarefas.isEmpty()) {
            return false;
        }
        String nomeArquivo = "Relatorio_Tarefas_" + dia.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        String[] cabecalhos = { "Título", "Descrição", "Prioridade" };
        List<String[]> dados = new ArrayList<>();
        for (Tarefa t : tarefas) {
            dados.add(new String[] { t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade()) });
        }

        geradorPDF.gerarRelatorio(nomeArquivo, tituloRelatorio, cabecalhos, dados);

        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia "
                + dia.format(DateTimeFormatter.ofPattern(DATE_PATTERN)) + ".\n\nAtenciosamente,\nSistema ToDoList.";

        boolean sucesso = notificador.enviarNotificacaoComAnexo(usuario.getEmail(), assunto, corpo, nomeArquivo);

        try {
            java.nio.file.Files.delete(java.nio.file.Paths.get(nomeArquivo));
        } catch (java.io.IOException e) {

            LOGGER.log(java.util.logging.Level.WARNING, "Aviso: Não foi possível deletar o arquivo temporário: {0}",
                    nomeArquivo);
        }
        return sucesso;
    }

    @Override
    public void gerarRelatorioPDFTarefasDoDia(LocalDate dia, String nomeArquivo) {
        List<Tarefa> tarefas = taskService.listarTarefasPorDia(dia);
        if (tarefas == null || tarefas.isEmpty()) {
            throw new IllegalArgumentException("Não existem tarefas para a data especificada.");
        }
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] cabecalhos = { "Título", "Descrição", "Prioridade" };
        List<String[]> dados = new ArrayList<>();
        for (Tarefa t : tarefas) {
            dados.add(new String[] { t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade()) });
        }

        geradorPDF.gerarRelatorio(nomeArquivo, tituloRelatorio, cabecalhos, dados);
    }

    @Override
    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        List<Tarefa> tarefasDoMes = new ArrayList<>();
        for (Tarefa t : taskService.listarTodasTarefas()) {
            if (t.getDeadline() != null && YearMonth.from(t.getDeadline()).equals(mes)) {
                tarefasDoMes.add(t);
            }
        }
        String[] cabecalhos = { "Título", "Descrição", "Prioridade", "Prazo", "Conclusão (%)" };
        List<String[]> dados = new ArrayList<>();
        for (Tarefa t : tarefasDoMes) {
            dados.add(new String[] {
                    t.getTitulo(),
                    t.getDescricao(),
                    String.valueOf(t.getPrioridade()),
                    t.getDeadline().format(DateTimeFormatter.ofPattern(DATE_PATTERN)),
            });
        }
        List<String> colunaExtra = new ArrayList<>();
        for (Tarefa t : tarefasDoMes) {
            colunaExtra.add(String.format("%.0f%%", t.obterPercentual()));
        }
        String nomePlanilha = "Tarefas de " + mes.format(DateTimeFormatter.ofPattern("MM-yyyy"));

        geradorExcel.gerarRelatorioComColunaExtra(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
    }
}