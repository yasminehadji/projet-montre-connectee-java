package montre.modele;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principale modélisant une montre connectée.
 * Agrège tous les éléments : boîtier, fond, affichage principal et éléments optionnels.
 *
 * Extensibilité : pour ajouter un nouvel élément, il suffit de créer
 * une sous-classe d'ElementMontre et de l'ajouter via ajouterElement().
 
 */
public class Montre implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nom de la montre */
    private String nom;

    /** Boîtier (forme et bord) */
    private Boitier boitier;

    /** Fond du cadran */
    private FondCadran fond;

    /**
     * Affichage principal de l'heure (analogique ou numérique).
     * Utilise le polymorphisme : AffichageHeure est abstraite.
     */
    private AffichageHeure affichageHeure;

    /**
     * Liste des éléments optionnels (OCP : on peut en ajouter sans modifier ce code).
     */
    private List<ElementMontre> elements;

    /** Heure simulée (si null, utilise l'heure système) */
    private Integer heureSimulee;
    private Integer minuteSimulee;
    private Integer secondeSimulee;

    /** Moment où la simulation a été lancée */
    private transient long tempsDepart;

    /** Flag indiquant si l'affichage est numérique, lu par les éléments optionnels */
    public static boolean modeNumerique = false;

    public Montre(String nom) {
        this.nom = nom;
        this.boitier = new Boitier();
        this.fond = new FondCadran();
        this.affichageHeure = new AffichageAnalogique();
        this.elements = new ArrayList<>();
        this.heureSimulee = null;
        this.minuteSimulee = null;
        this.secondeSimulee = null;
    }

    /**
     * Dessine la montre complète sur le Graphics2D fourni.
     * g2d le contexte graphique
     *  cx centre X en pixels
     * cy centre Y en pixels
     *  rayon rayon du cadran
     */
    public void dessiner(Graphics2D g2d, int cx, int cy, int rayon) {
        int h, m, s;
        if (heureSimulee != null) {
            // Calcule le temps écoulé depuis le lancement de la simulation
            long ecoule = (System.currentTimeMillis() - tempsDepart) / 1000;
            int totalSecondes = heureSimulee * 3600 + minuteSimulee * 60 + secondeSimulee + (int) ecoule;
            h = (totalSecondes / 3600) % 24;
            m = (totalSecondes % 3600) / 60;
            s = totalSecondes % 60;
        } else {
            java.time.LocalTime now = java.time.LocalTime.now();
            h = now.getHour(); m = now.getMinute(); s = now.getSecond();
        }

        // Met à jour le flag avant de dessiner les éléments
        Montre.modeNumerique = (affichageHeure instanceof AffichageNumerique);

        Shape shape = boitier.getShape(cx, cy, rayon);
        Shape oldClip = g2d.getClip();
        g2d.setClip(shape);

        // 1. Fond
        fond.dessiner(g2d, shape);

        // 2. Éléments optionnels EN PREMIER (date, batterie, bpm...)
        for (ElementMontre el : elements) {
            el.dessiner(g2d, cx, cy, rayon, h, m, s);
        }

        // 3. Aiguilles PAR DESSUS
        affichageHeure.dessiner(g2d, cx, cy, rayon, h, m, s);

        g2d.setClip(oldClip);

        // 4. Contour du boîtier
        boitier.dessinerContour(g2d, cx, cy, rayon);
    }

    /** Ajoute un élément optionnel à la montre. */
    public void ajouterElement(ElementMontre el) {
        elements.add(el);
    }

    /** Retire un élément optionnel. */
    public void retirerElement(ElementMontre el) {
        elements.remove(el);
    }

    /** Retire tous les éléments du type donné. */
    public void retirerElementsDuType(Class<?> type) {
        elements.removeIf(e -> type.isInstance(e));
    }

    /** Retourne les éléments optionnels (lecture seule). */
    public List<ElementMontre> getElements() {
        return new ArrayList<>(elements);
    }

    //  Simulation 
    public void setHeureSimulee(int h, int m, int s) {
        this.heureSimulee = h;
        this.minuteSimulee = m;
        this.secondeSimulee = s;
        // On note l'heure système au moment où on lance la simulation
        this.tempsDepart = System.currentTimeMillis();
    }

    public void utiliserHeureSystème() {
        this.heureSimulee = null;
        this.minuteSimulee = null;
        this.secondeSimulee = null;
    }

    public boolean isEnSimulation() { return heureSimulee != null; }
    public int getHeureSimulee() { return heureSimulee != null ? heureSimulee : 0; }
    public int getMinuteSimulee() { return minuteSimulee != null ? minuteSimulee : 0; }
    public int getSecondeSimulee() { return secondeSimulee != null ? secondeSimulee : 0; }

    //  Accesseurs 
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Boitier getBoitier() { return boitier; }
    public void setBoitier(Boitier b) { this.boitier = b; }

    public FondCadran getFond() { return fond; }
    public void setFond(FondCadran f) { this.fond = f; }

    public AffichageHeure getAffichageHeure() { return affichageHeure; }
    public void setAffichageHeure(AffichageHeure a) { this.affichageHeure = a; }

    
    /** Appelée après désérialisation pour réinitialiser tempsDepart */
    private Object readResolve() {
        if (heureSimulee != null) {
            this.tempsDepart = System.currentTimeMillis();
        }
        return this;
    }

    @Override
    public String toString() { return nom; }
}
