package br.com.todolist.service;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Itens;
import br.com.todolist.repository.ItemRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class EventServiceImpl implements EventService {

    private final ItemRepository<Itens> itemRepository;
    private final String emailUsuario;

    public EventServiceImpl(ItemRepository<Itens> itemRepository, String emailUsuario) {
        this.itemRepository = itemRepository;
        this.emailUsuario = emailUsuario;
    }

    @Override
    public boolean cadastrarEvento(Evento novoEvento) {
        if (!novoEvento.getCriado_por().equals(emailUsuario)) {
            return false;
        }

        boolean dataDisponivel = listarTodosEventos().stream()
                .noneMatch(evento -> evento.getDeadline().isEqual(novoEvento.getDeadline()));

        if (dataDisponivel) {
            itemRepository.salvar(novoEvento);
            return true;
        }
        return false;
    }

    @Override
    public void excluirEvento(Evento evento) {
        if (evento.getCriado_por().equals(emailUsuario)) {
            itemRepository.excluir(evento);
        }
    }

    @Override
    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        if (eventoOriginal.getCriado_por().equals(emailUsuario)) {
            eventoOriginal.setTitulo(novoTitulo);
            eventoOriginal.setDescricao(novaDescricao);
            eventoOriginal.setDeadLine(novoDeadline);
            itemRepository.atualizar(eventoOriginal);
        }
    }

    @Override
    public List<Evento> listarTodosEventos() {
        return itemRepository.buscarTodos().stream()
                .filter(item -> item instanceof Evento && emailUsuario.equals(item.getCriado_por()))
                .map(item -> (Evento) item)
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
}