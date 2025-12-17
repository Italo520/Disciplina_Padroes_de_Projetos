package br.com.todolist.service;

import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.event.CalendarEvent;
import br.com.todolist.service.util.IEventObserver;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface IEventService {

    void cadastrarEvento(Evento evento) throws BusinessException;

    void excluirEvento(Evento evento) throws BusinessException;

    void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline)
            throws BusinessException;

    List<Evento> listarTodosEventos();

    List<Evento> listarEventosPorDia(LocalDate dia);

    List<Evento> listarEventosPorMes(YearMonth mes);

    void addObserver(IEventObserver observer);

    void removeObserver(IEventObserver observer);

    void notifyObservers(CalendarEvent object);
}