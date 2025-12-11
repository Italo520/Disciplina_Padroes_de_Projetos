package br.com.todolist.unit.entity;

import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.util.IProgressCalculationStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Tarefa - Unit Tests")
class TarefaTest {

    @Test
    @DisplayName("Deve criar tarefa com construtor completo")
    void shouldCreateTaskWithFullConstructor() {
        LocalDate deadline = LocalDate.now();
        Tarefa tarefa = new Tarefa("Title", "Desc", "user@test.com", deadline, 1);

        assertThat(tarefa.getTitulo()).isEqualTo("Title");
        assertThat(tarefa.getDescricao()).isEqualTo("Desc");
        assertThat(tarefa.getCriado_por()).isEqualTo("user@test.com");
        assertThat(tarefa.getDeadline()).isEqualTo(deadline);
        assertThat(tarefa.getPrioridade()).isEqualTo(1);
        assertThat(tarefa.getDataConclusao()).isNull();
        assertThat(tarefa.getSubtarefas()).isEmpty();
    }

    @Test
    @DisplayName("Deve gerenciar subtarefas corretamente")
    void shouldManageSubtasks() {
        Tarefa tarefa = new Tarefa();
        Subtarefa sub1 = new Subtarefa("Sub 1");
        Subtarefa sub2 = new Subtarefa("Sub 2");

        tarefa.adicionarSubtarefa(sub1);
        tarefa.adicionarSubtarefa(sub2);

        assertThat(tarefa.getSubtarefas()).hasSize(2);
        assertThat(tarefa.getSubtarefas()).contains(sub1, sub2);
        assertThat(sub1.getTarefa()).isEqualTo(tarefa);

        tarefa.removerSubtarefa(sub1);

        assertThat(tarefa.getSubtarefas()).hasSize(1);
        assertThat(tarefa.getSubtarefas()).contains(sub2);
        assertThat(sub1.getTarefa()).isNull();
    }

    @Test
    @DisplayName("Deve delegar cálculo de progresso para a estratégia")
    void shouldDelegateProgressCalculation() {
        Tarefa tarefa = new Tarefa();
        IProgressCalculationStrategy strategy = mock(IProgressCalculationStrategy.class);
        tarefa.setProgressCalculationStrategy(strategy);
        when(strategy.calcularProgresso(tarefa)).thenReturn(50.0);

        double progress = tarefa.obterPercentual();

        assertThat(progress).isEqualTo(50.0);
        verify(strategy).calcularProgresso(tarefa);
    }

    @Test
    @DisplayName("Deve retornar título no toString")
    void shouldReturnTitleInToString() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("My Task");

        assertThat(tarefa.toString()).isEqualTo("My Task");
    }
}
