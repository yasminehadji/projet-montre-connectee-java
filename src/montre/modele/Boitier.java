package montre.modele;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;

/**
 * Représente la forme du boîtier de la montre.
 */
public class Boitier implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Formes disponibles */
    public enum FormeBoitier { ROND, CARRE, ARRONDI }

    private FormeBoitier forme;
    private Color couleurBord;
    private float epaisseurBord;

    public Boitier() {
        this.forme = FormeBoitier.ROND; //par defaut c  rond 
        this.couleurBord = new Color(150, 150, 170);
        this.epaisseurBord = 4f;
    }

    /**
     * Retourne la Shape correspondant au boîtier, centré en (cx,cy) de rayon r.
     */
    public Shape getShape(int cx, int cy, int r) {
        switch (forme) {
            case CARRE:
                return new Rectangle2D.Double(cx - r, cy - r, r * 2, r * 2);
            case ARRONDI:
                return new RoundRectangle2D.Double(cx - r, cy - r, r * 2, r * 2, r * 0.3, r * 0.3);
            default: // ROND
                return new Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2);
        }
    }

    /**
     * Dessine le contour du boîtier.
     */
    public void dessinerContour(Graphics2D g2d, int cx, int cy, int r) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(couleurBord);
        g2d.setStroke(new BasicStroke(epaisseurBord));
        g2d.draw(getShape(cx, cy, r));

        // Léger reflet
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.setStroke(new BasicStroke(epaisseurBord / 2f));
        g2d.draw(getShape(cx, cy, r - (int)(epaisseurBord)));
    }

    public FormeBoitier getForme() { return forme; }
    public void setForme(FormeBoitier forme) { this.forme = forme; }

    public Color getCouleurBord() { return couleurBord; }
    public void setCouleurBord(Color c) { this.couleurBord = c; }

    public float getEpaisseurBord() { return epaisseurBord; }
    public void setEpaisseurBord(float e) { this.epaisseurBord = e; }
}