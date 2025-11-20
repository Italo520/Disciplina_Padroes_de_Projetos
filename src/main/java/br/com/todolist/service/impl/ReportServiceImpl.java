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
     * Construtor da classe ReportServiceImpl.
     *
     * @param taskService O serviço de tarefas utilizado para obter os dados dos relatórios.
     * @param mensageiro  O utilitário de envio de e-mails para despachar os relatórios.
     */
    public ReportServiceImpl(ITaskService taskService, Mensageiro mensageiro) {
        this.taskService = taskService;
        this.mensageiro = mensageiro;
    }

    /**
     * Gera um relatório em PDF com as tarefas de um dia específico e envia por e-mail para o usuário.
     * O arquivo temporário gerado é excluído após o envio.
     *
     * @param dia     O dia para o qual o relatório será gerado.
     * @param usuario O usuário que receberá o relatório.
     * @return true se o e-mail foi enviado com sucesso, false caso não existam tarefas ou ocorra erro no envio.
     */
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

    /**
     * Gera um relatório em formato Excel (XLSX) com todas as tarefas de um mês específico.
     *
     * @param mes         O mês e ano de referência para o relatório.
     * @param nomeArquivo O nome do arquivo Excel a ser gerado.
     */
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
