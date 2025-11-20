package br.com.todolist.service.impl;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.IReportService;
import br.com.todolist.service.ITaskService;
import br.com.todolist.util.Central;
import br.com.todolist.util.Mensageiro;
import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do serviço de geração de relatórios.
 * Contém a lógica de negócio para criar e enviar relatórios de tarefas.
 */
public class ReportServiceImpl implements IReportService {

    private final ITaskService taskService;
    private final Mensageiro mensageiro;

    /**
     * Construtor da classe.
     *
     * @param taskService o serviço de tarefas a ser utilizado.
     * @param mensageiro o utilitário de envio de e-mails.
     */
    public ReportServiceImpl(ITaskService taskService, Mensageiro mensageiro) {
        this.taskService = taskService;
        this.mensageiro = mensageiro;
    }

    @Override
    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia, Usuario usuario) {
        List<Tarefa> tarefas = taskService.listarTarefasPorDia(dia);
        if (tarefas == null || tarefas.isEmpty()) {
            return false;
        }
        String nomeArquivo = "Relatorio_Tarefas_" + dia.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] cabecalhos = {"Título", "Descrição", "Prioridade"};
        List<String[]> dados = new ArrayList<>();
        for (Tarefa t : tarefas) {
            dados.add(new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade())});
        }
        Central.gerarPdf(nomeArquivo, tituloRelatorio, cabecalhos, dados);
        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n\nAtenciosamente,\nSistema ToDoList.";
        boolean sucesso = mensageiro.enviarEmailComAnexo(usuario.getEmail(), assunto, corpo, nomeArquivo);
        new File(nomeArquivo).delete();
        return sucesso;
    }

    @Override
    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        List<Tarefa> tarefasDoMes = new ArrayList<>();
        for (Tarefa t : taskService.listarTodasTarefas()) {
            if (YearMonth.from(t.getDeadline()).equals(mes)) {
                tarefasDoMes.add(t);
            }
        }
        String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo", "Conclusão (%)"};
        List<String[]> dados = new ArrayList<>();
        for (Tarefa t : tarefasDoMes) {
            dados.add(new String[]{
                    t.getTitulo(),
                    t.getDescricao(),
                    String.valueOf(t.getPrioridade()),
                    t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            });
        }
        List<String> colunaExtra = new ArrayList<>();
        for (Tarefa t : tarefasDoMes) {
            colunaExtra.add(String.format("%.0f%%", t.obterPercentual()));
        }
        String nomePlanilha = "Tarefas de " + mes.format(DateTimeFormatter.ofPattern("MM-yyyy"));
        Central.gerarExcel(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
    }
}
