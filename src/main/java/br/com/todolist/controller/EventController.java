package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.IEventService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class EventController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    public void cadastrarEvento(Evento evento) throws BusinessException {
        eventService.cadastrarEvento(evento);
    }

    public List<Evento> listarTodosEventos() {
        return eventService.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) throws BusinessException {
        eventService.excluirEvento(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) throws BusinessException {
        eventService.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return eventService.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return eventService.listarEventosPorMes(mes);
    }
}