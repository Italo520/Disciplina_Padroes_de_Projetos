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

/**
 * Implementação do serviço de geração de relatórios.
 * Contém a lógica de negócio para criar e enviar relatórios de tarefas.
 * 
 * Segue os princípios SOLID:
 * - SRP: Responsável apenas pela lógica de negócio de relatórios
 * - OCP: Aberto para extensão (novos tipos de relatórios/notificações) fechado
 * para modificação
 * - DIP: Depende de abstrações (INotificador, IGeradorRelatorio) não de
 * implementações concretas
 */
public class ReportServiceImpl implements IReportService {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(ReportServiceImpl.class.getName());
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private final ITaskService taskService;
    private final INotificador notificador;
    private final IGeradorRelatorio geradorPDF;
    private final IGeradorRelatorioAvancado geradorExcel;

    /**
     * Construtor da classe ReportServiceImpl.
     *
     * @param taskService  O serviço de tarefas utilizado para obter os dados dos
     *                     relatórios.
     * @param notificador  O notificador para envio de relatórios (e-mail, WhatsApp,
     *                     etc.).
     * @param geradorPDF   O gerador de relatórios em formato PDF.
     * @param geradorExcel O gerador de relatórios em formato Excel.
     */
    public ReportServiceImpl(ITaskService taskService, INotificador notificador,
            IGeradorRelatorio geradorPDF, IGeradorRelatorioAvancado geradorExcel) {
        this.taskService = taskService;
        this.notificador = notificador;
        this.geradorPDF = geradorPDF;
        this.geradorExcel = geradorExcel;
    }

    /**
     * Gera um relatório em PDF com as tarefas de um dia específico e envia por
     * notificação para o usuário.
     * O arquivo temporário gerado é excluído após o envio.
     *
     * @param dia     O dia para o qual o relatório será gerado.
     * @param usuario O usuário que receberá o relatório.
     * @return true se a notificação foi enviada com sucesso, false caso não existam
     *         tarefas ou ocorra erro no envio.
     */
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

        // Usa o gerador de PDF através da interface
        geradorPDF.gerarRelatorio(nomeArquivo, tituloRelatorio, cabecalhos, dados);

        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia "
                + dia.format(DateTimeFormatter.ofPattern(DATE_PATTERN)) + ".\n\nAtenciosamente,\nSistema ToDoList.";

        // Usa o notificador através da interface
        boolean sucesso = notificador.enviarNotificacaoComAnexo(usuario.getEmail(), assunto, corpo, nomeArquivo);

        // Tenta deletar o arquivo temporário
        try {
            java.nio.file.Files.delete(java.nio.file.Paths.get(nomeArquivo));
        } catch (java.io.IOException e) {
            // Log de falha na exclusão, mas não afeta o retorno do sucesso do envio
            LOGGER.log(java.util.logging.Level.WARNING, "Aviso: Não foi possível deletar o arquivo temporário: {0}",
                    nomeArquivo);
        }
        return sucesso;
    }

    /**
     * Gera um relatório em formato Excel (XLSX) com todas as tarefas de um mês
     * específico.
     *
     * @param mes         O mês e ano de referência para o relatório.
     * @param nomeArquivo O nome do arquivo Excel a ser gerado.
     */
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

        // Usa o gerador de Excel através da interface
        geradorExcel.gerarRelatorioComColunaExtra(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
    }
}
