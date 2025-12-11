package br.com.todolist.unit.controller;

import br.com.todolist.controller.AppController;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.IEventService;
import br.com.todolist.service.IReportService;
import br.com.todolist.service.ITaskService;
import br.com.todolist.service.IUserService;
import br.com.todolist.service.util.IItemFactory;
import br.com.todolist.util.Mensageiro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("AppController - Unit Tests")
class AppControllerTest {

    @Mock
    private IUserService userService;
    @Mock
    private Mensageiro mensageiro;
    @Mock
    private IItemFactory itemFactory;
    @Mock
    private ITaskService taskService;
    @Mock
    private IEventService eventService;
    @Mock
    private IReportService reportService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        AppController.resetInstance();
        AppController.init(userService, mensageiro, itemFactory);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
        AppController.resetInstance();
    }

    @Test
    @DisplayName("Deve inicializar corretamente")
    void shouldInitializeCorrectly() {
        AppController controller = AppController.getInstance();
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void shouldLoginSuccessfully() {
        // Arrange
        Usuario user = new Usuario("Test", "test@test.com", "pass");
        when(userService.autenticarUsuario("test@test.com", "pass")).thenReturn(user);

        // Act
        boolean result = AppController.getInstance().login("test@test.com", "pass");

        // Assert
        assertThat(result).isTrue();
        assertThat(AppController.getInstance().getUsuarioLogado()).isEqualTo(user);
    }

    @Test
    @DisplayName("Deve falhar no login com credenciais inválidas")
    void shouldFailLogin() {
        // Arrange
        when(userService.autenticarUsuario(anyString(), anyString())).thenReturn(null);

        // Act
        boolean result = AppController.getInstance().login("wrong", "pass");

        // Assert
        assertThat(result).isFalse();
        assertThat(AppController.getInstance().getUsuarioLogado()).isNull();
    }

    @Test
    @DisplayName("Deve delegar cadastro de tarefa para o serviço")
    void shouldDelegateTaskCreation() {
        // Arrange
        Usuario user = new Usuario("Test", "test@test.com", "pass");
        AppController controller = AppController.getInstance();
        controller.setUsuarioLogado(user);
        controller.setTaskService(taskService);

        Tarefa tarefa = new Tarefa("Title", "Desc", "test@test.com", LocalDate.now(), 1);
        when(itemFactory.criarTarefa(anyString(), anyString(), anyString(), any(), eq(1))).thenReturn(tarefa);

        // Act
        controller.cadastrarTarefa("Title", "Desc", LocalDate.now(), 1);

        // Assert
        verify(taskService).cadastrarTarefa(tarefa);
    }

    @Test
    @DisplayName("Deve delegar listagem de tarefas")
    void shouldDelegateTaskList() {
        // Arrange
        AppController controller = AppController.getInstance();
        controller.setTaskService(taskService);
        when(taskService.listarTodasTarefas()).thenReturn(Collections.emptyList());

        // Act
        List<Tarefa> tasks = controller.listarTodasTarefas();

        // Assert
        assertThat(tasks).isEmpty();
        verify(taskService).listarTodasTarefas();
    }

    @Test
    @DisplayName("Deve delegar cadastro de evento")
    void shouldDelegateEventCreation() {
        // Arrange
        Usuario user = new Usuario("Test", "test@test.com", "pass");
        AppController controller = AppController.getInstance();
        controller.setUsuarioLogado(user);
        controller.setEventService(eventService);

        Evento evento = new Evento("Title", "Desc", "test@test.com", LocalDate.now());
        when(itemFactory.criarEvento(anyString(), anyString(), anyString(), any())).thenReturn(evento);
        when(eventService.cadastrarEvento(evento)).thenReturn(true);

        // Act
        boolean result = controller.cadastrarEvento("Title", "Desc", LocalDate.now());

        // Assert
        assertThat(result).isTrue();
        verify(eventService).cadastrarEvento(evento);
    }

    @Test
    @DisplayName("Deve delegar envio de relatório por email")
    void shouldDelegateEmailReport() {
        // Arrange
        Usuario user = new Usuario("Test", "test@test.com", "pass");
        AppController controller = AppController.getInstance();
        controller.setUsuarioLogado(user);
        controller.setReportService(reportService);
        LocalDate today = LocalDate.now();

        when(reportService.enviarRelatorioTarefasDoDiaPorEmail(today, user)).thenReturn(true);

        // Act
        boolean result = controller.enviarRelatorioTarefasDoDiaPorEmail(today);

        // Assert
        assertThat(result).isTrue();
        verify(reportService).enviarRelatorioTarefasDoDiaPorEmail(today, user);
    }
}
