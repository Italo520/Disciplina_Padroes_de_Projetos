# Diagramas de Classes

## Sistema de Notificações

```mermaid
classDiagram
    class INotificador {
        <<interface>>
        +enviarNotificacao()
        +enviarNotificacaoComAnexo()
    }

    class NotificadorEmail {
        -session
        +enviarNotificacao()
        +enviarNotificacaoComAnexo()
    }

    class NotificadorWhatsApp {
        +enviarNotificacao()
        +enviarNotificacaoComAnexo()
    }

    INotificador <|.. NotificadorEmail : implements
    INotificador <|.. NotificadorWhatsApp : implements
```

## Sistema de Relatórios

```mermaid
classDiagram
    class IGeradorRelatorio {
        <<interface>>
        +gerarRelatorio()
    }

    class IGeradorRelatorioAvancado {
        <<interface>>
        +gerarRelatorioComColunaExtra()
    }

    class GeradorRelatorioPDF {
        +gerarRelatorio()
    }

    class GeradorRelatorioExcel {
        +gerarRelatorio()
        +gerarRelatorioComColunaExtra()
    }

    IGeradorRelatorio <|.. GeradorRelatorioPDF : implements
    IGeradorRelatorio <|-- IGeradorRelatorioAvancado : extends
    IGeradorRelatorioAvancado <|.. GeradorRelatorioExcel : implements
```

## ReportServiceImpl e Dependências

```mermaid
classDiagram
    class ReportServiceImpl {
        -taskService: ITaskService
        -notificador: INotificador
        -geradorPDF: IGeradorRelatorio
        -geradorExcel: IGeradorRelatorioAvancado
        +enviarRelatorioTarefasDoDiaPorEmail()
        +gerarRelatorioTarefasPorMes()
    }

    class INotificador {
        <<interface>>
    }

    class IGeradorRelatorio {
        <<interface>>
    }

    class IGeradorRelatorioAvancado {
        <<interface>>
    }

    ReportServiceImpl --> INotificador
    ReportServiceImpl --> IGeradorRelatorio
    ReportServiceImpl --> IGeradorRelatorioAvancado
```

## Fluxo de Dependências (Inversão de Dependência)

```mermaid
graph TD
    Service[ReportServiceImpl]
    INot[<<interface>> INotificador]
    IGer[<<interface>> IGeradorRelatorio]
    IGerAdv[<<interface>> IGeradorRelatorioAvancado]
    
    Email[NotificadorEmail]
    PDF[GeradorRelatorioPDF]
    Excel[GeradorRelatorioExcel]

    Service --> INot
    Service --> IGer
    Service --> IGerAdv

    INot <|.. Email
    IGer <|.. PDF
    IGerAdv <|.. Excel
```
