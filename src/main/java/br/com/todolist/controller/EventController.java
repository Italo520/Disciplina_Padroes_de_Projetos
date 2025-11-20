package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.service.IEventService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Controlador responsável pelo gerenciamento de eventos.
 * Intermedeia as operações entre a interface gráfica e o serviço de eventos.
 */
public class EventController {

    private final IEventService eventService;

    /**
     * Construtor da classe EventController.
     *
     * @param eventService O serviço de eventos a ser utilizado pelo controlador.
     */
    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Cadastra um novo evento.
     *
     * @param evento O objeto Evento a ser cadastrado.
     * @return true se o evento foi cadastrado com sucesso, false caso contrário.
     */
    public boolean cadastrarEvento(Evento evento) {
        return eventService.cadastrarEvento(evento);
    }

    /**
     * Lista todos os eventos cadastrados.
     *
     * @return Uma lista contendo todos os eventos.
     */
    public List<Evento> listarTodosEventos() {
        return eventService.listarTodosEventos();
    }

    /**
     * Exclui um evento existente.
     *
     * @param evento O evento a ser excluído.
     */
    public void excluirEvento(Evento evento) {
        eventService.excluirEvento(evento);
    }

    /**
     * Edita os dados de um evento existente.
     *
     * @param eventoOriginal O evento original que será modificado.
     * @param novoTitulo     O novo título do evento.
     * @param novaDescricao  A nova descrição do evento.
     * @param novoDeadline   A nova data limite do evento.
     */
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        eventService.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    /**
     * Lista os eventos agendados para um dia específico.
     *
     * @param dia A data para a qual se deseja listar os eventos.
     * @return Uma lista de eventos agendados para o dia especificado.
     */
    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return eventService.listarEventosPorDia(dia);
    }

    /**
     * Lista os eventos agendados para um mês específico.
     *
     * @param mes O mês e ano (YearMonth) para o qual se deseja listar os eventos.
     * @return Uma lista de eventos agendados para o mês especificado.
     */
    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return eventService.listarEventosPorMes(mes);
    }
}
