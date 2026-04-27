package montre.modele;

import java.awt.*;

/**
 * Élément optionnel : rythme cardiaque (simulé).
 */
public class ElementRythmeCardiaque extends ElementMontre {

    private static final long serialVersionUID = 1L;

    private int bpm;
    private Color couleur;

    public ElementRythmeCardiaque() {
        super(-0.4, 0.0);
        this.bpm = 72;
        this.couleur = new Color(255, 80, 100);
    }

    @Override
    public void dessiner(Graphics2D g2d, int cx, int cy, int rayon,
                         int heure, int minute, int seconde) {
        if (!isVisible()) return;

        int bpmAffiche = bpm + (seconde % 5 < 2 ? 2 : 0);

        // En mode numérique on descend en bas à gauche
        // En mode analogique on garde la position d'origine (-0.4, 0.0)
        int px, py;
        if (Montre.modeNumerique) {
            px = (int)(cx - 0.3 * rayon);
            py = (int)(cy + 0.65 * rayon);
        } else {
            px = (int)(cx + getPosX() * rayon);
            py = (int)(cy + getPosY() * rayon);
        }

        int s = rayon / 10;
        dessinerCoeur(g2d, px, py - s / 2, s);

        int taille = rayon / 9;
        g2d.setFont(new Font("Courier New", Font.BOLD, taille));
        g2d.setColor(couleur);
        String txt = bpmAffiche + " bpm";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(txt, px - fm.stringWidth(txt) / 2, py + taille);
    }

    private void dessinerCoeur(Graphics2D g2d, int cx, int cy, int s) {
        int[] xp = {cx, cx - s, cx - s, cx - s/2, cx, cx + s/2, cx + s, cx + s};
        int[] yp = {cy + s, cy, cy - s/2, cy - s, cy - s/2, cy - s, cy - s/2, cy};
        g2d.setColor(couleur);
        g2d.fillPolygon(xp, yp, xp.length);
    }

    @Override
    public String getNom() { return "Rythme cardiaque"; }

    public int getBpm() { return bpm; }
    public void setBpm(int bpm) { this.bpm = bpm; }

    public Color getCouleur() { return couleur; }
    public void setCouleur(Color c) { this.couleur = c; }
}