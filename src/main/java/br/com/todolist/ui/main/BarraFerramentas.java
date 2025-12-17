package br.com.todolist.ui.main;

import br.com.todolist.controller.AppController;
import br.com.todolist.controller.EventController;
import br.com.todolist.controller.TaskController;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;
import java.awt.Cursor;
import java.awt.Window;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class BarraFerramentas {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(BarraFerramentas.class.getName());
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATADOR_MES_ANO = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final String SUCCESS_TITLE = "Sucesso";
    private static final String INFO_TITLE = "Informação";
    private static final String SOBRE_MENSAGEM = """
            Aplicação de Lista de Tarefas
            Versão 2.0
            Criado Por: Ítalo Santos e Rickson Costa
            Disciplina de POO
            Curso ADS - IFPB
            2025""";

    private BarraFerramentas() {
        throw new IllegalStateException("Classe utilitária");
    }

    public static JMenuBar criarBarraFerramentas(TelaPrincipal frame, TaskController taskController,
            EventController eventController) {
        JMenuBar menuBar = new JMenuBar();
        AppController appController = AppController.getInstance();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAparencia = new JMenu("Aparência");
        JMenu menuAjuda = new JMenu("Ajuda");

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        JMenuItem listarTodasTarefas = new JMenuItem("Listar Todas as Tarefas");
        listarTodasTarefas.addActionListener(new OuvinteListarTodasTarefas(frame, taskController));

        JMenuItem listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        listarTarefasPorDia.addActionListener(new OuvinteListarTarefasPorDia(frame, taskController));

        JMenuItem listarTarefasCriticas = new JMenuItem("Listar Tarefas Críticas");
        listarTarefasCriticas.addActionListener(new OuvinteListarTarefasCriticas(frame, taskController));

        JMenuItem pdfDoDia = new JMenuItem("Gerar PDF das Tarefas do Dia");
        pdfDoDia.addActionListener(new OuvinteGerarPdfTarefas(frame, appController));

        JMenuItem enviarEmailTarefas = new JMenuItem("Enviar Tarefas do Dia por Email");
        enviarEmailTarefas.addActionListener(new OuvinteEnviarEmailTarefas(frame, appController));

        JMenuItem relatorioTarefasPorMes = new JMenuItem("Relatório de Tarefas por Mês (Excel)");
        relatorioTarefasPorMes.addActionListener(new OuvinteGerarExcelTarefas(frame, appController));

        JMenuItem listarEventosPorDia = new JMenuItem("Listar Eventos por Dia");
        listarEventosPorDia.addActionListener(new OuvinteListarEventosPorDia(frame, eventController));

        JMenuItem listarEventosMesEspecifico = new JMenuItem("Listar Eventos por Mês");
        listarEventosMesEspecifico.addActionListener(new OuvinteListarEventosPorMes(frame, eventController));

        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                SOBRE_MENSAGEM,
                "Sobre", JOptionPane.INFORMATION_MESSAGE));

        ButtonGroup grupoDeTemas = new ButtonGroup();

        adicionarTemaNoMenu(menuAparencia, grupoDeTemas, "Carbon (Padrão)", FlatCarbonIJTheme.class.getName(), true);
        adicionarTemaNoMenu(menuAparencia, grupoDeTemas, "Dracula", FlatDraculaIJTheme.class.getName(), false);
        adicionarTemaNoMenu(menuAparencia, grupoDeTemas, "Solarized Light", FlatSolarizedLightIJTheme.class.getName(),
                false);
        adicionarTemaNoMenu(menuAparencia, grupoDeTemas, "High Contrast", FlatHighContrastIJTheme.class.getName(),
                false);
        adicionarTemaNoMenu(menuAparencia, grupoDeTemas, "Vuesion", FlatVuesionIJTheme.class.getName(), false);
        adicionarTemaNoMenu(menuAparencia, grupoDeTemas, "Light", FlatLightLaf.class.getName(), false);
        adicionarTemaNoMenu(menuAparencia, grupoDeTemas, "Dark", FlatDarkLaf.class.getName(), false);

        menuAjuda.add(itemSobre);
        menuEventos.add(listarEventosPorDia);
        menuEventos.add(listarEventosMesEspecifico);
        menuTarefas.add(listarTodasTarefas);
        menuTarefas.add(listarTarefasPorDia);
        menuTarefas.add(listarTarefasCriticas);
        menuTarefas.addSeparator();
        menuTarefas.add(pdfDoDia);
        menuTarefas.add(enviarEmailTarefas);
        menuTarefas.add(relatorioTarefasPorMes);
        JMenuItem itemLogout = new JMenuItem("Logout");
        itemLogout.addActionListener(e -> {
            frame.dispose();
            new br.com.todolist.ui.auth.TelaLogin().setVisible(true);
        });

        menuArquivo.add(itemLogout);
        menuArquivo.add(itemSair);
        menuBar.add(menuArquivo);
        menuBar.add(menuTarefas);
        menuBar.add(menuEventos);
        menuBar.add(menuAparencia);
        menuBar.add(menuAjuda);

        return menuBar;
    }

    private static void adicionarTemaNoMenu(JMenu menu, ButtonGroup grupo, String nome, String className,
            boolean selecionado) {
        JRadioButtonMenuItem itemMenu = new JRadioButtonMenuItem(nome, selecionado);
        itemMenu.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(className);
                for (Window window : Window.getWindows()) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            } catch (Exception ex) {
                LOGGER.log(java.util.logging.Level.SEVERE, ex, () -> "Falha ao aplicar o tema: " + className);
            }
        });
        grupo.add(itemMenu);
        menu.add(itemMenu);
    }

    private static Optional<LocalDate> obterDataDoUsuario(JFrame frame, String mensagem) {
        String dataInput = JOptionPane.showInputDialog(frame, mensagem);
        if (dataInput == null || dataInput.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(dataInput, FORMATADOR_DATA));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Formato de data inválido! Use dd/MM/yyyy.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private static Optional<YearMonth> obterMesAnoDoUsuario(JFrame frame, String mensagem) {
        String mesAnoInput = JOptionPane.showInputDialog(frame, mensagem);
        if (mesAnoInput == null || mesAnoInput.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(YearMonth.parse(mesAnoInput, FORMATADOR_MES_ANO));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Formato de data inválido! Use MM/yyyy.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private static class OuvinteListarTodasTarefas implements ActionListener {
        private final TelaPrincipal frame;
        private final TaskController taskController;

        public OuvinteListarTodasTarefas(TelaPrincipal frame, TaskController taskController) {
            this.frame = frame;
            this.taskController = taskController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Tarefa> tarefas = taskController.listarTodasTarefas();
            if (tarefas.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nenhuma tarefa encontrada.",
                        INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
            }
            frame.atualizarPainelDeTarefas(tarefas);
        }
    }

    private static class OuvinteListarTarefasPorDia implements ActionListener {
        private final TelaPrincipal frame;
        private final TaskController taskController;

        public OuvinteListarTarefasPorDia(TelaPrincipal frame, TaskController taskController) {
            this.frame = frame;
            this.taskController = taskController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para listar as tarefas (dd/MM/yyyy):")
                    .ifPresent(dia -> {
                        List<Tarefa> tarefas = taskController.listarTarefasPorDia(dia);
                        if (tarefas.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Nenhuma tarefa encontrada para esta data.",
                                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
                        }
                        frame.atualizarPainelDeTarefas(tarefas);
                    });
        }
    }

    private static class OuvinteListarTarefasCriticas implements ActionListener {
        private final TelaPrincipal frame;
        private final TaskController taskController;

        public OuvinteListarTarefasCriticas(TelaPrincipal frame, TaskController taskController) {
            this.frame = frame;
            this.taskController = taskController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Tarefa> tarefas = taskController.listarTarefasCriticas();
            if (tarefas.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nenhuma tarefa crítica encontrada.", INFO_TITLE,
                        JOptionPane.INFORMATION_MESSAGE);
            }
            frame.atualizarPainelDeTarefas(tarefas);
        }
    }

    private static class OuvinteListarEventosPorDia implements ActionListener {
        private final TelaPrincipal frame;
        private final EventController eventController;

        public OuvinteListarEventosPorDia(TelaPrincipal frame, EventController eventController) {
            this.frame = frame;
            this.eventController = eventController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para listar os eventos (dd/MM/yyyy):")
                    .ifPresent(dia -> {
                        List<Evento> eventos = eventController.listarEventosPorDia(dia);
                        if (eventos.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Nenhum evento encontrado para esta data.",
                                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
                        }
                        frame.atualizarPainelDeEventos(eventos);
                    });
        }
    }

    private static class OuvinteListarEventosPorMes implements ActionListener {
        private final TelaPrincipal frame;
        private final EventController eventController;

        public OuvinteListarEventosPorMes(TelaPrincipal frame, EventController eventController) {
            this.frame = frame;
            this.eventController = eventController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            obterMesAnoDoUsuario(frame, "Digite o mês e ano (MM/yyyy):")
                    .ifPresent(mes -> {
                        List<Evento> eventos = eventController.listarEventosPorMes(mes);
                        if (eventos.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Nenhum evento encontrado para este mês.",
                                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
                        }
                        frame.atualizarPainelDeEventos(eventos);
                    });
        }
    }

    private static class OuvinteGerarPdfTarefas implements ActionListener {
        private final JFrame frame;
        private final AppController appController;

        public OuvinteGerarPdfTarefas(JFrame frame, AppController appController) {
            this.frame = frame;
            this.appController = appController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para gerar o PDF (dd/MM/yyyy):")
                    .ifPresent(dia -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Salvar Relatório PDF");
                        fileChooser.setSelectedFile(new java.io.File("Relatorio_Tarefas_" + dia.toString() + ".pdf"));

                        int userSelection = fileChooser.showSaveDialog(frame);
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            java.io.File fileToSave = fileChooser.getSelectedFile();
                            String filePath = fileToSave.getAbsolutePath();
                            if (!filePath.toLowerCase().endsWith(".pdf")) {
                                filePath += ".pdf";
                            }

                            try {
                                appController.gerarRelatorioPDFTarefasDoDia(dia, filePath);
                                JOptionPane.showMessageDialog(frame, "PDF salvo com sucesso em:\n" + filePath,
                                        SUCCESS_TITLE,
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Erro ao gerar PDF: " + ex.getMessage(), "Erro",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
        }
    }

    private static class OuvinteEnviarEmailTarefas implements ActionListener {
        private final JFrame frame;
        private final AppController appController;

        public OuvinteEnviarEmailTarefas(JFrame frame, AppController appController) {
            this.frame = frame;
            this.appController = appController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para o envio do relatório (dd/MM/yyyy):")
                    .ifPresent(dia -> {
                        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        JOptionPane.showMessageDialog(frame, "Enviando e-mail em segundo plano...", "Aguarde",
                                JOptionPane.INFORMATION_MESSAGE);

                        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                            @Override
                            protected Boolean doInBackground() throws Exception {
                                return appController.enviarRelatorioTarefasDoDiaPorEmail(dia);
                            }

                            @Override
                            protected void done() {
                                try {
                                    boolean sucesso = get();
                                    if (sucesso) {
                                        JOptionPane.showMessageDialog(frame,
                                                "Email com o relatório em anexo enviado com sucesso!", SUCCESS_TITLE,
                                                JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(frame,
                                                "Não foi possível enviar o email.\nVerifique se existem tarefas para a data informada.",
                                                "Erro", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                    JOptionPane.showMessageDialog(frame,
                                            "Operação de envio de e-mail foi interrompida.", "Operação Interrompida",
                                            JOptionPane.WARNING_MESSAGE);
                                } catch (ExecutionException ex) {
                                    JOptionPane.showMessageDialog(frame,
                                            "Ocorreu um erro inesperado ao enviar o e-mail.", "Erro Crítico",
                                            JOptionPane.ERROR_MESSAGE);
                                    ex.printStackTrace();
                                } finally {
                                    frame.setCursor(Cursor.getDefaultCursor());
                                }
                            }
                        };

                        worker.execute();
                    });
        }
    }

    private static class OuvinteGerarExcelTarefas implements ActionListener {
        private final JFrame frame;
        private final AppController appController;

        public OuvinteGerarExcelTarefas(JFrame frame, AppController appController) {
            this.frame = frame;
            this.appController = appController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            obterMesAnoDoUsuario(frame, "Digite o mês e ano para o relatório (MM/yyyy):")
                    .ifPresent(mes -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Salvar Relatório Excel");
                        fileChooser.setSelectedFile(new java.io.File(
                                "Relatorio_Tarefas_" + mes.format(DateTimeFormatter.ofPattern("MM_yyyy")) + ".xlsx"));

                        int userSelection = fileChooser.showSaveDialog(frame);
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            java.io.File fileToSave = fileChooser.getSelectedFile();
                            String filePath = fileToSave.getAbsolutePath();
                            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                                filePath += ".xlsx";
                            }

                            try {
                                appController.gerarRelatorioTarefasPorMes(mes, filePath);
                                JOptionPane.showMessageDialog(frame,
                                        "Relatório Excel salvo com sucesso em:\n" + filePath, SUCCESS_TITLE,
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Erro ao gerar Excel: " + ex.getMessage(), "Erro",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
        }
    }
}