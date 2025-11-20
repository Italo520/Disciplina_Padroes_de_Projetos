package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
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
     * @throws BusinessException se houver conflito ou erro de persistência.
     */
    public void cadastrarEvento(Evento evento) throws BusinessException {
        eventService.cadastrarEvento(evento);
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
     * @throws BusinessException se ocorrer erro ao excluir.
     */
    public void excluirEvento(Evento evento) throws BusinessException {
        eventService.excluirEvento(evento);
    }

    /**
     * Edita os dados de um evento existente.
     *
     * @param eventoOriginal O evento original que será modificado.
     * @param novoTitulo     O novo título do evento.
     * @param novaDescricao  A nova descrição do evento.
     * @param novoDeadline   A nova data limite do evento.
     * @throws BusinessException se ocorrer erro ao editar.
     */
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) throws BusinessException {
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
