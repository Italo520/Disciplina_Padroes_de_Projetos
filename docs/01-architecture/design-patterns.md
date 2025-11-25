# Padrões de Projeto Implementados

Este documento detalha a aplicação dos padrões de projeto (GoF) na arquitetura do sistema To-Do List.

## 1. Observer
**Onde:** `br.com.todolist.observer.EventAuditObserver`

O padrão Observer é utilizado para monitorar mudanças de estado em objetos importantes e notificar interessados. No nosso caso, o `EventAuditObserver` escuta alterações em tarefas e eventos para fins de auditoria.

- **Sujeito (Subject):** `TaskService`, `EventService`
- **Observador (Observer):** `EventAuditObserver`
- **Funcionamento:** Quando uma tarefa é criada ou concluída, o serviço notifica o observador, que registra a ação no log (MongoDB).

## 2. Factory Method
**Onde:** `br.com.todolist.factory.DefaultItemFactory`

Utilizado para encapsular a lógica de criação de objetos complexos, como Tarefas e Eventos, garantindo que eles sejam instanciados com estados iniciais consistentes.

- **Produto:** `Tarefa`, `Evento`
- **Criador:** `DefaultItemFactory`
- **Uso:** O `AppController` utiliza a fábrica para criar novas instâncias baseadas na entrada do usuário, sem conhecer a lógica de instanciação concreta.

## 3. Strategy
**Onde:** `br.com.todolist.strategy.IProgressCalculationStrategy` e `br.com.todolist.util.notificacao.INotificador`

O padrão Strategy permite definir uma família de algoritmos, encapsulá-los e torná-los intercambiáveis.

### Cálculo de Progresso
- **Interface:** `IProgressCalculationStrategy`
- **Implementações:** Estratégias para cálculo simples (baseado em checkbox) ou complexo (baseado em subtarefas).
- **Benefício:** Permite mudar a forma como o progresso é calculado sem alterar a classe `Tarefa`.

### Notificações
- **Interface:** `INotificador`
- **Implementações:** `NotificadorEmail`, `NotificadorWhatsApp` (exemplo).
- **Benefício:** Permite trocar o meio de notificação (Email, SMS, WhatsApp) sem alterar o serviço que dispara a notificação.

### Relatórios
- **Interface:** `IGeradorRelatorio`
- **Implementações:** `GeradorRelatorioPDF`, `GeradorRelatorioExcel`.

## 4. Singleton
**Onde:** `br.com.todolist.util.SessionManager` e `br.com.todolist.controller.AppController`

Garante que uma classe tenha apenas uma instância e fornece um ponto global de acesso a ela.

- **SessionManager:** Gerencia a sessão do usuário logado. Só pode haver uma sessão ativa por vez no cliente.
- **AppController:** Centraliza a coordenação entre as views e os services.
