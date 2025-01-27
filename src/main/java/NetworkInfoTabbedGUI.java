import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

public class NetworkInfoTabbedGUI {
    static Urls urls = new Urls();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkInfoTabbedGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Autodiagnóstico de TI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Banner superior
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new BorderLayout());
        bannerPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));

        JLabel bannerLabel = new JLabel("Autodiagnóstico de TI", JLabel.CENTER);
        bannerLabel.setOpaque(true);
        bannerLabel.setBackground(new Color(0, 0, 128)); // Azul escuro
        bannerLabel.setForeground(Color.WHITE); // Texto branco
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel redLine = new JPanel();
        redLine.setBackground(new Color(139, 0, 0)); // Vermelho escuro
        redLine.setPreferredSize(new Dimension(frame.getWidth(), 5));

        bannerPanel.add(redLine, BorderLayout.NORTH);
        bannerPanel.add(bannerLabel, BorderLayout.CENTER);

        // Painel de abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Aba 1: Diagnóstico geral de rede
        JTextPane diagnosisTextPane = new JTextPane();
        diagnosisTextPane.setContentType("text/html");
        diagnosisTextPane.setEditable(false);
        diagnosisTextPane.setText(getNetworkDiagnosis());
        JScrollPane diagnosisScrollPane = new JScrollPane(diagnosisTextPane);
        tabbedPane.addTab("Diagnóstico de Rede", diagnosisScrollPane);

        // Aba 2: Interfaces filtradas
        JTextPane filteredTextPane = new JTextPane();
        filteredTextPane.setContentType("text/html");
        filteredTextPane.setEditable(false);
        filteredTextPane.setText(getFilteredNetworkInterfacesInfo());
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextPane);
        tabbedPane.addTab("Interfaces Filtradas", filteredScrollPane);

        // Aba 3: Todas as interfaces principais e componentes adicionais
        JTextPane allTextPane = new JTextPane();
        allTextPane.setContentType("text/html");
        allTextPane.setEditable(false);
        allTextPane.setText(getAllNetworkInterfacesInfo());
        JScrollPane allScrollPane = new JScrollPane(allTextPane);
        tabbedPane.addTab("Todas as Interfaces", allScrollPane);

        // Banner de rodapé
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(139, 0, 0)); // Vermelho escuro
        footerPanel.setPreferredSize(new Dimension(frame.getWidth(), 5));

        // Adicionar o banner e as abas ao painel principal
        mainPanel.add(bannerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Obtém o diagnóstico geral de rede.
     */
    private static String getNetworkDiagnosis() {
        StringBuilder diagnosis = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            boolean hasPrivateIP = false;
            String gateway = null;
            String dnsInternet = null;
            String dnsIntranet = null;
            String dns = null;


            // Verificar se há um IP privado em uma interface ativa
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
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
                            gateway = getDefaultGateway();
                            diagnosis.append("<b>IP Privado:</b> ").append(ip).append(" / ").append(netmask).append("<br>");
                        } else if (ip.startsWith("169")) {
                            diagnosis.append("<b>IP:</b> ").append(ip).append(" <span style='color:red;'>Não recebeu IP</span><br>");
                        }
                    }
                }
                if (hasPrivateIP) {
                    break;
                }
            }

            // Verificar DNS configurados através de resolução
            dnsIntranet = getResolvedDNS(urls.getUrlSiteDeIntranet());
            dnsInternet = getResolvedDNS(urls.getUrlSiteInternet());
            dns = dnsInternet != null ? dnsInternet : (dnsIntranet != null ? dnsIntranet : "NÃO");
            diagnosis.append("<b>Consegue resolver DNS:</b> ").append(dns).append("<br>");


            // Colocar as informações por último
            diagnosis.append("<b>Possui IP privado em uma interface ativa:</b> ")
                    .append(hasPrivateIP ? "<span style='color:green;'>SIM</span>" : "<span style='color:red;'>NÃO</span>")
                    .append("<br>");

            // Verificar conexão com o gateway
            if (gateway != null) {
                diagnosis.append("<b>Gateway configurado:</b> ").append(gateway).append("<br>");
                diagnosis.append("<b>Consegue pingar o gateway:</b> ")
                        .append(ping(gateway) ? "<span style='color:green;'>SIM</span>" : "<span style='color:red;'>NÃO</span>")
                        .append("<br>");
            } else {
                diagnosis.append("<b>Gateway configurado:</b> <span style='color:red;'>NÃO ENCONTRADO</span><br>");
            }


            // Verificar endereços específicos
            String[] addresses = {
                    urls.getUrlSiteDeIntranet(),
                    urls.getUrlSistemaDeDocumentos(),
                    urls.getUrlSistemaCloud(),
                    urls.getUrlSistemaRemoto(),
                    urls.getUrlSistemaRemotoDePessoal(),
                    urls.getUrlSiteInternet(),
                    urls.getUrlSiteInternetDeTerceito()
            };

            for (String address : addresses) {
                diagnosis.append("<b>Consegue pingar ").append(address).append(":</b> ")
                        .append(ping(address) ? "<span style='color:green;'>SIM</span>" : "<span style='color:red;'>NÃO</span>")
                        .append("<br>");
            }

        } catch (Exception e) {
            diagnosis.append("Erro ao obter informações: ").append(e.getMessage());
        }
        diagnosis.append("</body></html>");
        return diagnosis.toString();
    }

    /**
     * Verifica se o IP é privado.
     */
    private static boolean isPrivateIP(String ip) {
        return ip.startsWith("10.") || ip.startsWith("172.") || ip.startsWith("192.168.");
    }

    /**
     * Realiza um ping para o endereço fornecido.
     */
    private static boolean ping(String address) {
        try {
            Process process = Runtime.getRuntime().exec("ping -n 1 " + address);
            int returnCode = process.waitFor();
            return returnCode == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtém o gateway padrão pelo comando tracert.
     */
    private static String getDefaultGateway() {

        try {
            Process process = Runtime.getRuntime().exec("tracert -d " + urls.getUrlSiteInternet());
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("1")) {
                    String[] parts = line.split("\\s+");
                    return parts[parts.length - 1]; // Último elemento é o IP do primeiro salto
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Resolve o DNS de um endereço.
     */
    private static String getResolvedDNS(String hostname) {
        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
            if (inetAddresses.length > 0) {
                return inetAddresses[0].getHostAddress(); // Retorna o primeiro IP resolvido
            }
        } catch (UnknownHostException e) {
            return null;
        }
        return null;
    }

    /**
     * Obtém informações apenas das interfaces principais (Wi-Fi, Ethernet, VPN ativas, Bluetooth).
     */
    private static String getFilteredNetworkInterfacesInfo() {
        StringBuilder info = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                    InetAddress inetAddress = address.getAddress();
                    if (inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        String netmask = address.getNetworkPrefixLength() + "";
                        info.append("<b>Interface:</b> ").append(networkInterface.getDisplayName()).append("<br>");
                        info.append("<b>IP:</b> ").append(ip).append(" / ").append(netmask).append("<br>");
                    }
                }
                info.append("<hr>");
            }
        } catch (Exception e) {
            info.append("Erro ao obter informações: ").append(e.getMessage());
        }
        info.append("</body></html>");
        return info.toString();
    }

    /**
     * Obtém informações de todas as interfaces principais, incluindo subinterfaces e drivers adicionais.
     */
    private static String getAllNetworkInterfacesInfo() {
        StringBuilder info = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                info.append("<b>Interface:</b> ").append(networkInterface.getDisplayName()).append("<br>");
                info.append("<b>Nome:</b> ").append(networkInterface.getName()).append("<br>");
                info.append("<b>Endereços:</b> ").append(networkInterface.getInterfaceAddresses().toString()).append("<br>");
                info.append("<b>Ativa:</b> ").append(networkInterface.isUp() ? "SIM" : "NÃO").append("<br>");
                info.append("<b>Subinterfaces:</b> ").append(networkInterface.getSubInterfaces()).append("<br>");
                info.append("<hr>");
            }
        } catch (Exception e) {
            info.append("Erro ao obter informações: ").append(e.getMessage());
        }
        info.append("</body></html>");
        return info.toString();
    }
}
