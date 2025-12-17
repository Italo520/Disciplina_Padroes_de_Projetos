package br.com.todolist.service;

import br.com.todolist.entity.Usuario;
import java.time.LocalDate;
import java.time.YearMonth;

public interface IReportService {

    boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia, Usuario usuario);

    void gerarRelatorioPDFTarefasDoDia(LocalDate dia, String nomeArquivo);

    void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo);
}