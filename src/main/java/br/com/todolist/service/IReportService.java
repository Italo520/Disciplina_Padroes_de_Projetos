package br.com.todolist.service;

import br.com.todolist.entity.Usuario;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Interface para o serviço de geração de relatórios.
 * Define os métodos que devem ser implementados pelas classes de serviço de
 * relatório.
 */
public interface IReportService {

    /**
     * Gera um relatório de tarefas do dia em PDF e o envia por e-mail.
     *
     * @param dia     a data para a qual o relatório deve ser gerado.
     * @param usuario o usuário para quem o e-mail será enviado.
     * @return true se o e-mail foi enviado com sucesso, false caso contrário.
     */
    boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia, Usuario usuario);

    /**
     * Gera um relatório de tarefas do dia em formato PDF (apenas salva o arquivo).
     *
     * @param dia         a data para a qual o relatório deve ser gerado.
     * @param nomeArquivo o nome do arquivo PDF a ser criado.
     */
    void gerarRelatorioPDFTarefasDoDia(LocalDate dia, String nomeArquivo);

    /**
     * Gera um relatório de tarefas do mês em formato Excel.
     *
     * @param mes         o mês para o qual o relatório deve ser gerado.
     * @param nomeArquivo o nome do arquivo Excel a ser criado.
     */
    void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo);
}
