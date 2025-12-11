package br.com.todolist.unit.strategy;

import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.util.DefaultProgressCalculationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DefaultProgressCalculationStrategy - Unit Tests")
class DefaultProgressCalculationStrategyTest {

    private DefaultProgressCalculationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new DefaultProgressCalculationStrategy();
    }

    @Test
    @DisplayName("Deve retornar 0.0 se tarefa não tem subtarefas e não está concluída")
    void shouldReturnZero_When_NoSubtasksAndNotCompleted() {
        Tarefa tarefa = new Tarefa("Task", "Desc", "user", LocalDate.now(), 1);

        double progress = strategy.calcularProgresso(tarefa);

        assertThat(progress).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Deve retornar 100.0 se tarefa não tem subtarefas e está concluída")
    void shouldReturnHundred_When_NoSubtasksAndCompleted() {
        Tarefa tarefa = new Tarefa("Task", "Desc", "user", LocalDate.now(), 1);
        tarefa.setDataConclusao(LocalDate.now());

        double progress = strategy.calcularProgresso(tarefa);

        assertThat(progress).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Deve calcular progresso baseado nas subtarefas")
    void shouldCalculateProgressBasedOnSubtasks() {
        Tarefa tarefa = new Tarefa("Task", "Desc", "user", LocalDate.now(), 1);

        Subtarefa sub1 = new Subtarefa("Sub 1");
        sub1.setStatus(true); // Completed

        Subtarefa sub2 = new Subtarefa("Sub 2");
        sub2.setStatus(false); // Pending

        tarefa.adicionarSubtarefa(sub1);
        tarefa.adicionarSubtarefa(sub2);

        double progress = strategy.calcularProgresso(tarefa);

        assertThat(progress).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Deve retornar 100.0 se todas subtarefas estão concluídas")
    void shouldReturnHundred_When_AllSubtasksCompleted() {
        Tarefa tarefa = new Tarefa("Task", "Desc", "user", LocalDate.now(), 1);

        Subtarefa sub1 = new Subtarefa("Sub 1");
        sub1.setStatus(true);

        Subtarefa sub2 = new Subtarefa("Sub 2");
        sub2.setStatus(true);

        tarefa.adicionarSubtarefa(sub1);
        tarefa.adicionarSubtarefa(sub2);

        double progress = strategy.calcularProgresso(tarefa);

        assertThat(progress).isEqualTo(100.0);
    }
}
