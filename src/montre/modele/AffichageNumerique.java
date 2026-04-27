package montre.modele;

import java.awt.*;

/**
  Affichage numérique de l'heure.
 */
public class AffichageNumerique extends AffichageHeure {

    private static final long serialVersionUID = 1L;

    /** Taille de la police (relative au rayon) */
    private float taillePoliceFactor;

    /** Nom de la police */
    private String nomPolice;

    /** Style de la police (Font.BOLD, etc.) */
    private int stylePolice;

    public AffichageNumerique() {
        super(Color.WHITE);
        this.taillePoliceFactor = 0.3f;
        this.nomPolice = "Courier New";
        this.stylePolice = Font.BOLD;
    }

    @Override
    public void dessiner(Graphics2D g2d, int cx, int cy, int rayon,
                         int heure, int minute, int seconde) {
        if (!isVisible()) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int taille = (int)(rayon * taillePoliceFactor);
        g2d.setFont(new Font(nomPolice, stylePolice, taille));
        g2d.setColor(getCouleur());

        String texte = String.format("%02d:%02d", heure, minute);
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(texte);
        int h = fm.getAscent();

        int posX = (int)(cx + getPosX() * rayon) - w / 2;
        int posY = (int)(cy + getPosY() * rayon) + h / 2;

        g2d.drawString(texte, posX, posY);
    }

    @Override
    public String getNom() { return "Numérique"; }

    public float getTaillePoliceFactor() { return taillePoliceFactor; }
    public void setTaillePoliceFactor(float f) { this.taillePoliceFactor = f; }

    public String getNomPolice() { return nomPolice; }
    public void setNomPolice(String n) { this.nomPolice = n; }

    public int getStylePolice() { return stylePolice; }
    public void setStylePolice(int s) { this.stylePolice = s; }
}