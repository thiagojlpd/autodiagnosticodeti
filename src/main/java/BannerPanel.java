import javax.swing.*;
import java.awt.*;

class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;

    public GradientPanel(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

class BannerPanel extends JPanel {

    public BannerPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 60));

        JPanel redLine = new GradientPanel(new Color(139, 0, 0), Color.PINK);
        redLine.setPreferredSize(new Dimension(800, 6));

        add(redLine, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Gradiente linear de azul escuro para azul claro
        GradientPaint gradient = new GradientPaint(0, 0, Color.BLUE, getWidth(), getHeight(), Color.CYAN);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Desenha o texto com sombra
        String text = "Autodiagnóstico de TI";
        Font font = new Font("Arial", Font.BOLD, 25);
        g2d.setFont(font);

        int x = getWidth() / 2 - g2d.getFontMetrics().stringWidth(text) / 2;
        int y = getHeight() / 2 + g2d.getFontMetrics().getAscent() / 4;

        // Sombra preta com deslocamento suave (2,2)
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x + 1, y + 2);

        // Texto principal em branco
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Teste do Painel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 100);
        frame.add(new BannerPanel());
        frame.setVisible(true);
    }
}
