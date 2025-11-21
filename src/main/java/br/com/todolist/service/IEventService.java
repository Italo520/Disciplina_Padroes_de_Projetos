package br.com.todolist.service;

import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.service.event.CalendarEvent;
import br.com.todolist.service.util.ISubject;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Interface para o serviço de eventos.
 * Define os métodos que devem ser implementados pelas classes de serviço de eventos.
 */
public interface IEventService extends ISubject<CalendarEvent> {

    /**
     * Cadastra um novo evento.
     *
     * @param evento o evento a ser cadastrado.
     * @throws BusinessException se o evento for inválido ou houver conflito de datas.
     */
    void cadastrarEvento(Evento evento) throws BusinessException;

    /**
     * Exclui um evento.
     *
     * @param evento o evento a ser excluído.
     * @throws BusinessException se ocorrer erro ao excluir.
     */
    void excluirEvento(Evento evento) throws BusinessException;

    /**
     * Edita um evento.
     *
     * @param eventoOriginal o evento original.
     * @param novoTitulo o novo título do evento.
     * @param novaDescricao a nova descrição do evento.
     * @param novoDeadline o novo prazo do evento.
     * @throws BusinessException se ocorrer erro ao editar.
     */
    void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) throws BusinessException;

    /**
     * Lista todos os eventos.
     *
     * @return uma lista com todos os eventos.
     */
    List<Evento> listarTodosEventos();

    /**
     * Lista os eventos de um dia específico.
     *
     * @param dia o dia para o qual os eventos devem ser listados.
     * @return uma lista com os eventos do dia.
     */
    List<Evento> listarEventosPorDia(LocalDate dia);

    /**
     * Lista os eventos de um mês específico.
     *
     * @param mes o mês para o qual os eventos devem ser listados.
     * @return uma lista com os eventos do mês.
     */
    List<Evento> listarEventosPorMes(YearMonth mes);
}
