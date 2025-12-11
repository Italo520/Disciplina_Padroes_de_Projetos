package br.com.todolist.gui;

import br.com.todolist.controller.AuthController;
import br.com.todolist.ui.service.DialogService;
import br.com.todolist.ui.telasusuario.TelaCadastro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelaCadastroTest {

    @Mock
    private AuthController authController;

    @Mock
    private DialogService dialogService;

    private Frame owner;

    private TelaCadastro telaCadastro;

    @BeforeEach
    void setUp() {
        owner = new JFrame();
        telaCadastro = new TelaCadastro(owner, authController, dialogService);
    }

    @Test
    void inicializarTela_deveCriarComponentesCorretos() {
        assertThat(telaCadastro.getCampoNome()).isNotNull();
        assertThat(telaCadastro.getCampoEmail()).isNotNull();
        assertThat(telaCadastro.getCampoSenha()).isNotNull();
        assertThat(telaCadastro.getBotaoCadastrar()).isNotNull();
        assertThat(telaCadastro.getBotaoCancelar()).isNotNull();
    }

    @Test
    void realizarCadastro_dadosValidos_deveChamarControllerEExibirSucesso() {
        // Arrange
        when(authController.cadastrarUsuario(anyString(), anyString(), anyString())).thenReturn(true);
        telaCadastro.getCampoNome().setText("User");
        telaCadastro.getCampoEmail().setText("user@teste.com");
        telaCadastro.getCampoSenha().setText("123456");

        // Act
        telaCadastro.getBotaoCadastrar().doClick();

        // Assert
        verify(authController).cadastrarUsuario("User", "user@teste.com", "123456");
        verify(dialogService).showInformation(eq(telaCadastro), eq("Usuário cadastrado com sucesso!"));
        assertThat(telaCadastro.getEmailCadastrado()).isEqualTo("user@teste.com");
    }

    @Test
    void realizarCadastro_emailExistente_deveExibirErro() {
        // Arrange
        when(authController.cadastrarUsuario(anyString(), anyString(), anyString())).thenReturn(false);
        telaCadastro.getCampoNome().setText("User");
        telaCadastro.getCampoEmail().setText("user@teste.com");
        telaCadastro.getCampoSenha().setText("123456");

        // Act
        telaCadastro.getBotaoCadastrar().doClick();

        // Assert
        verify(authController).cadastrarUsuario("User", "user@teste.com", "123456");
        verify(dialogService).showError(eq(telaCadastro), eq("Este email já está em uso. Tente outro."));
        assertThat(telaCadastro.getEmailCadastrado()).isNull();
    }

    @Test
    void realizarCadastro_camposVazios_deveExibirErroValidacao() {
        // Arrange
        telaCadastro.getCampoNome().setText("");
        telaCadastro.getCampoEmail().setText("");
        telaCadastro.getCampoSenha().setText("");

        // Act
        telaCadastro.getBotaoCadastrar().doClick();

        // Assert
        verify(authController, never()).cadastrarUsuario(anyString(), anyString(), anyString());
        verify(dialogService).showError(eq(telaCadastro), eq("Todos os campos são obrigatórios."));
    }
}
