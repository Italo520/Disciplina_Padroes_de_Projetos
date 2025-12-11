# Plano de Implementação - Fase 3 & 4: Utilitários e Controladores

## Objetivo
Cobrir componentes utilitários essenciais e a camada de controle principal, garantindo que a lógica auxiliar e o fluxo de aplicação funcionem corretamente.

## Mudanças Propostas

### Fase 3: Utilitários e Helpers

#### [NEW] [MensageiroTest.java](file:///home/italo/Área de Trabalho/Refactoring_to_do_list/To_Do_List_PP/src/test/java/br/com/todolist/unit/utils/MensageiroTest.java)
- **Objetivo**: Validar envio de e-mails usando servidor SMTP mockado (GreenMail).
- **Cenários**:
    1.  Enviar e-mail simples com sucesso.
    2.  Enviar e-mail com anexo com sucesso.
    3.  Tratar erro de conexão SMTP.

#### [NEW] [DefaultProgressCalculationStrategyTest.java](file:///home/italo/Área de Trabalho/Refactoring_to_do_list/To_Do_List_PP/src/test/java/br/com/todolist/unit/strategy/DefaultProgressCalculationStrategyTest.java)
- **Objetivo**: Garantir precisão no cálculo de progresso.
- **Cenários**:
    1.  Cálculo com 0 tarefas.
    2.  Cálculo com tarefas pendentes e concluídas.
    3.  Cálculo com 100% concluído.

### Fase 4: Controladores

#### [NEW] [AppControllerTest.java](file:///home/italo/Área de Trabalho/Refactoring_to_do_list/To_Do_List_PP/src/test/java/br/com/todolist/unit/controller/AppControllerTest.java)
- **Objetivo**: Testar a integração entre a UI e os serviços através do controlador principal.
- **Cenários**:
    1.  Inicialização do controller.
    2.  Navegação entre painéis (Login -> Main).
    3.  Logout.

### Fase 5: Interface Gráfica (GUI)

#### [NEW] [TelaLoginTest.java](file:///home/italo/Área de Trabalho/Refactoring_to_do_list/To_Do_List_PP/src/test/java/br/com/todolist/gui/TelaLoginTest.java)
- **Objetivo**: Validar a interface de login usando AssertJ Swing.
- **Cenários**:
    1.  Verificar se os componentes (campos de texto, botões) estão visíveis.
    2.  Tentar login com credenciais inválidas e verificar mensagem de erro (se houver).
    3.  Tentar login com credenciais válidas.

### Fase 7: UI Enterprise (Refatoração e Testes)

#### [NEW] [TelaLoginTest.java](file:///home/italo/Área de Trabalho/Refactoring_to_do_list/To_Do_List_PP/src/test/java/br/com/todolist/gui/TelaLoginTest.java)
- **Objetivo**: Validar lógica de login e navegação isolada da UI Swing.
- **Estratégia**: Mockito + Injeção de Dependência (`DialogService`, `NavigationService`).

#### [NEW] [TelaCadastroTest.java](file:///home/italo/Área de Trabalho/Refactoring_to_do_list/To_Do_List_PP/src/test/java/br/com/todolist/gui/TelaCadastroTest.java)
- **Objetivo**: Validar lógica de cadastro e tratamento de erros.
- **Estratégia**: Mockito + Injeção de Dependência.

#### [NEW] [TelaPrincipalTest.java](file:///home/italo/Área de Trabalho/Refactoring_to_do_list/To_Do_List_PP/src/test/java/br/com/todolist/gui/TelaPrincipalTest.java)
- **Objetivo**: Demonstrar padrões de teste e documentar limitações arquiteturais.
- **Estratégia**: Testes de contrato pragmáticos adequados para nível acadêmico.
- **Nota**: Testes completos requerem refatoração de `BarraFerramentas`, `PainelTarefas` e `PainelEventos`.

## Lições Aprendidas

### Testabilidade de UI Swing
- **Desafio**: Código Swing legado com forte acoplamento a Singletons dificulta testes.
- **Solução Aplicada**: Injeção de Dependência via construtores adicionais.
- **Limitação**: Componentes complexos (`BarraFerramentas`) com dependências profundas exigem refatoração extensiva.
- **Recomendação**: Para projetos novos, usar DI desde o início (Spring, Guice) para facilitar testes.

## Plano de Verificação
Executar testes das fases 3 e 4:
```bash
mvn test -Dtest=MensageiroTest,DefaultProgressCalculationStrategyTest,AppControllerTest
```
