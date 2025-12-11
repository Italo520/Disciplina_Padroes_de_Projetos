package br.com.todolist.gui;

import br.com.todolist.controller.AuthController;
import br.com.todolist.entity.Usuario;
import br.com.todolist.ui.telasusuario.TelaLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import br.com.todolist.ui.service.DialogService;

import br.com.todolist.ui.service.NavigationService;

@ExtendWith(MockitoExtension.class)
class TelaLoginTest {

    @Mock
    private AuthController authController;

    @Mock
    private DialogService dialogService;

    @Mock
    private NavigationService navigationService;

    private TelaLogin telaLogin;

    @BeforeEach
    void setUp() {
        telaLogin = new TelaLogin(authController, dialogService, navigationService);
    }

    @Test
    void inicializarTela_deveCriarComponentesCorretos() {
        assertThat(telaLogin.getCampoEmail()).isNotNull();
        assertThat(telaLogin.getCampoSenha()).isNotNull();
        assertThat(telaLogin.getBotaoEntrar()).isNotNull();
        assertThat(telaLogin.getBotaoCriarConta()).isNotNull();
    }

    @Test
    void validarLogin_emailValidoSenhaValida_deveChamarController() {
        // Arrange
        when(authController.login(anyString(), anyString())).thenReturn(new Usuario("User", "email", "pass"));
        telaLogin.getCampoEmail().setText("user@teste.com");
        telaLogin.getCampoSenha().setText("123456");

        // Act
        telaLogin.getBotaoEntrar().doClick();

        // Assert
        verify(authController).login("user@teste.com", "123456");
        verify(navigationService).navigateToMain(telaLogin);
    }

    @Test
    void validarLogin_emailInvalido_deveMostrarErro() {
        // Arrange
        when(authController.login(anyString(), anyString())).thenReturn(null);
        telaLogin.getCampoEmail().setText("email_invalido");
        telaLogin.getCampoSenha().setText("123456");

        // Act
        telaLogin.getBotaoEntrar().doClick();

        // Assert
        verify(authController).login("email_invalido", "123456");
        verify(dialogService).showError(eq(telaLogin), eq("Email ou senha incorretos."));
    }

    @Test
    void botaoCadastrar_deveAbrirTelaCadastro() {
        // Act
        // Since opening the window is hard-coded, we just check if the button exists
        // and is enabled.
        // We cannot verify the window opening without further refactoring (Factory
        // pattern).
        assertThat(telaLogin.getBotaoCriarConta().isEnabled()).isTrue();
    }
}
