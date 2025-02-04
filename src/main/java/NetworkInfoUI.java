
import javax.swing.*;
import java.awt.*;


class NetworkInfoUI {

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Autodiagnóstico de TI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createBannerPanel(), BorderLayout.NORTH);
        mainPanel.add(createTabbedPane(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createBannerPanel() {
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setPreferredSize(new Dimension(800, 60));

        JLabel bannerLabel = new JLabel("Autodiagnóstico de TI", JLabel.CENTER);
        bannerLabel.setOpaque(true);
        bannerLabel.setBackground(new Color(0, 0, 128));
        bannerLabel.setForeground(Color.WHITE);
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel redLine = new JPanel();
        redLine.setBackground(new Color(139, 0, 0));
        redLine.setPreferredSize(new Dimension(800, 5));

        bannerPanel.add(redLine, BorderLayout.NORTH);
        bannerPanel.add(bannerLabel, BorderLayout.CENTER);
        return bannerPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JTextPane diagnosisTextPane = createTextPane(NetworkDiagnostics.getNetworkDiagnosis());
        tabbedPane.addTab("Diagnóstico de Rede", new JScrollPane(diagnosisTextPane));

        JTextPane filteredTextPane = createTextPane(NetworkDiagnostics.getFilteredNetworkInterfacesInfo());
        tabbedPane.addTab("Interfaces Filtradas", new JScrollPane(filteredTextPane));

        JTextPane allTextPane = createTextPane(NetworkDiagnostics.getAllNetworkInterfacesInfo());
        tabbedPane.addTab("Todas as Interfaces", new JScrollPane(allTextPane));

        JTextPane printersTextPane = createTextPane(PrinterInfo.getInstalledPrinters());
        tabbedPane.addTab("Impressoras Instaladas", new JScrollPane(printersTextPane));

        return tabbedPane;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(139, 0, 0));
        footerPanel.setPreferredSize(new Dimension(800, 5));
        return footerPanel;
    }

    private JTextPane createTextPane(String content) {
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setText(content);
        return textPane;
    }
}