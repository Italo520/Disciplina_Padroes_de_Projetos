package br.com.todolist.unit.service;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskServiceImpl - Unit Tests")
public class TaskServiceImplTest {

    @Mock
    private ITarefaRepository tarefaRepository;

    private TaskServiceImpl taskService;

    @BeforeEach
    public void setUp() {
        System.out.println("DEBUG: setUp called");
        taskService = new TaskServiceImpl(tarefaRepository, "user@test.com");
    }

    @DisplayName("Deve cadastrar tarefa com sucesso")
    void shouldRegisterTaskSuccessfully() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);

        // Act
        taskService.cadastrarTarefa(tarefa);

        // Assert
        verify(tarefaRepository).salvar(tarefa);
    }

    @Test
    @DisplayName("Deve listar todas as tarefas")
    void shouldListAllTasks() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);
        when(tarefaRepository.buscarTodos()).thenReturn(List.of(tarefa));

        // Act
        List<Tarefa> result = taskService.listarTodasTarefas();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tarefa);
    }

    @Test
    @DisplayName("Deve excluir tarefa")
    void shouldDeleteTask() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);

        // Act
        taskService.excluirTarefa(tarefa);

        // Assert
        verify(tarefaRepository).excluir(tarefa);
    }

    @Test
    @DisplayName("Deve editar tarefa")
    void shouldEditTask() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);
        String newTitle = "New Title";
        String newDesc = "New Desc";
        LocalDate newDate = LocalDate.now().plusDays(1);
        int newPriority = 2;

        // Act
        taskService.editarTarefa(tarefa, newTitle, newDesc, newDate, newPriority);

        // Assert
        assertThat(tarefa.getTitulo()).isEqualTo(newTitle);
        assertThat(tarefa.getDescricao()).isEqualTo(newDesc);
        assertThat(tarefa.getDeadline()).isEqualTo(newDate);
        assertThat(tarefa.getPrioridade()).isEqualTo(newPriority);
        verify(tarefaRepository).atualizar(tarefa);
    }

    @Test
    @DisplayName("Deve atualizar tarefa")
    void shouldUpdateTask() {
        // Arrange
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", LocalDate.now(), 1);

        // Act
        taskService.atualizarTarefa(tarefa);

        // Assert
        verify(tarefaRepository).atualizar(tarefa);
    }

    @Test
    @DisplayName("Deve listar tarefas por dia")
    void shouldListTasksByDay() {
        // Arrange
        LocalDate dia = LocalDate.now();
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", dia, 1);
        when(tarefaRepository.buscarTodos()).thenReturn(List.of(tarefa));

        // Act
        List<Tarefa> result = taskService.listarTarefasPorDia(dia);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tarefa);
    }

    @Test
    @DisplayName("Deve listar tarefas críticas")
    void shouldListCriticalTasks() {
        // Arrange
        // Critical task: deadline is today (0 days diff) - priority 1 = -1 < 0
        Tarefa tarefa = new Tarefa("Critical", "Desc", "user@test.com", LocalDate.now(), 1);
        when(tarefaRepository.buscarTodos()).thenReturn(List.of(tarefa));

        // Act
        List<Tarefa> result = taskService.listarTarefasCriticas();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tarefa);
    }
}
