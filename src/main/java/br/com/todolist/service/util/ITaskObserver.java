package br.com.todolist.service.util;

import br.com.todolist.service.event.TaskEvent;

public interface ITaskObserver {

    void update(TaskEvent event);
}