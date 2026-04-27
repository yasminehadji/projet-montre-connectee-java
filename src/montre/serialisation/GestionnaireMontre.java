package montre.serialisation;

import montre.modele.Montre;

import java.io.*;

/**
 * Gestionnaire de sauvegarde/chargement d'une montre via la sérialisation Java.
 * 
 *
 */
public class GestionnaireMontre {

    private GestionnaireMontre() {}

    /**
     * Sauvegarde une montre dans un fichier.
     *  montre la montre à sauvegarder
     *  fichier le fichier de destination
     
     */
    public static void sauvegarder(Montre montre, File fichier) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(montre);
        }
    }

    /**
     * Charge une montre depuis un fichier.
     *  fichier le fichier source
     *  la montre chargée
     * @throws IOException en cas d'erreur de lecture
     * @throws ClassNotFoundException si la classe n'est pas reconnue
     */
    public static Montre charger(File fichier) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
            return (Montre) ois.readObject();
        }
    }
}