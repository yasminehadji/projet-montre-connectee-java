package montre.modele;

import java.awt.Color;
import java.io.Serializable;

/**
 * Classe abstraite représentant le type d'affichage de l'heure.
 * Sous-classes concrètes : AffichageAnalogique, AffichageNumerique.
 */
public abstract class AffichageHeure extends ElementMontre {

    private static final long serialVersionUID = 1L;

    /** Couleur principale de l'affichage */
    private Color couleur;

    public AffichageHeure(Color couleur) {
        super();
        this.couleur = couleur;
    }

    public Color getCouleur() { return couleur; }
    public void setCouleur(Color couleur) { this.couleur = couleur; }
}