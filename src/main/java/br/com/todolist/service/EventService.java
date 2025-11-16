package br.com.todolist.service;

import br.com.todolist.entity.Evento;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface EventService {
    boolean cadastrarEvento(String titulo, String descricao, LocalDate deadline);
    void excluirEvento(Evento evento);
    void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline);
    List<Evento> listarTodosEventos();
    List<Evento> listarEventosPorDia(LocalDate dia);
    List<Evento> listarEventosPorMes(YearMonth mes);
    String getEmailUsuario();
}