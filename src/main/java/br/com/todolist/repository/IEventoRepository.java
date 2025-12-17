package br.com.todolist.repository;

import br.com.todolist.entity.Evento;
import java.util.List;

public interface IEventoRepository {

    void salvar(Evento entity);

    void excluir(Evento entity);

    void atualizar(Evento entity);

    Evento buscarPorId(Long id);

    List<Evento> buscarTodos();

}