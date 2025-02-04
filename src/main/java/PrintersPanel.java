import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.standard.PrinterURI;
import javax.swing.*;

class PrintersPanel extends JTextPane {
    public PrintersPanel() {
        setContentType("text/html");
        setEditable(false);
        setText(getInstalledPrintersInfo());
    }

    private String getInstalledPrintersInfo() {
        StringBuilder printersInfo = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length == 0) {
                printersInfo.append("Nenhuma impressora encontrada.");
            } else {
                for (PrintService printer : printServices) {
                    if (defaultPrinter != null && printer.getName().equals(defaultPrinter.getName())) {
                        printersInfo.append("<b>Impressora Padrão:</b> ").append(printer.getName()).append("<br>");
                    } else {
                        printersInfo.append("<b>Impressora:</b> ").append(printer.getName()).append("<br>");
                    }
                    printersInfo.append("<b>Atributos:</b><br>");
                    AttributeSet attributes = printer.getAttributes();
                    for (Attribute attribute : attributes.toArray()) {
                        String attributeName = attribute.getName();
                        String attributeValue = attributes.get(attribute.getClass()).toString();
                        printersInfo.append(attributeName).append(": ").append(attributeValue).append("<br>");
                    }
                    printersInfo.append("<b>Tipo de Conexão:</b> ");
                    String connectionType = determineConnectionType(printer);
                    printersInfo.append(connectionType).append("<br><hr>");
                }
            }
        } catch (Exception e) {
            printersInfo.append("Erro ao obter informações de impressoras: ").append(e.getMessage());
        }
        printersInfo.append("</body></html>");
        return printersInfo.toString();
    }

    private String determineConnectionType(PrintService printer) {
        String connectionType = "Desconhecido";
        AttributeSet attributes = printer.getAttributes();
        if (attributes != null) {
            PrinterURI printerURI = (PrinterURI) attributes.get(PrinterURI.class);
            if (printerURI != null) {
                String uri = printerURI.getURI().toString();
                if (uri.startsWith("usb")) {
                    connectionType = "USB";
                } else if (uri.startsWith("http") || uri.startsWith("ipp")) {
                    connectionType = "Impressora de Rede";
                } else if (uri.startsWith("socket") || uri.startsWith("lpd")) {
                    connectionType = "Impressora Física de Rede";
                }
            } else {
                connectionType = "Possivelmente Software Lógico";
            }
        }
        return connectionType;
    }
}
