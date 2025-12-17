package br.com.todolist.service.impl;

import br.com.todolist.entity.Evento;
import br.com.todolist.exception.BusinessException;
import br.com.todolist.exception.DadosInvalidosException;
import br.com.todolist.exception.DatabaseException;
import br.com.todolist.log.AuditAction;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.service.IEventService;
import br.com.todolist.service.event.CalendarEvent;
import br.com.todolist.service.util.IEventObserver;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl implements IEventService {

    private final IEventoRepository eventoRepository;
    private final String emailUsuario;
    private final List<IEventObserver> observers = new ArrayList<>();

    public EventServiceImpl(IEventoRepository eventoRepository, String emailUsuario) {
        this.eventoRepository = eventoRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public void cadastrarEvento(Evento evento) throws BusinessException {

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

    @Override
    public void excluirEvento(Evento evento) throws BusinessException {
        if (evento.getCriadoPor() != null && evento.getCriadoPor().equals(emailUsuario)) {
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

    @Override
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline)
            throws BusinessException {
        if (eventoOriginal.getCriadoPor() != null && eventoOriginal.getCriadoPor().equals(emailUsuario)) {
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

    @Override
    public List<Evento> listarTodosEventos() {
        return eventoRepository.buscarTodos().stream()
                .filter(evento -> evento.getCriadoPor() != null && evento.getCriadoPor().equals(emailUsuario))
                .toList();
    }

    @Override
    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return listarTodosEventos().stream()
                .filter(evento -> evento.getDeadline().isEqual(dia))
                .toList();
    }

    @Override
    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return listarTodosEventos().stream()
                .filter(evento -> YearMonth.from(evento.getDeadline()).equals(mes))
                .toList();
    }

    @Override
    public void addObserver(IEventObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IEventObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(CalendarEvent event) {
        for (IEventObserver observer : observers) {
            observer.update(event);
        }
    }
}