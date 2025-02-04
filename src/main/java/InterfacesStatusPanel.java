import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InterfacesStatusPanel extends JPanel {

    public InterfacesStatusPanel() {
        setLayout(new BorderLayout());

        JEditorPane editorPane = new JEditorPane("text/html", "");
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);

        add(new JLabel("Status das Interfaces"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Verifica o status das interfaces
        String usbStatus = getInterfaceStatus("USB");
        String vgaStatus = getInterfaceStatus("VGA");
        String displayPortStatus = getInterfaceStatus("DisplayPort");

        // Formata os resultados como HTML
        String result = "<html><body style='font-family: Arial; font-size: 14px;'>";
        result += "<h2>USB:</h2>";
        result += usbStatus;
        result += "<hr>";
        result += "<h2>VGA:</h2>";
        result += vgaStatus;
        result += "<hr>";
        result += "<h2>DisplayPort:</h2>";
        result += displayPortStatus;
        result += "</body></html>";

        editorPane.setText(result);
    }


    private String getInterfaceStatus(String interfaceName) {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Para Windows, usa o comando WMIC
            return getStatusWindows(interfaceName);
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Para sistemas Linux ou macOS, usa o comando adequado
            return getStatusUnix(interfaceName);
        } else {
            return "Sistema não suportado";
        }
    }

    // Método para verificar status no Windows
    private String getStatusWindows(String interfaceName) {
        try {
            String command = "wmic path Win32_PnPEntity where \"Description like '%" + interfaceName + "%'\" get Caption";
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean found = false;
            StringBuilder interfacesFound = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains(interfaceName)) {
                    found = true;
                    //interfacesFound.append(line).append("\n");
                    interfacesFound.append(line).append("<br>");
                }
            }
            if (!found) {
                return "Não há " + interfaceName;
            } else {
                //return interfacesFound.toString() + "\nStatus: Em uso";
                return interfacesFound.toString() + "<br>Status: Em uso";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao verificar";
        }
    }

    // Método para verificar status no Linux/macOS
    private String getStatusUnix(String interfaceName) {
        try {
            String command = "";
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                // Para Linux, utiliza o lspci
                command = "lspci | grep -i " + interfaceName;
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                // Para macOS, usa system_profiler
                command = "system_profiler SPUSBDataType | grep -i " + interfaceName;
            }

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean found = false;
            StringBuilder interfacesFound = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(interfaceName.toLowerCase())) {
                    found = true;
                    interfacesFound.append(line).append("\n");
                }
            }
            if (!found) {
                return "Não há " + interfaceName;
            } else {
                return "Interfaces encontradas:\n" + interfacesFound.toString() + "\nStatus: Em uso";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao verificar";
        }
    }
}
