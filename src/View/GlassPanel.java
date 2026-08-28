package View;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class GlassPanel extends JPanel {

    public GlassPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 24;

        // soft drop shadow
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fill(new RoundRectangle2D.Float(4, 6, w - 4, h - 4, arc, arc));

        // frosted glass fill — dark navy, semi-transparent
        g2.setColor(new Color(10, 25, 55, 150));
        g2.fill(new RoundRectangle2D.Float(0, 0, w - 6, h - 6, arc, arc));

        // subtle top highlight for a "glass" sheen
        g2.setPaint(new GradientPaint(
                0, 0, new Color(255, 255, 255, 35),
                0, h * 0.4f, new Color(255, 255, 255, 0)
        ));
        g2.fill(new RoundRectangle2D.Float(0, 0, w - 6, h - 6, arc, arc));

        // gold border matching the background's accent color
        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(new Color(200, 168, 90, 160));
        g2.draw(new RoundRectangle2D.Float(0, 0, w - 7, h - 7, arc, arc));

        g2.dispose();
        super.paintComponent(g);
    }
}