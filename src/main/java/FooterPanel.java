
import javax.swing.*;
import java.awt.*;

class FooterPanel extends JPanel {
    public FooterPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 10));

        JPanel redLine = new GradientPanel(new Color(139, 0, 0), Color.PINK);
        redLine.setPreferredSize(new Dimension(800, 5));

        add(redLine, BorderLayout.NORTH);
    }
}