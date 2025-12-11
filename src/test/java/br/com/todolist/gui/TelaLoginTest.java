package br.com.todolist.gui;

import br.com.todolist.ui.telasusuario.TelaLogin;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TelaLoginTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;

    @Override
    protected void onSetUp() {
        TelaLogin frame = GuiActionRunner.execute(TelaLogin::new);
        window = new FrameFixture(robot(), frame);
        window.show(); // shows the frame to test
    }

    @Test
    public void shouldHaveCorrectComponents() {
        window.textBox("campoEmail").requireVisible();
        window.textBox("campoSenha").requireVisible();
        window.button("botaoEntrar").requireVisible().requireText("Entrar");
        window.button("botaoCriarConta").requireVisible().requireText("Criar Conta");
    }

    @Test
    public void shouldShowErrorOnEmptyLogin() {
        window.button("botaoEntrar").click();
        window.optionPane().requireErrorMessage().requireMessage("Email e senha são obrigatórios.");
    }
}
