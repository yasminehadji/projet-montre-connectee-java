package montre.modele;

import java.awt.*;
import java.awt.geom.*;

/**
 * Élément optionnel : affichage des secondes.
 * Peut être une trotteuse sur le cadran principal, un mini-cadran, ou un compteur numérique.
 */
public class ElementSecondes extends ElementMontre {

    private static final long serialVersionUID = 1L;

    /** Type d'affichage des secondes */
    public enum TypeSecondes { TROTTEUSE, MINI_CADRAN, NUMERIQUE }

    private TypeSecondes type;
    private Color couleur;

    public ElementSecondes() {
        super(0.0, 0.35);
        this.type = TypeSecondes.MINI_CADRAN;
        this.couleur = new Color(220, 80, 80);
    }

    @Override
    public void dessiner(Graphics2D g2d, int cx, int cy, int rayon,
                         int heure, int minute, int seconde) {
        if (!isVisible()) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (type) {
            case TROTTEUSE:
                dessinerTrotteuse(g2d, cx, cy, rayon, seconde);
                break;
            case MINI_CADRAN:
                dessinerMiniCadran(g2d, cx, cy, rayon, seconde);
                break;
            case NUMERIQUE:
                dessinerNumerique(g2d, cx, cy, rayon, seconde);
                break;
        }
    }

    private void dessinerTrotteuse(Graphics2D g2d, int cx, int cy, int rayon, int seconde) {
        double angle = Math.toRadians(seconde * 6 - 90);
        int longueur = (int)(rayon * 0.85);
        int x2 = (int)(cx + longueur * Math.cos(angle));
        int y2 = (int)(cy + longueur * Math.sin(angle));
        g2d.setColor(couleur);
        g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(cx, cy, x2, y2);
        // Queue
        int xq = (int)(cx - (longueur * 0.2) * Math.cos(angle));
        int yq = (int)(cy - (longueur * 0.2) * Math.sin(angle));
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawLine(cx, cy, xq, yq);
    }

    private void dessinerMiniCadran(Graphics2D g2d, int cx, int cy, int rayon, int seconde) {
        int miniCx = (int)(cx + getPosX() * rayon);
        int miniCy = (int)(cy + getPosY() * rayon);
        int miniR = rayon / 5;

        // Fond mini-cadran
        g2d.setColor(new Color(30, 30, 40, 180));
        g2d.fillOval(miniCx - miniR, miniCy - miniR, miniR * 2, miniR * 2);
        g2d.setColor(couleur.darker());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(miniCx - miniR, miniCy - miniR, miniR * 2, miniR * 2);

        // Aiguille des secondes
        double angle = Math.toRadians(seconde * 6 - 90);
        int x2 = (int)(miniCx + (miniR - 3) * Math.cos(angle));
        int y2 = (int)(miniCy + (miniR - 3) * Math.sin(angle));
        g2d.setColor(couleur);
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(miniCx, miniCy, x2, y2);

        // Centre
        g2d.fillOval(miniCx - 3, miniCy - 3, 6, 6);
    }

    private void dessinerNumerique(Graphics2D g2d, int cx, int cy, int rayon, int seconde) {
        int posX = (int)(cx + getPosX() * rayon);
        int posY = (int)(cy + getPosY() * rayon);
        int taille = rayon / 7;
        g2d.setFont(new Font("Courier New", Font.BOLD, taille));
        g2d.setColor(couleur);
        String s = String.format(":%02d", seconde);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(s, posX - fm.stringWidth(s) / 2, posY + fm.getAscent() / 2);
    }

    @Override
    public String getNom() { return "Secondes (" + type + ")"; }

    public TypeSecondes getType() { return type; }
    public void setType(TypeSecondes type) { this.type = type; }

    public Color getCouleur() { return couleur; }
    public void setCouleur(Color c) { this.couleur = c; }
}