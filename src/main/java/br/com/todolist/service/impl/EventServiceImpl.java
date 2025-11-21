package br.com.todolist.service.impl;

import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.DadosInvalidosException;
import br.com.todolist.exception.DatabaseException;
import br.com.todolist.log.AuditAction;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.service.IEventService;
import br.com.todolist.service.event.CalendarEvent;
import br.com.todolist.service.util.IObserver;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de eventos.
 * Contém a lógica de negócio para gerenciar eventos e notificar observadores sobre mudanças.
 * Filtra os eventos pelo e-mail do usuário logado.
 */
public class EventServiceImpl implements IEventService {

    private final IEventoRepository eventoRepository;
    private final String emailUsuario;
    private final List<IObserver<CalendarEvent>> observers = new ArrayList<>();

    /**
     * Construtor da classe EventServiceImpl.
     *
     * @param eventoRepository O repositório de eventos a ser utilizado.
     * @param emailUsuario     O e-mail do usuário cujos eventos serão gerenciados.
     */
    public EventServiceImpl(IEventoRepository eventoRepository, String emailUsuario) {
        this.eventoRepository = eventoRepository;
        this.emailUsuario = emailUsuario;
    }

    /**
     * Cadastra um novo evento se a data estiver disponível.
     * Verifica se já existe um evento na mesma data para o usuário.
     *
     * @param evento O evento a ser cadastrado.
     * @throws BusinessException se a data já estiver ocupada ou houver erro de persistência.
     */
    @Override
    public void cadastrarEvento(Evento evento) throws BusinessException {
        // Ineficiente buscar todos para verificar, mas mantendo lógica original
        boolean dataDisponivel = listarTodosEventos().stream()
                .noneMatch(e -> e.getDeadline().isEqual(evento.getDeadline()));

        if (!dataDisponivel) {
            throw new BusinessException("Já existe um evento agendado para esta data.");
        }

        try {
            eventoRepository.salvar(evento);
            notifyObservers(new CalendarEvent(AuditAction.CREATE, evento));
        } catch (DatabaseException e) {
            throw new BusinessException("Erro ao salvar evento.", e);
        }
    }

    /**
     * Exclui um evento, garantindo que pertença ao usuário logado.
     *
     * @param evento O evento a ser excluído.
     * @throws BusinessException se ocorrer erro ao excluir.
     */
    @Override
    public void excluirEvento(Evento evento) throws BusinessException {
        if (evento.getCriado_por().equals(emailUsuario)) {
            try {
                eventoRepository.excluir(evento);
                notifyObservers(new CalendarEvent(AuditAction.DELETE, evento));
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao excluir evento.", e);
            }
        } else {
             throw new DadosInvalidosException("Evento não pertence ao usuário.");
        }
    }

    /**
     * Edita os detalhes de um evento existente, se pertencer ao usuário.
     *
     * @param eventoOriginal O evento original.
     * @param novoTitulo     O novo título.
     * @param novaDescricao  A nova descrição.
     * @param novoDeadline   A nova data.
     * @throws BusinessException se ocorrer erro ao editar.
     */
    @Override
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) throws BusinessException {
        if (eventoOriginal.getCriado_por().equals(emailUsuario)) {
            Evento oldEvento = eventoOriginal.copiar();

            eventoOriginal.setTitulo(novoTitulo);
            eventoOriginal.setDescricao(novaDescricao);
            eventoOriginal.setDeadLine(novoDeadline);
            try {
                eventoRepository.atualizar(eventoOriginal);
                notifyObservers(new CalendarEvent(AuditAction.UPDATE, eventoOriginal, oldEvento));
            } catch (DatabaseException e) {
                throw new BusinessException("Erro ao atualizar evento.", e);
            }
        } else {
             throw new DadosInvalidosException("Evento não pertence ao usuário.");
        }
    }

    /**
     * Lista todos os eventos do usuário logado.
     *
     * @return Uma lista de eventos filtrada pelo e-mail do usuário.
     */
    @Override
    public List<Evento> listarTodosEventos() {
        return eventoRepository.buscarTodos().stream()
                .filter(evento -> evento.getCriado_por().equals(emailUsuario))
                .collect(Collectors.toList());
    }

    /**
     * Lista os eventos do usuário para um dia específico.
     *
     * @param dia O dia a ser consultado.
     * @return Uma lista de eventos do dia.
     */
    @Override
    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return listarTodosEventos().stream()
                .filter(evento -> evento.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    /**
     * Lista os eventos do usuário para um mês específico.
     *
     * @param mes O mês a ser consultado.
     * @return Uma lista de eventos do mês.
     */
    @Override
    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return listarTodosEventos().stream()
                .filter(evento -> YearMonth.from(evento.getDeadline()).equals(mes))
                .collect(Collectors.toList());
    }

    /**
     * Adiciona um observador para receber notificações sobre mudanças nos eventos.
     *
     * @param observer O observador a ser adicionado.
     */
    @Override
    public void addObserver(IObserver<CalendarEvent> observer) {
        observers.add(observer);
    }

    /**
     * Remove um observador.
     *
     * @param observer O observador a ser removido.
     */
    @Override
    public void removeObserver(IObserver<CalendarEvent> observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores registrados sobre uma mudança em um evento.
     *
     * @param event O evento que sofreu alteração.
     */
    @Override
    public void notifyObservers(CalendarEvent event) {
        for (IObserver<CalendarEvent> observer : observers) {
            observer.update(event);
        }
    }
}
