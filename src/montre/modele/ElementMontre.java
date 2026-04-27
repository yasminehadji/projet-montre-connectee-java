package montre.modele;

import java.awt.Graphics2D;
import java.io.Serializable;

/**
 * Classe abstraite représentant un élément affichable sur la montre.
 * Principe OCP : on peut ajouter de nouveaux éléments sans modifier le code existant.

 */
public abstract class ElementMontre implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Position X relative au centre du cadran (en pourcentage de rayon) */
    private double posX;

    /** Position Y relative au centre du cadran (en pourcentage de rayon) */
    private double posY;

    /** Indique si l'élément est visible */
    private boolean visible;

    /**
     * Constructeur par défaut : positionné au centre, visible.
     */
    public ElementMontre() {
        this.posX = 0.0;
        this.posY = 0.0;
        this.visible = true;
    }

    /**
     * Constructeur avec position.
     *  posX position X relative
     *  posY position Y relative
     */
    public ElementMontre(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.visible = true;
    }

    /**
     * Dessine cet élément sur le contexte graphique fourni.
     *  g2d le contexte graphique 2D
     *  cx  centre X du cadran en pixels
     *  cy  centre Y du cadran en pixels
     *  rayon rayon du cadran en pixels
     *  heure heure courante (0-23)
     *  minute minute courante (0-59)
     *  seconde seconde courante (0-59)
     */
    public abstract void dessiner(Graphics2D g2d, int cx, int cy, int rayon,
                                   int heure, int minute, int seconde);

    /**
     * retourne le nom de cet élément (pour affichage dans l'UI)
     */
    public abstract String getNom();

    // Accesseurs 

    public double getPosX() { return posX; }
    public void setPosX(double posX) { this.posX = posX; }

    public double getPosY() { return posY; }
    public void setPosY(double posY) { this.posY = posY; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}