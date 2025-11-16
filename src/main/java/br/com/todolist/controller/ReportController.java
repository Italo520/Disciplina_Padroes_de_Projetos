package br.com.todolist.controller;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.TaskService;
import br.com.todolist.util.Central;
import br.com.todolist.util.Mensageiro;
import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReportController {

    private final TaskService taskService;
    private final Mensageiro mensageiro;
    private final String emailUsuario;

    public ReportController(TaskService taskService, String emailUsuario) {
        this.taskService = taskService;
        this.emailUsuario = emailUsuario;
        this.mensageiro = new Mensageiro();
    }

    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia) {
        List<Tarefa> tarefas = taskService.listarTarefasPorDia(dia);
        if (tarefas == null || tarefas.isEmpty()) {
            return false;
        }
        String nomeArquivo = "Relatorio_Tarefas_" + dia.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] cabecalhos = {"Título", "Descrição", "Prioridade"};
        List<String[]> dados = tarefas.stream()
                .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade())})
                .collect(Collectors.toList());
        Central.gerarPdf(nomeArquivo, tituloRelatorio, cabecalhos, dados);
        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n\nAtenciosamente,\nSistema ToDoList.";
        boolean sucesso = mensageiro.enviarEmailComAnexo(emailUsuario, assunto, corpo, nomeArquivo);
        new File(nomeArquivo).delete();
        return sucesso;
    }

    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        List<Tarefa> tarefasDoMes = taskService.listarTodasTarefas().stream()
                .filter(t -> YearMonth.from(t.getDeadline()).equals(mes))
                .collect(Collectors.toList());
        String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo", "Conclusão (%)"};
        List<String[]> dados = tarefasDoMes.stream()
                .map(t -> new String[]{
                        t.getTitulo(),
                        t.getDescricao(),
                        String.valueOf(t.getPrioridade()),
                        t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                })
                .collect(Collectors.toList());
        List<String> colunaExtra = tarefasDoMes.stream()
                .map(t -> String.format("%.0f%%", t.obterPercentual()))
                .collect(Collectors.toList());
        String nomePlanilha = "Tarefas de " + mes.format(DateTimeFormatter.ofPattern("MM-yyyy"));
        Central.gerarExcel(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
    }
}