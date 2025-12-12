# Padrões de Projeto Implementados

Este documento detalha a aplicação dos padrões de projeto (GoF) na arquitetura do sistema To-Do List.

## 1. Observer
**Onde:** `br.com.todolist.log.EventAuditObserver` e `br.com.todolist.log.TaskAuditObserver`

O padrão Observer é utilizado para monitorar mudanças de estado em objetos importantes e notificar interessados. No nosso caso, os observers de auditoria escutam alterações em tarefas e eventos.

- **Sujeito (Subject):** `TaskService`, `EventService`
- **Observador (Observer):** `TaskAuditObserver`, `EventAuditObserver`
- **Funcionamento:** Quando uma tarefa ou evento é criado, atualizado ou excluído, o serviço notifica o observador, que registra a ação no log (MongoDB).

## 2. Factory Method
**Onde:** `br.com.todolist.service.util.DefaultItemFactory`

Utilizado para encapsular a lógica de criação de objetos complexos, como Tarefas e Eventos, garantindo que eles sejam instanciados com estados iniciais consistentes.

- **Produto:** `Tarefa`, `Evento`
- **Criador:** `DefaultItemFactory`
- **Uso:** O `AppController` utiliza a fábrica para criar novas instâncias baseadas na entrada do usuário, sem conhecer a lógica de instanciação concreta.

## 3. Strategy
**Onde:** `br.com.todolist.util.notificacao.INotificador` e `br.com.todolist.service.IReportService`

O padrão Strategy permite definir uma família de algoritmos, encapsulá-los e torná-los intercambiáveis.

### Notificações
- **Interface:** `INotificador`
- **Implementações:** `NotificadorEmail`, `NotificadorWhatsApp` (exemplo).
- **Benefício:** Permite trocar o meio de notificação (Email, SMS, WhatsApp) sem alterar o serviço que dispara a notificação.

### Relatórios
- **Interface:** `IGeradorRelatorio` (usado internamente pelo `ReportService`)
- **Implementações:** `GeradorRelatorioPDF`, `GeradorRelatorioExcel`.
- **Benefício:** Permite selecionar o formato de relatório desejado em tempo de execução.

## 4. Singleton
**Onde:** `br.com.todolist.service.SessionManager` e `br.com.todolist.controller.AppController`

Garante que uma classe tenha apenas uma instância e fornece um ponto global de acesso a ela.

- **SessionManager:** Gerencia a sessão do usuário logado e mantém as referências aos serviços configurados para o usuário atual.
- **AppController:** Centraliza a coordenação entre as views e os services, atuando como uma Fachada (Facade) Singleton.
