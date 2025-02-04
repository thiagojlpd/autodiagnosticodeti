import javax.print.PrintService;
import javax.print.PrintServiceLookup;

class PrinterInfo {

    public static String getInstalledPrinters() {
        StringBuilder printersInfo = new StringBuilder("<html><body style='font-size:14px; font-family: Arial;'>");
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length == 0) {
                printersInfo.append("Nenhuma impressora encontrada.");
            } else {
                for (PrintService printService : printServices) {
                    printersInfo.append("<b>Impressora:</b> ").append(printService.getName()).append("<br>");
                }
            }
        } catch (Exception e) {
            printersInfo.append("Erro ao obter informações: ").append(e.getMessage());
        }
        printersInfo.append("</body></html>");
        return printersInfo.toString();
    }
}