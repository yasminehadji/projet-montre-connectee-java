package montre.modele;

import java.awt.*;
import java.awt.geom.*;

/*
  Affichage analogique : aiguilles heures, minutes (et  secondes qu'on peut activer ou désactiver ).
 */
public class AffichageAnalogique extends AffichageHeure {

    private static final long serialVersionUID = 1L;

    /** Épaisseur de l'aiguille des heures */
    private float epaisseurHeures;

    /** Épaisseur de l'aiguille des minutes */
    private float epaisseurMinutes;

    /** Couleur de l'aiguille des heures */
    private Color couleurHeures;

    /** Couleur de l'aiguille des minutes */
    private Color couleurMinutes;

    /** Afficher les chiffres sur le cadran */
    private boolean afficherChiffres;

    public AffichageAnalogique() {
        super(Color.WHITE);
        this.epaisseurHeures = 6f;
        this.epaisseurMinutes = 4f;
        this.couleurHeures = Color.WHITE;
        this.couleurMinutes = Color.WHITE;
        this.afficherChiffres = true;
    }

    @Override
    public void dessiner(Graphics2D g2d, int cx, int cy, int rayon,
                         int heure, int minute, int seconde) {
        if (!isVisible()) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Graduation / chiffres
        if (afficherChiffres) {
            dessinerGraduations(g2d, cx, cy, rayon);
        }

        // Aiguille des heures
        double angleH = Math.toRadians((heure % 12) * 30 + minute * 0.5 - 90); //30=360/12 (pour definir la place de cahque heure ) et 0.5=360/12/60 (pour definir les place des minute)
        dessinerAiguille(g2d, cx, cy, (int)(rayon * 0.5), angleH, couleurHeures, epaisseurHeures);

        // Aiguille des minutes
        double angleM = Math.toRadians(minute * 6 + seconde * 0.1 - 90);//0.1=360/60/60 (pour les place des seconde)  et le -90 cest pour que lheure demare de 12 h parceque snn ca demare de 3h vu que cest le 0 degret de base 
        dessinerAiguille(g2d, cx, cy, (int)(rayon * 0.75), angleM, couleurMinutes, epaisseurMinutes);

        // Centre
        g2d.setColor(getCouleur());
        g2d.fillOval(cx - 6, cy - 6, 12, 12);
    }

    /** Dessine une aiguille depuis le centre */
    private void dessinerAiguille(Graphics2D g2d, int cx, int cy, int longueur,
                                   double angle, Color couleur, float epaisseur) {
        int x2 = (int)(cx + longueur * Math.cos(angle));
        int y2 = (int)(cy + longueur * Math.sin(angle));
        g2d.setColor(couleur);
        g2d.setStroke(new BasicStroke(epaisseur, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(cx, cy, x2, y2);
    }

    /** Dessine les chiffres 1-12 et les graduations */
    private void dessinerGraduations(Graphics2D g2d, int cx, int cy, int rayon) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, rayon / 8));
        FontMetrics fm = g2d.getFontMetrics();

        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(i * 30 - 90);
            int textR = (int)(rayon * 0.82);
            int tx = (int)(cx + textR * Math.cos(angle));
            int ty = (int)(cy + textR * Math.sin(angle));
            String s = String.valueOf(i);
            int sw = fm.stringWidth(s);
            int sh = fm.getAscent();
            g2d.setColor(new Color(200, 200, 200, 200));
            g2d.drawString(s, tx - sw / 2, ty + sh / 2 - 2);
        }

        // Petites graduations
        for (int i = 0; i < 60; i++) {
            double angle = Math.toRadians(i * 6);
            float len = (i % 5 == 0) ? 0.1f : 0.05f;
            float epaisseur = (i % 5 == 0) ? 2f : 1f;
            int x1 = (int)(cx + (rayon * (1 - len * 1.1)) * Math.cos(angle));
            int y1 = (int)(cy + (rayon * (1 - len * 1.1)) * Math.sin(angle));
            int x2 = (int)(cx + rayon * 0.93 * Math.cos(angle));
            int y2 = (int)(cy + rayon * 0.93 * Math.sin(angle));
            g2d.setColor(new Color(180, 180, 180, 150));
            g2d.setStroke(new BasicStroke(epaisseur));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    public String getNom() { return "Analogique"; }

    //  Accesseurs 
    public float getEpaisseurHeures() { return epaisseurHeures; }
    public void setEpaisseurHeures(float e) { this.epaisseurHeures = e; }

    public float getEpaisseurMinutes() { return epaisseurMinutes; }
    public void setEpaisseurMinutes(float e) { this.epaisseurMinutes = e; }

    public Color getCouleurHeures() { return couleurHeures; }
    public void setCouleurHeures(Color c) { this.couleurHeures = c; }

    public Color getCouleurMinutes() { return couleurMinutes; }
    public void setCouleurMinutes(Color c) { this.couleurMinutes = c; }

    public boolean isAfficherChiffres() { return afficherChiffres; }
    public void setAfficherChiffres(boolean b) { this.afficherChiffres = b; }
}