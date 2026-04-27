package montre;

import montre.vue.FenetreMontre;

import javax.swing.*;

/**
 * Point d'entrée de l'application Montre.
 * Lance l'interface dans l'Event Dispatch Thread (bonne pratique Swing).
 */
public class Main {
    public static void main(String[] args) {
        // Lancer l'interface dans l'EDT (cours Événements et Threads)
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new FenetreMontre();
        });
    }
}