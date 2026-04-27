package montre.vue;

import montre.modele.Montre;
import montre.serialisation.GestionnaireMontre;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Fenêtre principale de l'application Montre.
 * Utilise JFrame, JMenuBar, JFileChooser, JOptionPane .
 * Lance l'interface dans l'Event Dispatch Thread .
 */
public class FenetreMontre extends JFrame {

    private PanneauMontre panneauMontre;
    private PanneauConfig panneauConfig;
    private Timer timerActualisation;

    public FenetreMontre() {
        super("Simulateur de Montre Connectée – POO1");

        Montre montre = new Montre("Ma Montre");

        panneauMontre = new PanneauMontre(montre, 180);
        panneauConfig = new PanneauConfig(panneauMontre);

        construireUI();
        configurerTimer();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void construireUI() {
        //JPanel pour la zone ou ya les composantes 
        JPanel main = new JPanel(new BorderLayout(10, 0));
        main.setBackground(new Color(18, 18, 28));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel droite = new JPanel(new BorderLayout());
        droite.setBackground(new Color(18, 18, 28));
      //JLabel est une zone pour afficher une chaîne courte ou une image ou les deux, on peut changer la position , center left or right 
        JLabel titre = new JLabel(" Montre yasnad", SwingConstants.CENTER);
        titre.setFont(new Font("Serif", Font.BOLD, 22));
        titre.setForeground(new Color(140, 180, 255));
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        droite.add(titre, BorderLayout.NORTH);
        droite.add(panneauMontre, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(panneauConfig,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBackground(new Color(22, 25, 40));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(22, 25, 40));

        main.add(scroll, BorderLayout.WEST);
        main.add(droite, BorderLayout.CENTER);

        this.setContentPane(main);
        this.setJMenuBar(construireMenuBar());
    }

    private JMenuBar construireMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        //JMenuBar permet de créer un menu déroulant dans notre cas 
        menuBar.setBackground(new Color(22, 25, 40));
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        JMenu menuFichier = creerMenu("Fichier");

        JMenuItem miNouveau = new JMenuItem("Nouvelle montre");
        miNouveau.setAccelerator(KeyStroke.getKeyStroke("control N"));
        miNouveau.addActionListener(e -> nouvelleMontre());

        JMenuItem miSauvegarder = new JMenuItem("Sauvegarder…");
        miSauvegarder.setAccelerator(KeyStroke.getKeyStroke("control S"));
        miSauvegarder.addActionListener(e -> sauvegarder());

        JMenuItem miCharger = new JMenuItem("Charger…");
        miCharger.setAccelerator(KeyStroke.getKeyStroke("control O"));
        miCharger.addActionListener(e -> charger());

        JMenuItem miQuitter = new JMenuItem("Quitter");
        miQuitter.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        miQuitter.addActionListener(e -> System.exit(0));

        menuFichier.add(miNouveau);
        menuFichier.addSeparator();
        menuFichier.add(miSauvegarder);
        menuFichier.add(miCharger);
        menuFichier.addSeparator();
        menuFichier.add(miQuitter);
        menuBar.add(menuFichier);

        JMenu menuAide = creerMenu("Aide");
        JMenuItem miAPropos = new JMenuItem("À propos…");
        miAPropos.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Simulateur de Montre Connectée\nProjet POO1 2025-2026\n\n" +
                "Modélisation OO : héritage, encapsulation, abstraction,\n" +
                "GUI Swing, sérialisation Java.",
                "À propos", JOptionPane.INFORMATION_MESSAGE));
        menuAide.add(miAPropos);
        menuBar.add(menuAide);

        return menuBar;
    }

    private JMenu creerMenu(String titre) {
        JMenu m = new JMenu(titre);
        m.setForeground(new Color(200, 220, 255));
        m.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return m;
    }

    private void configurerTimer() {
        timerActualisation = new Timer(1000, e -> {
            panneauMontre.repaint();
        });
        timerActualisation.start();
    }

    // Détecte le bon dossier selon l'OS (WSL, Linux, Windows)
    private File getDossierDepart() {
        String userHome = System.getProperty("user.home");
        String cheminWindows = userHome.replace("/root", "/mnt/c/Users")
                                       .replace("/home/", "/mnt/c/Users/");
        File dossierWindows = new File(cheminWindows);
        if (dossierWindows.exists()) {
            return dossierWindows;
        }
        return new File(userHome);
    }

    // Actions 

    private void nouvelleMontre() {
        String nom = JOptionPane.showInputDialog(this, "Nom de la nouvelle montre :", "Nouvelle montre", JOptionPane.QUESTION_MESSAGE);
        if (nom != null && !nom.trim().isEmpty()) {
            panneauMontre.setMontre(new Montre(nom));
        }
    }

    private void sauvegarder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(getDossierDepart());
        chooser.setDialogTitle("Sauvegarder la montre");
        chooser.setFileFilter(new FileNameExtensionFilter("Fichier montre (*.montre)", "montre"));
        chooser.setSelectedFile(new File(panneauMontre.getMontre().getNom() + ".montre"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (!f.getName().endsWith(".montre")) f = new File(f.getPath() + ".montre");
            try {
                GestionnaireMontre.sauvegarder(panneauMontre.getMontre(), f);
                JOptionPane.showMessageDialog(this, "Montre sauvegardée dans :\n" + f.getAbsolutePath(),
                        "Sauvegarde réussie", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde :\n" + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void charger() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(getDossierDepart());
        chooser.setDialogTitle("Charger une montre");
        chooser.setFileFilter(new FileNameExtensionFilter("Fichier montre (*.montre)", "montre"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Montre m = GestionnaireMontre.charger(chooser.getSelectedFile());
                panneauMontre.setMontre(m);
                setTitle("Simulateur de Montre – " + m.getNom());
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement :\n" + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}