import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Enumeration;

class NetworkDiagnosisPanel extends JTextPane {
    public NetworkDiagnosisPanel() {
        setContentType("text/html");
        setEditable(false);
        setText(getNetworkDiagnosis());
    }

    private String getNetworkDiagnosis() {
        StringBuilder diagnosis = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            boolean hasPrivateIP = false;
            String gateway = getDefaultGateway();
            String dns = getResolvedDNS("www.google.com");

            // Teste de ping ao gateway
            String pingGatewayResult = pingHost(gateway);

            // Teste de ping ao IP 8.8.8.8
            String pingGoogleResult = pingHost("8.8.8.8");

            // Informações sobre interfaces de rede
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            String connectedNetworkType = "Não conectado";
            String wifiSSID = "Não disponível";
            String wifiDetails = "";

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress address = interfaceAddress.getAddress();
                    if (address instanceof Inet4Address) {
                        String ip = address.getHostAddress();
                        String netmask = interfaceAddress.getNetworkPrefixLength() + "";
                        if (isPrivateIP(ip)) {
                            hasPrivateIP = true;
                            diagnosis.append("<b>IP Privado:</b> ").append(ip).append(" / ").append(netmask).append("<br>");
                        }
                    }
                }

                // Ajustando a identificação para Wi-Fi e Cabeada
                if (isWiFiInterface(networkInterface)) {
                    connectedNetworkType = "Wi-Fi";
                    wifiDetails = getWiFiDetails();
                } else if (networkInterface.getName().contains("eth")) {
                    connectedNetworkType = "Cabeada";
                }
            }

            diagnosis.append("<b>Tipo de Conexão:</b> ").append(connectedNetworkType).append("<br>");
            if (connectedNetworkType.equals("Wi-Fi")) {
                diagnosis.append(wifiDetails).append("<br>");
            }

            diagnosis.append("<b>Gateway Configurado:</b> ").append(gateway != null ? gateway : "NÃO ENCONTRADO").append("<br>");
            diagnosis.append("<b>DNS Resolvido:</b> ").append(dns != null ? dns : "NÃO").append("<br>");
            diagnosis.append("<b>Ping ao Gateway:</b> ").append(pingGatewayResult).append("<br>");
            diagnosis.append("<b>Ping ao Google (8.8.8.8):</b> ").append(pingGoogleResult).append("<br>");


        } catch (Exception e) {
            diagnosis.append("Erro ao obter informações: ").append(e.getMessage());
        }
        diagnosis.append("</body></html>");
        return diagnosis.toString();
    }

    private boolean isPrivateIP(String ip) {
        return ip.startsWith("10.") || ip.startsWith("172.") || ip.startsWith("192.168.");
    }

    private String getDefaultGateway() {
        try {
            Process process = Runtime.getRuntime().exec("tracert -d www.google.com");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("1")) {
                    String[] parts = line.split("\\s+");
                    return parts[parts.length - 1];
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private String getResolvedDNS(String hostname) {
        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
            if (inetAddresses.length > 0) {
                return inetAddresses[0].getHostAddress();
            }
        } catch (UnknownHostException e) {
            return null;
        }
        return null;
    }

    private String pingHost(String host) {
        try {
            String command = "ping -n 1 " + host; // Para Windows, se for Linux, use "ping -c 1"
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "Sucesso";
            } else {
                return "Falha";
            }
        } catch (Exception e) {
            return "Erro ao testar ping";
        }
    }

    // Método para verificar se a interface é Wi-Fi
    private boolean isWiFiInterface(NetworkInterface networkInterface) {
        String interfaceName = networkInterface.getName().toLowerCase();
        return interfaceName.contains("wlan") || interfaceName.contains("wifi") || networkInterface.getDisplayName().toLowerCase().contains("wifi");
    }

    // Método para obter detalhes do Wi-Fi
    private String getWiFiDetails() {
        StringBuilder wifiDetails = new StringBuilder();
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Para Windows, podemos usar o comando netsh wlan show interfaces
                // Executa o comando
                Process process = Runtime.getRuntime().exec("netsh wlan show interfaces");

                // Lê a saída do processo
                InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim(); // Remove espaços em branco no início/fim
                    if (line.startsWith("SSID")) {
                        wifiDetails.append(" "+line+"</br>");
                    } else if (line.startsWith("Authentica")) {
                        wifiDetails.append(" "+line+"</br>");
                    } else if (line.startsWith("Faixa")) {
                        wifiDetails.append(" "+line+" </br>");
                    } else if (line.startsWith("Sinal")) {
                        wifiDetails.append(" "+line+"</br>");
                    }
                    System.out.println("WIFI DETAILS NO WHILE: "+wifiDetails.toString());
                }
            } else if (System.getProperty("os.name").toLowerCase().contains("nix")) {
                // Para Linux, podemos usar iwconfig
                Process process = Runtime.getRuntime().exec("iwconfig");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim(); // Remove espaços em branco no início/fim
                    if (line.contains("ESSID")) {
                        wifiDetails.append("<b>SSID:</b> ").append(line.split("ESSID:")[1].replaceAll("\"", "").trim()).append("<br>");
                    } else if (line.contains("Frequency")) {
                        wifiDetails.append("<b>Faixa:</b> ").append(line.split("Frequency:")[1].split(" ")[0]).append("<br>");
                    } else if (line.contains("Signal level")) {
                        wifiDetails.append("<b>Sinal:</b> ").append(line.split("Signal level=")[1].split(" ")[0]).append("<br>");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao obter detalhes Wi-Fi";
        }

        return wifiDetails.toString();
    }
}
