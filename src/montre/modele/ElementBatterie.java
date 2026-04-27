package montre.modele;

import java.awt.*;
import java.nio.file.*;

/**
 * Élément optionnel : état de la batterie.
 * Tente de lire la batterie réelle du PC, sinon affiche 85% par défaut.
 */
public class ElementBatterie extends ElementMontre {

    private static final long serialVersionUID = 1L;

    private int niveau;
    private Color couleur;

    public ElementBatterie() {
        super(0.4, 0.0);
        this.niveau = lireBatteriePC();
        this.couleur = new Color(100, 220, 100);
    }

    private int lireBatteriePC() {
        try {
            Path path = Paths.get("/sys/class/power_supply/BAT0/capacity");
            if (Files.exists(path)) {
                String contenu = new String(Files.readAllBytes(path)).trim();
                return Integer.parseInt(contenu);
            }
        } catch (Exception e) {}

        try {
            Path path = Paths.get("/sys/class/power_supply/BAT1/capacity");
            if (Files.exists(path)) {
                String contenu = new String(Files.readAllBytes(path)).trim();
                return Integer.parseInt(contenu);
            }
        } catch (Exception e) {}

        return 85;
    }

    @Override
    public void dessiner(Graphics2D g2d, int cx, int cy, int rayon,
                         int heure, int minute, int seconde) {
        if (!isVisible()) return;

        if (seconde % 30 == 0) {
            this.niveau = lireBatteriePC();
        }

        // En mode numérique on descend en bas à droite
        // En mode analogique on garde la position d'origine (0.4, 0.0)
        int px, py;
        if (estEnModeNumerique()) {
            px = (int)(cx + 0.3 * rayon);
            py = (int)(cy + 0.65 * rayon);
        } else {
            px = (int)(cx + getPosX() * rayon);
            py = (int)(cy + getPosY() * rayon);
        }

        int w = rayon / 5;
        int h = rayon / 10;
        int bx = px - w / 2;
        int by = py - h / 2;

        // Contour batterie
        g2d.setColor(new Color(180, 180, 180));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(bx, by, w, h, 4, 4);

        // Borne
        g2d.fillRect(bx + w, by + h / 3, 4, h / 3);

        // Remplissage
        Color c = niveau > 20 ? couleur : new Color(220, 60, 60);
        g2d.setColor(c);
        int rempli = (int)((w - 4) * (niveau / 100.0));
        g2d.fillRoundRect(bx + 2, by + 2, rempli, h - 4, 3, 3);

        // Pourcentage
        int taille = rayon / 10;
        g2d.setFont(new Font("SansSerif", Font.PLAIN, taille));
        g2d.setColor(Color.WHITE);
        String txt = niveau + "%";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(txt, px - fm.stringWidth(txt) / 2, by + h + taille + 2);
    }

    private boolean estEnModeNumerique() {
        return Montre.modeNumerique;
    }

    @Override
    public String getNom() { return "Batterie"; }

    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = Math.max(0, Math.min(100, niveau)); }

    public Color getCouleur() { return couleur; }
    public void setCouleur(Color c) { this.couleur = c; }
}