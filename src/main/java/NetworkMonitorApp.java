import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkMonitorApp extends JFrame {

    private JLabel statusLabel;
    private boolean wifiConnected = false;

    public NetworkMonitorApp() {
        setTitle("Monitor de Rede Cabeada e Wi-Fi");
        setSize(400, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        statusLabel = new JLabel("Status da Rede: Verificando...");
        add(statusLabel);

        monitorNetworkConnectivity();
    }

    private void monitorNetworkConnectivity() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean ethernetActive = isEthernetConnected();
                boolean wifiActive = isWifiConnected();

                if (ethernetActive) {
                    statusLabel.setText("Conexão Cabeada Ativa");
                    statusLabel.setForeground(Color.GREEN);

                    if (wifiActive) {
                        int response = JOptionPane.showConfirmDialog(
                                null,
                                "A conexão cabeada está ativa, mas você também está conectado ao Wi-Fi. Deseja desconectar do Wi-Fi?",
                                "Escolha de Rede",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (response == JOptionPane.YES_OPTION) {
                            disconnectWifi();
                            wifiConnected = false;
                        }
                    }
                } else {
                    statusLabel.setText("Conexão Cabeada Perdida!");
                    statusLabel.setForeground(Color.RED);

                    if (!wifiConnected && !wifiActive) {
                        int response = JOptionPane.showConfirmDialog(
                                null,
                                "Conexão cabeada perdida! Deseja conectar-se a uma rede Wi-Fi?",
                                "Alerta de Rede",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (response == JOptionPane.YES_OPTION) {
                            connectToWifi();
                        }
                    } else if (wifiActive) {
                        statusLabel.setText("Conexão Cabeada Perdida! Wi-Fi Conectado.");
                        statusLabel.setForeground(Color.BLUE);
                        wifiConnected = true;
                    }
                }
            }
        }, 0, 5000); // verifica a cada 5 segundos
    }

    private boolean isEthernetConnected() {
        try {
            Process process = Runtime.getRuntime().exec("ipconfig");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean ethernetFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Ethernet") && !line.contains("Wi-Fi")) {
                    ethernetFound = true;
                }
                if (ethernetFound && line.trim().startsWith("Media")) {
                    return !line.contains("disconnected");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isWifiConnected() {
        try {
            Process process = Runtime.getRuntime().exec("netsh wlan show interfaces");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Estado") && line.contains("Conectado")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void connectToWifi() {
        try {
            Process listNetworks = Runtime.getRuntime().exec("netsh wlan show networks");
            BufferedReader reader = new BufferedReader(new InputStreamReader(listNetworks.getInputStream()));
            String line;
            java.util.List<String> ssidList = new java.util.ArrayList<>();

            // Processar a saída do comando para capturar os SSIDs disponíveis
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("SSID")) {
                    String ssid = line.split(":")[1].trim();
                    if (!ssid.isEmpty() && !ssidList.contains(ssid)) { // Evitar SSIDs duplicados
                        ssidList.add(ssid);
                    }
                }
            }

            if (ssidList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma rede Wi-Fi disponível.");
                return;
            }

            // Mostrar redes disponíveis e permitir que o usuário escolha
            String selectedNetwork = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecione uma rede Wi-Fi:",
                    "Redes Wi-Fi Disponíveis",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ssidList.toArray(),
                    ssidList.get(0)
            );

            if (selectedNetwork != null) {
                // Verificar se já está conectado ao SSID selecionado
                if (isAlreadyConnectedToSSID(selectedNetwork)) {
                    JOptionPane.showMessageDialog(this, "Você já está conectado à rede \"" + selectedNetwork + "\".");
                    return;
                }

                // Solicitar a senha da rede
                String password = JOptionPane.showInputDialog(this, "Digite a senha da rede Wi-Fi \"" + selectedNetwork + "\":");
                if (password != null && !password.isEmpty()) {
                    // Criar um perfil temporário para conexão com a rede
                    String profileContent =
                            "<?xml version=\"1.0\"?>\n" +
                                    "<WLANProfile xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v1\">\n" +
                                    "    <name>" + selectedNetwork + "</name>\n" +
                                    "    <SSIDConfig>\n" +
                                    "        <SSID>\n" +
                                    "            <name>" + selectedNetwork + "</name>\n" +
                                    "        </SSID>\n" +
                                    "    </SSIDConfig>\n" +
                                    "    <connectionType>ESS</connectionType>\n" +
                                    "    <connectionMode>manual</connectionMode>\n" +
                                    "    <MSM>\n" +
                                    "        <security>\n" +
                                    "            <authEncryption>\n" +
                                    "                <authentication>WPA2PSK</authentication>\n" +
                                    "                <encryption>AES</encryption>\n" +
                                    "                <useOneX>false</useOneX>\n" +
                                    "            </authEncryption>\n" +
                                    "            <sharedKey>\n" +
                                    "                <keyType>passPhrase</keyType>\n" +
                                    "                <protected>false</protected>\n" +
                                    "                <keyMaterial>" + password + "</keyMaterial>\n" +
                                    "            </sharedKey>\n" +
                                    "        </security>\n" +
                                    "    </MSM>\n" +
                                    "</WLANProfile>";

                    String profileFile = "wifi-profile.xml";
                    java.nio.file.Files.write(java.nio.file.Paths.get(profileFile), profileContent.getBytes());

                    // Adicionar e conectar ao perfil
                    Process addProfileProcess = Runtime.getRuntime().exec("netsh wlan add profile filename=\"" + profileFile + "\"");
                    addProfileProcess.waitFor();
                    Process connectProcess = Runtime.getRuntime().exec("netsh wlan connect name=\"" + selectedNetwork + "\"");
                    connectProcess.waitFor();

                    JOptionPane.showMessageDialog(this, "Conexão com a rede Wi-Fi \"" + selectedNetwork + "\" concluída!");
                    wifiConnected = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma senha fornecida. Conexão cancelada.");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao tentar conectar à rede Wi-Fi!");
        }
    }

    private boolean isAlreadyConnectedToSSID(String ssid) {
        try {
            Process process = Runtime.getRuntime().exec("netsh wlan show interfaces");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("SSID") && line.contains(ssid)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }




    private void disconnectWifi() {
        try {
            Process disconnectProcess = Runtime.getRuntime().exec("netsh wlan disconnect");
            disconnectProcess.waitFor();
            JOptionPane.showMessageDialog(this, "Desconectado do Wi-Fi. Utilizando apenas a rede cabeada.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao tentar desconectar do Wi-Fi!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkMonitorApp().setVisible(true));
    }
}


