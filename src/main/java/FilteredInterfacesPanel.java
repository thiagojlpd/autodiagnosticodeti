import javax.swing.*;
import java.net.*;
import java.util.Enumeration;

class FilteredInterfacesPanel extends JTextPane {
    public FilteredInterfacesPanel() {
        setContentType("text/html");
        setEditable(false);
        setText(getFilteredNetworkInterfacesInfo());
    }

    private String getFilteredNetworkInterfacesInfo() {
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
                        info.append("<hr>");
                    }
                }

            }
        } catch (Exception e) {
            info.append("Erro ao obter informações: ").append(e.getMessage());
        }
        info.append("</body></html>");
        return info.toString();
    }
}