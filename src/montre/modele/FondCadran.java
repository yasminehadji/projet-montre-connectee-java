package montre.modele;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 * Représente le fond du cadran de la montre.
 * Peut être uniforme, dégradé ou une image.
 */
public class FondCadran implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TypeFond { UNIFORME, DEGRADE, IMAGE }

    private TypeFond type;
    private Color couleur1;
    private Color couleur2;

    /** Chemin vers l'image choisie */
    private String cheminImage;

    /** Image en mémoire (transient car BufferedImage n'est pas sérialisable) */
    private transient BufferedImage image;

    public FondCadran() {
        this.type = TypeFond.DEGRADE;
        this.couleur1 = new Color(15, 20, 40);
        this.couleur2 = new Color(30, 50, 80);
        this.cheminImage = null;
        this.image = null;
    }

    public void dessiner(Graphics2D g2d, Shape boitier) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle bounds = boitier.getBounds();
        int cx = bounds.x + bounds.width / 2;
        int cy = bounds.y + bounds.height / 2;
        int r = bounds.width / 2;

        switch (type) {
            case UNIFORME:
                g2d.setColor(couleur1);
                g2d.fill(boitier);
                break;

            case DEGRADE:
                RadialGradientPaint gradient = new RadialGradientPaint(
                        cx, cy, r,
                        new float[]{0f, 1f},
                        new Color[]{couleur2, couleur1}
                );
                g2d.setPaint(gradient);
                g2d.fill(boitier);
                break;

            case IMAGE:
                if (image == null && cheminImage != null) {
                    chargerImage(cheminImage);
                }
                if (image != null) {
                    g2d.fill(boitier);
                    g2d.drawImage(image,
                            bounds.x, bounds.y,
                            bounds.width, bounds.height, null);
                } else {
                    g2d.setColor(couleur1);
                    g2d.fill(boitier);
                }
                break;
        }
    }

    public void chargerImage(String chemin) {
        try {
            this.image = ImageIO.read(new File(chemin));
            this.cheminImage = chemin;
            this.type = TypeFond.IMAGE;
        } catch (IOException e) {
            this.image = null;
            this.cheminImage = null;
        }
    }

    public TypeFond getType() { return type; }
    public void setType(TypeFond type) { this.type = type; }

    public Color getCouleur1() { return couleur1; }
    public void setCouleur1(Color c) { this.couleur1 = c; }

    public Color getCouleur2() { return couleur2; }
    public void setCouleur2(Color c) { this.couleur2 = c; }

    public String getCheminImage() { return cheminImage; }
}