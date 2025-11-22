# Diagrama de Classes - Sistema de Notificações

```
┌─────────────────────────────┐
│     <<interface>>           │
│      INotificador           │
├─────────────────────────────┤
│ + enviarNotificacao()       │
│ + enviarNotificacaoComAnexo()│
└─────────────────────────────┘
           △
           │ implements
           │
    ┌──────┴───────┐
    │              │
┌───┴──────────┐ ┌─┴─────────────────┐
│NotificadorEmail│NotificadorWhatsApp│
├──────────────┤ ├───────────────────┤
│- session     │ │                   │
├──────────────┤ ├───────────────────┤
│+ enviar...() │ │+ enviar...()      │
└──────────────┘ └───────────────────┘
```

# Diagrama de Classes - Sistema de Relatórios

```
┌──────────────────────────────┐
│     <<interface>>            │
│    IGeradorRelatorio         │
├──────────────────────────────┤
│ + gerarRelatorio()           │
└──────────────────────────────┘
           △
           │ implements
    ┌──────┴─────────┐
    │                │
┌───┴────────────┐ ┌─┴──────────────────────────┐
│GeradorRelatorioPDF│  IGeradorRelatorioAvancado│
├────────────────┤ ├────────────────────────────┤
│                │ │+ gerarRelatorioComColunaExtra()
├────────────────┤ └────────────────────────────┘
│+ gerarRelatorio()│              △
└────────────────┘              │ implements
                                │
                      ┌─────────┴────────────┐
                      │ GeradorRelatorioExcel│
                      ├──────────────────────┤
                      │                      │
                      ├──────────────────────┤
                      │+ gerarRelatorio()    │
                      │+ gerarRelatorio...() │
                      └──────────────────────┘
```

# Diagrama de Classes - ReportServiceImpl

```
┌──────────────────────────────────────┐
│        ReportServiceImpl             │
├──────────────────────────────────────┤
│- taskService: ITaskService           │
│- notificador: INotificador           │◄─────────────┐
│- geradorPDF: IGeradorRelatorio       │◄────────┐    │
│- geradorExcel: IGeradorRelatorioAv...│◄───┐    │    │
├──────────────────────────────────────┤    │    │    │
│+ enviarRelatorioTarefasDoDiaPor...() │    │    │    │
│+ gerarRelatorioTarefasPorMes()       │    │    │    │
└──────────────────────────────────────┘    │    │    │
                                            │    │    │
                                            │    │    │
                  ┌─────────────────────────┘    │    │
                  │                              │    │
        ┌─────────┴────────────┐    ┌────────────┴────┴──────┐
        │ GeradorRelatorioExcel│    │  GeradorRelatorioPDF   │
        └──────────────────────┘    └────────────────────────┘
                                                 │
                                    ┌────────────┴─────────────┐
                                    │    NotificadorEmail      │
                                    └──────────────────────────┘
```

# Fluxo de Dependências (Antes vs Depois)

## ANTES (Acoplamento Forte):
```
ReportServiceImpl
        │
        ├─> Mensageiro (classe concreta)
        └─> Central (classe concreta)
```

## DEPOIS (Inversão de Dependência):
```
ReportServiceImpl
        │
        ├─> INotificador (interface)
        │        │
        │        └─> NotificadorEmail (implementação)
        │
        ├─> IGeradorRelatorio (interface)
        │        │
        │        └─> GeradorRelatorioPDF (implementação)
        │
        └─> IGeradorRelatorioAvancado (interface)
                 │
                 └─> GeradorRelatorioExcel (implementação)
```

## Vantagens do Novo Design:
1. **Baixo Acoplamento**: ReportServiceImpl não conhece implementações concretas
2. **Alta Coesão**: Cada classe tem uma única responsabilidade
3. **Flexibilidade**: Fácil trocar implementações (Email → WhatsApp, PDF → CSV)
4. **Testabilidade**: Fácil criar mocks das interfaces para testes
5. **Extensibilidade**: Adicionar novos tipos sem modificar código existente
