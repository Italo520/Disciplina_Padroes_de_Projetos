package br.com.todolist.unit.controller;

import br.com.todolist.controller.TaskController;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.ITaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskController - Unit Tests")
class TaskControllerTest {

    @Mock
    private ITaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    @DisplayName("Deve cadastrar tarefa delegando para o serviço")
    void shouldRegisterTask() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);

        // Act
        taskController.cadastrarTarefa(tarefa);

        // Assert
        verify(taskService).cadastrarTarefa(tarefa);
    }

    @Test
    @DisplayName("Deve listar todas as tarefas delegando para o serviço")
    void shouldListAllTasks() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);
        when(taskService.listarTodasTarefas()).thenReturn(List.of(tarefa));

        // Act
        List<Tarefa> result = taskController.listarTodasTarefas();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tarefa);
        verify(taskService).listarTodasTarefas();
    }

    @Test
    @DisplayName("Deve excluir tarefa delegando para o serviço")
    void shouldDeleteTask() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);

        // Act
        taskController.excluirTarefa(tarefa);

        // Assert
        verify(taskService).excluirTarefa(tarefa);
    }

    @Test
    @DisplayName("Deve editar tarefa delegando para o serviço")
    void shouldEditTask() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);
        String newTitle = "New Title";
        String newDesc = "New Desc";
        LocalDate newDate = LocalDate.now().plusDays(1);
        int newPriority = 2;

        // Act
        taskController.editarTarefa(tarefa, newTitle, newDesc, newDate, newPriority);

        // Assert
        verify(taskService).editarTarefa(tarefa, newTitle, newDesc, newDate, newPriority);
    }

    @Test
    @DisplayName("Deve atualizar tarefa delegando para o serviço")
    void shouldUpdateTask() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);

        // Act
        taskController.atualizarTarefa(tarefa);

        // Assert
        verify(taskService).atualizarTarefa(tarefa);
    }

    @Test
    @DisplayName("Deve listar tarefas por dia delegando para o serviço")
    void shouldListTasksByDay() {
        // Arrange
        LocalDate dia = LocalDate.now();
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", dia, 1);
        when(taskService.listarTarefasPorDia(dia)).thenReturn(List.of(tarefa));

        // Act
        List<Tarefa> result = taskController.listarTarefasPorDia(dia);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tarefa);
        verify(taskService).listarTarefasPorDia(dia);
    }

    @Test
    @DisplayName("Deve listar tarefas críticas delegando para o serviço")
    void shouldListCriticalTasks() {
        // Arrange
        Tarefa tarefa = new Tarefa("Critical", "Desc", "user@test.com", LocalDate.now(), 1);
        when(taskService.listarTarefasCriticas()).thenReturn(List.of(tarefa));

        // Act
        List<Tarefa> result = taskController.listarTarefasCriticas();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tarefa);
        verify(taskService).listarTarefasCriticas();
    }
}
