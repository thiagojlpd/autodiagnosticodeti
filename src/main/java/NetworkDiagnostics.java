import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;


class NetworkDiagnostics {

    public static String getNetworkDiagnosis() {
        StringBuilder diagnosis = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            boolean hasPrivateIP = false;
            String gateway = getDefaultGateway();
            String dnsInternet = getResolvedDNS("www.google.com");
            diagnosis.append("<b>DNS Resolvido:</b> ").append(dnsInternet != null ? dnsInternet : "NÃO").append("<br>");
            diagnosis.append("<b>Gateway configurado:</b> ").append(gateway != null ? gateway : "NÃO").append("<br>");
        } catch (Exception e) {
            diagnosis.append("Erro ao obter informações: ").append(e.getMessage());
        }
        diagnosis.append("</body></html>");
        return diagnosis.toString();
    }

    public static String getFilteredNetworkInterfacesInfo() {
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
                        info.append("<b>Interface:</b> ").append(networkInterface.getDisplayName()).append("<br>");
                        info.append("<b>IP:</b> ").append(inetAddress.getHostAddress()).append("<br>");
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

    public static String getAllNetworkInterfacesInfo() {
        StringBuilder info = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                info.append("<b>Interface:</b> ").append(networkInterface.getDisplayName()).append("<br>");
                info.append("<hr>");
            }
        } catch (Exception e) {
            info.append("Erro ao obter informações: ").append(e.getMessage());
        }
        info.append("</body></html>");
        return info.toString();
    }

    private static String getDefaultGateway() {
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

    private static String getResolvedDNS(String hostname) {
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
}