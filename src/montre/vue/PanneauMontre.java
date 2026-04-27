package montre.vue;

import montre.modele.Montre;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panneau Swing qui dessine la montre.
 * Redéfinit paintComponent() pour garantir la persistance du dessin.
 */
public class PanneauMontre extends JPanel {

    private Montre montre;
    private int rayon;

    public PanneauMontre(Montre montre, int rayon) {
        this.montre = montre;
        this.rayon = rayon;
        setBackground(new Color(18, 18, 28));
        setPreferredSize(new Dimension(rayon * 2 + 60, rayon * 2 + 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Antialiasing global
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Ombre portée
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillOval(cx - rayon + 8, cy - rayon + 8, rayon * 2, rayon * 2);

        // Dessin de la montre
        montre.dessiner(g2d, cx, cy, rayon);

        g2d.dispose();
    }

    public void setMontre(Montre m) {
        this.montre = m;
        repaint();
    }

    public Montre getMontre() { return montre; }

    public void setRayon(int r) {
        this.rayon = r;
        setPreferredSize(new Dimension(r * 2 + 60, r * 2 + 60));
        revalidate();
        repaint();
    }
}