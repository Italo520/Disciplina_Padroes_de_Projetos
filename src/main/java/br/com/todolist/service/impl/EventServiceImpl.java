package br.com.todolist.service.impl;

import br.com.todolist.entity.Evento;
import br.com.todolist.repository.IEventoRepository;
import br.com.todolist.service.util.IObserver;
import br.com.todolist.service.IEventService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de eventos.
 * Contém a lógica de negócio para gerenciar eventos e notificar observadores sobre mudanças.
 */
public class EventServiceImpl implements IEventService {

    private final IEventoRepository eventoRepository;
    private final String emailUsuario;
    private final List<IObserver<Evento>> observers = new ArrayList<>();

    public EventServiceImpl(IEventoRepository eventoRepository, String emailUsuario) {
        this.eventoRepository = eventoRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public boolean cadastrarEvento(Evento evento) {
        boolean dataDisponivel = listarTodosEventos().stream()
                .noneMatch(e -> e.getDeadline().isEqual(evento.getDeadline()));

        if (dataDisponivel) {
            eventoRepository.salvar(evento);
            notifyObservers(evento);
            return true;
        }
        return false;
    }

    @Override
    public void excluirEvento(Evento evento) {
        if (evento.getCriado_por().equals(emailUsuario)) {
            eventoRepository.excluir(evento);
            notifyObservers(evento);
        }
    }

    @Override
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        if (eventoOriginal.getCriado_por().equals(emailUsuario)) {
            eventoOriginal.setTitulo(novoTitulo);
            eventoOriginal.setDescricao(novaDescricao);
            eventoOriginal.setDeadLine(novoDeadline);
            eventoRepository.atualizar(eventoOriginal);
            notifyObservers(eventoOriginal);
        }
    }

    @Override
    public List<Evento> listarTodosEventos() {
        return eventoRepository.buscarTodos().stream()
                .filter(evento -> evento.getCriado_por().equals(emailUsuario))
                .collect(Collectors.toList());
    }

    @Override
    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return listarTodosEventos().stream()
                .filter(evento -> evento.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    @Override
    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return listarTodosEventos().stream()
                .filter(evento -> YearMonth.from(evento.getDeadline()).equals(mes))
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(IObserver<Evento> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver<Evento> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Evento evento) {
        for (IObserver<Evento> observer : observers) {
            observer.update(evento);
        }
    }
}
