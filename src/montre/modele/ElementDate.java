package montre.modele;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Élément optionnel : affichage de la date.
 */
public class ElementDate extends ElementMontre {

    private static final long serialVersionUID = 1L;

    /** Formats de date disponibles */
    public enum FormatDate {
        JOUR_MOIS("dd/MM"),
        JOUR_MOIS_AN("dd/MM/yy"),
        JOUR_LETTRE("EEE dd"),
        COMPLET("EEE dd MMM");

        private final String pattern;
        FormatDate(String p) { this.pattern = p; }
        public String getPattern() { return pattern; }
        @Override public String toString() { return pattern; }
    }

    private FormatDate format;
    private Color couleur;

    public ElementDate() {
        super(0.0, -0.4);
        this.format = FormatDate.JOUR_MOIS;
        this.couleur = new Color(180, 220, 255);
    }

    @Override
    public void dessiner(Graphics2D g2d, int cx, int cy, int rayon,
                         int heure, int minute, int seconde) {
        if (!isVisible()) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format.getPattern(), java.util.Locale.FRENCH);
        String dateStr = now.format(dtf).toUpperCase();

        int taille = rayon / 9;
        g2d.setFont(new Font("Courier New", Font.BOLD, taille));
        g2d.setColor(couleur);

        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(dateStr);
        int px = (int)(cx + getPosX() * rayon) - w / 2;
        int py = (int)(cy + getPosY() * rayon) + fm.getAscent() / 2;

        // Fond léger
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(px - 6, py - fm.getAscent() - 2, w + 12, taille + 8, 8, 8);
        g2d.setColor(couleur);
        g2d.drawString(dateStr, px, py);
    }

    @Override
    public String getNom() { return "Date"; }

    public FormatDate getFormat() { return format; }
    public void setFormat(FormatDate f) { this.format = f; }

    public Color getCouleur() { return couleur; }
    public void setCouleur(Color c) { this.couleur = c; }
}