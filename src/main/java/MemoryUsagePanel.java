import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.*;
import java.awt.event.*;

class MemoryUsagePanel extends JTextPane {
    private Timer timer;

    public MemoryUsagePanel() {
        setContentType("text/html");
        setEditable(false);

        // Iniciar o Timer para atualizar os dados a cada 5 segundos
        startTimer();
    }

    private void startTimer() {
        // Cria um Timer que atualiza o painel a cada 5000 milissegundos (5 segundos)
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Garanta que a atualização da interface gráfica seja feita no EDT
                SwingUtilities.invokeLater(() -> setText(getMemoryUsage()));
            }
        }, 0, 5000);  // Inicia o timer, repetindo a cada 5000 ms (5 segundos)
    }

    private String getMemoryUsage() {
        StringBuilder memoryUsage = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            // Obtendo informações sobre o uso de memória
            List<ProcessInfo> processes = getTopMemoryConsumingProcesses();

            memoryUsage.append("<b>Processos que consomem mais memória:</b><br>");
            if (processes.isEmpty()) {
                memoryUsage.append("Nenhum processo encontrado ou não foi possível obter as informações.<br>");
            } else {
                for (ProcessInfo process : processes) {
                    memoryUsage.append("<b>Processo:</b> ").append(process.getName())
                            .append(" - <b>Memória (MB):</b> ").append(process.getMemoryUsage()).append("<br>");
                }
            }

        } catch (Exception e) {
            memoryUsage.append("Erro ao obter informações: ").append(e.getMessage());
        }
        memoryUsage.append("</body></html>");
        return memoryUsage.toString();
    }



    private List<ProcessInfo> getTopMemoryConsumingProcesses() {
        List<ProcessInfo> processes = new ArrayList<>();
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process process;

            // Dependendo do sistema operacional, executa o comando adequado
            if (os.contains("win")) {
                process = new ProcessBuilder("tasklist", "/fo", "csv", "/nh").start();
            } else {
                process = new ProcessBuilder("ps", "-eo", "pid,comm,%mem", "--sort=-%mem").start();
            }

            // Captura a saída do comando
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (os.contains("win")) {
                    // Para Windows, o formato CSV é utilizado
                    String[] columns = line.split(",");
                    String name = columns[0].replace("\"", "");
                    // Limpeza e conversão do valor de memória
                    String memoryStr = columns[4].replace("\"", "").trim();


                    // Tratamento para memória em K ou M
                    if (memoryStr.endsWith(" K")) {
                        memoryStr = memoryStr.replace(" K", "");
                        try {
                            double memoryValue = Double.parseDouble(memoryStr);
                            int memoryUsage = (int) memoryValue;
                            processes.add(new ProcessInfo(name, memoryUsage));
                            //System.out.println("Processo " + name + "Linha de memória: " + memoryStr +" Memoria INT: "+memoryUsage); // Adiciona para depuração
                        } catch (NumberFormatException e) {
                            // Ignora processos com dados inválidos de memória
                        }
                    } else if (memoryStr.endsWith(" M")) {
                        memoryStr = memoryStr.replace(" M", "");
                        try {
                            double memoryValue = Double.parseDouble(memoryStr);
                            int memoryUsage = (int) memoryValue;
                            processes.add(new ProcessInfo(name, memoryUsage));
                            //System.out.println("Processo " + name + "Linha de memória: " + memoryStr +" Memoria INT: "+memoryUsage); // Adiciona para depuração
                        } catch (NumberFormatException e) {
                            // Ignora processos com dados inválidos de memória
                        }
                    }
                } else {
                    // Para Unix/Linux, processa a saída do comando ps
                    String[] columns = line.trim().split("\\s+");
                    String name = columns[1];
                    String memoryStr = columns[2].replaceAll("[^0-9.]", ""); // Remove caracteres não numéricos
                    if (!memoryStr.isEmpty()) {
                        try {
                            double memoryPercentage = Double.parseDouble(memoryStr); // Usa o valor de % de memória
                            double totalMemory = getTotalMemory(); // Obtém a memória total do sistema
                            int memoryUsage = (int) (memoryPercentage / 100 * totalMemory); // Calcula a memória real em MB
                            processes.add(new ProcessInfo(name, memoryUsage));
                        } catch (NumberFormatException e) {
                            // Ignora processos com dados inválidos de memória
                        }
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ordenando pela memória utilizada em ordem decrescente e limitando a 10 processos
        return processes.stream()
                .sorted(Comparator.comparingInt(ProcessInfo::getMemoryUsage).reversed())
                .limit(25)
                .collect(Collectors.toList());
    }

    // Método para obter a memória total do sistema (em MB)
    private double getTotalMemory() {
        double totalMemory = 0;
        try {
            Process process = new ProcessBuilder("wmic", "ComputerSystem", "get", "TotalPhysicalMemory").start();
            Scanner scanner = new Scanner(process.getInputStream());
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Pula o cabeçalho
            }
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                totalMemory = Double.parseDouble(line) / 1024 / 1024; // Converte de bytes para MB
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalMemory;
    }

    // Classe interna para representar um processo
    class ProcessInfo {
        private String name;
        private int memoryUsage; // Memória usada pelo processo em MB

        public ProcessInfo(String name, int memoryUsage) {
            this.name = name;
            this.memoryUsage = memoryUsage;
        }

        public String getName() {
            return name;
        }

        public int getMemoryUsage() {
            return memoryUsage;
        }
    }
}
