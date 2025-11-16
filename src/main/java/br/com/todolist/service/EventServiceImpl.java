package br.com.todolist.service;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Itens;
import br.com.todolist.observer.Observer;
import br.com.todolist.repository.ItemRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do serviço de eventos.
 * Contém a lógica de negócio para gerenciar eventos e notificar observadores sobre mudanças.
 */
public class EventServiceImpl implements EventService {

    private final ItemRepository<Itens> itemRepository;
    private final String emailUsuario;
    private final List<Observer<Evento>> observers = new ArrayList<>();

    public EventServiceImpl(ItemRepository<Itens> itemRepository, String emailUsuario) {
        this.itemRepository = itemRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public boolean cadastrarEvento(Evento evento) {
        boolean dataDisponivel = true;
        for (Evento e : listarTodosEventos()) {
            if (e.getDeadline().isEqual(evento.getDeadline())) {
                dataDisponivel = false;
                break;
            }
        }

        if (dataDisponivel) {
            itemRepository.salvar(evento);
            notifyObservers(evento);
            return true;
        }
        return false;
    }

    @Override
    public void excluirEvento(Evento evento) {
        if (evento.getCriado_por().equals(emailUsuario)) {
            itemRepository.excluir(evento);
            notifyObservers(evento);
        }
    }

    @Override
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        if (eventoOriginal.getCriado_por().equals(emailUsuario)) {
            eventoOriginal.setTitulo(novoTitulo);
            eventoOriginal.setDescricao(novaDescricao);
            eventoOriginal.setDeadLine(novoDeadline);
            itemRepository.atualizar(eventoOriginal);
            notifyObservers(eventoOriginal);
        }
    }

    @Override
    public List<Evento> listarTodosEventos() {
        List<Evento> eventos = new ArrayList<>();
        for (Itens item : itemRepository.buscarTodos()) {
            if (item instanceof Evento && emailUsuario.equals(item.getCriado_por())) {
                eventos.add((Evento) item);
            }
        }
        return eventos;
    }

    @Override
    public List<Evento> listarEventosPorDia(LocalDate dia) {
        List<Evento> eventosDoDia = new ArrayList<>();
        for (Evento evento : listarTodosEventos()) {
            if (evento.getDeadline().isEqual(dia)) {
                eventosDoDia.add(evento);
            }
        }
        return eventosDoDia;
    }

    @Override
    public List<Evento> listarEventosPorMes(YearMonth mes) {
        List<Evento> eventosDoMes = new ArrayList<>();
        for (Evento evento : listarTodosEventos()) {
            if (YearMonth.from(evento.getDeadline()).equals(mes)) {
                eventosDoMes.add(evento);
            }
        }
        return eventosDoMes;
    }

    @Override
    public void addObserver(Observer<Evento> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Evento> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Evento evento) {
        for (Observer<Evento> observer : observers) {
            observer.update(evento);
        }
    }
}
