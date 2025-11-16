package br.com.todolist.repository;

import br.com.todolist.entity.Itens;
import java.util.List;

public interface ItemRepository<T extends Itens> {
    void salvar(T item);
    void excluir(T item);
    void atualizar(T item);
    List<T> buscarTodos();
}