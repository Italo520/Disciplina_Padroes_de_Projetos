package br.com.todolist.repository;

import br.com.todolist.entity.Evento;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Interface para o repositório de eventos.
 * Estende a interface genérica IRepository e define as operações de
 * persistência para a entidade Evento.
 */
public interface IEventoRepository extends IRepository<Evento, String> {
    List<Evento> buscarPorDia(LocalDate dia);

    List<Evento> buscarPorMes(YearMonth mes);
}
