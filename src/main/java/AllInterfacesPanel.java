import javax.swing.*;
import java.net.*;
import java.util.Enumeration;

class AllInterfacesPanel extends JTextPane {
    public AllInterfacesPanel() {
        setContentType("text/html");
        setEditable(false);
        setText(getAllNetworkInterfacesInfo());
    }

    private String getAllNetworkInterfacesInfo() {
        StringBuilder info = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                info.append("<b>Interface:</b> ").append(networkInterface.getDisplayName()).append("<br>");
                info.append("<b>Nome:</b> ").append(networkInterface.getName()).append("<br>");
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