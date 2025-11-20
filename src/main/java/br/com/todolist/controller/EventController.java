package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.service.IEventService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class EventController {

    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    public boolean cadastrarEvento(Evento evento) {
        return eventService.cadastrarEvento(evento);
    }

    public List<Evento> listarTodosEventos() {
        return eventService.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) {
        eventService.excluirEvento(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        eventService.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return eventService.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return eventService.listarEventosPorMes(mes);
    }
}
