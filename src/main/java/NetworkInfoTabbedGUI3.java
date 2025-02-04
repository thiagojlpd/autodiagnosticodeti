import javax.swing.*;
import java.awt.*;

public class NetworkInfoTabbedGUI3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkInfoTabbedGUI3::createAndShowGUI);  // Alterado de NetworkInfoTabbedGUI para NetworkInfoTabbedGUI3
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Autodiagnóstico de TI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        // Define fonte personalizada para títulos das abas
        UIManager.put("TabbedPane.font", new Font("Arial", Font.BOLD, 16));

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel bannerPanel = new BannerPanel();
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Diagnóstico de Rede", new JScrollPane(new NetworkDiagnosisPanel()));
        tabbedPane.addTab("Interfaces Filtradas", new JScrollPane(new FilteredInterfacesPanel()));
        tabbedPane.addTab("Todas as Interfaces", new JScrollPane(new AllInterfacesPanel()));
        tabbedPane.addTab("Impressoras", new JScrollPane(new PrintersPanel()));
        tabbedPane.addTab("USB, DisplayPort e VGS", new JScrollPane(new InterfacesStatusPanel()));
        tabbedPane.addTab("Memória", new JScrollPane(new MemoryUsagePanel()));


        JPanel footerPanel = new FooterPanel();

        mainPanel.add(bannerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
}
