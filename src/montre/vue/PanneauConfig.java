package montre.vue;

import montre.modele.*;
import montre.modele.ElementSecondes.TypeSecondes;
import montre.modele.ElementDate.FormatDate;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class PanneauConfig extends JPanel {

    private final PanneauMontre panneauMontre;
    // Attribut de classe pour que rbNum puisse y accéder
    private JComboBox<TypeSecondes> cbTypeSec;

    public PanneauConfig(PanneauMontre panneauMontre) {
        this.panneauMontre = panneauMontre;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(22, 25, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(260, 0));
        construire();
    }

    private void construire() {
        add(creerSectionBoitier());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(creerSectionFond());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(creerSectionAffichage());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(creerSectionElements());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(creerSectionSimulation());
        add(Box.createVerticalGlue());
    }

    // Boîtier 
    private JPanel creerSectionBoitier() {
        JPanel p = creerSection("Boîtier");

        JComboBox<Boitier.FormeBoitier> cbForme = new JComboBox<>(Boitier.FormeBoitier.values());
        cbForme.setSelectedItem(getMontre().getBoitier().getForme());
        cbForme.addActionListener(e -> {
            getMontre().getBoitier().setForme((Boitier.FormeBoitier) cbForme.getSelectedItem());
            rafraichir();
        });
        styliserCombo(cbForme);

        JButton btnCouleurBord = creerBoutonCouleur("Couleur bord", getMontre().getBoitier().getCouleurBord(), c -> {
            getMontre().getBoitier().setCouleurBord(c);
            rafraichir();
        });

        p.add(labelStyle("Forme :"));
        p.add(cbForme);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        p.add(btnCouleurBord);
        return p;
    }

    // Fond 
    private JPanel creerSectionFond() {
        JPanel p = creerSection("Fond");

        JComboBox<FondCadran.TypeFond> cbType = new JComboBox<>(FondCadran.TypeFond.values());
        cbType.setSelectedItem(getMontre().getFond().getType());
        styliserCombo(cbType);

        JButton btnC1 = creerBoutonCouleur("Couleur 1", getMontre().getFond().getCouleur1(), c -> {
            getMontre().getFond().setCouleur1(c);
            rafraichir();
        });
        JButton btnC2 = creerBoutonCouleur("Couleur 2", getMontre().getFond().getCouleur2(), c -> {
            getMontre().getFond().setCouleur2(c);
            rafraichir();
        });

        JButton btnImage = new JButton("Choisir une image...");
        btnImage.setBackground(new Color(40, 45, 65));
        btnImage.setForeground(new Color(200, 220, 255));
        btnImage.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnImage.setFocusPainted(false);
        btnImage.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnImage.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        btnImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            String userHome = System.getProperty("user.home");
            String cheminWindows = userHome.replace("/root", "/mnt/c/Users").replace("/home/", "/mnt/c/Users/");
            java.io.File dossierDepart = new java.io.File(cheminWindows);
            if (!dossierDepart.exists()) {
                dossierDepart = new java.io.File(userHome);
            }
            chooser.setCurrentDirectory(dossierDepart);
            chooser.setDialogTitle("Choisir une image de fond");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Images (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                getMontre().getFond().chargerImage(
                    chooser.getSelectedFile().getAbsolutePath());
                cbType.setSelectedItem(FondCadran.TypeFond.IMAGE);
                rafraichir();
            }
        });

        btnImage.setVisible(getMontre().getFond().getType() == FondCadran.TypeFond.IMAGE);

        cbType.addActionListener(e -> {
            FondCadran.TypeFond type = (FondCadran.TypeFond) cbType.getSelectedItem();
            getMontre().getFond().setType(type);
            btnImage.setVisible(type == FondCadran.TypeFond.IMAGE);
            rafraichir();
        });

        p.add(labelStyle("Type :"));
        p.add(cbType);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        p.add(btnC1);
        p.add(Box.createRigidArea(new Dimension(0, 3)));
        p.add(btnC2);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        p.add(btnImage);
        return p;
    }

    // Affichage heure 
    private JPanel creerSectionAffichage() {
        JPanel p = creerSection("Affichage heure");

        ButtonGroup bg = new ButtonGroup();
        JRadioButton rbAnalog = new JRadioButton("Analogique");
        JRadioButton rbNum = new JRadioButton("Numérique");
        styliserRadio(rbAnalog);
        styliserRadio(rbNum);
        bg.add(rbAnalog);
        bg.add(rbNum);

        boolean isAnalog = getMontre().getAffichageHeure() instanceof AffichageAnalogique;
        rbAnalog.setSelected(isAnalog);
        rbNum.setSelected(!isAnalog);

        rbAnalog.addActionListener(e -> {
            if (!(getMontre().getAffichageHeure() instanceof AffichageAnalogique)) {
                getMontre().setAffichageHeure(new AffichageAnalogique());
                rafraichir();
            }
        });

        rbNum.addActionListener(e -> {
            if (!(getMontre().getAffichageHeure() instanceof AffichageNumerique)) {
                getMontre().setAffichageHeure(new AffichageNumerique());
                // Désactive automatiquement la trotteuse si elle est active
                for (ElementMontre el : getMontre().getElements()) {
                    if (el instanceof ElementSecondes) {
                        ElementSecondes es = (ElementSecondes) el;
                        if (es.getType() == TypeSecondes.TROTTEUSE) {
                            es.setType(TypeSecondes.MINI_CADRAN);
                            cbTypeSec.setSelectedItem(TypeSecondes.MINI_CADRAN);
                        }
                    }
                }
                rafraichir();
            }
        });

        JButton btnCouleurH = creerBoutonCouleur("Couleur aiguille H", Color.WHITE, c -> {
            if (getMontre().getAffichageHeure() instanceof AffichageAnalogique) {
                ((AffichageAnalogique) getMontre().getAffichageHeure()).setCouleurHeures(c);
                rafraichir();
            }
        });
        JButton btnCouleurM = creerBoutonCouleur("Couleur aiguille M", Color.WHITE, c -> {
            if (getMontre().getAffichageHeure() instanceof AffichageAnalogique) {
                ((AffichageAnalogique) getMontre().getAffichageHeure()).setCouleurMinutes(c);
                rafraichir();
            }
        });

        JCheckBox cbChiffres = new JCheckBox("Afficher chiffres");
        styliserCheckbox(cbChiffres);
        cbChiffres.setSelected(true);
        cbChiffres.addActionListener(e -> {
            if (getMontre().getAffichageHeure() instanceof AffichageAnalogique) {
                ((AffichageAnalogique) getMontre().getAffichageHeure()).setAfficherChiffres(cbChiffres.isSelected());
                rafraichir();
            }
        });

        p.add(rbAnalog);
        p.add(rbNum);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        p.add(btnCouleurH);
        p.add(Box.createRigidArea(new Dimension(0, 3)));
        p.add(btnCouleurM);
        p.add(Box.createRigidArea(new Dimension(0, 3)));
        p.add(cbChiffres);
        return p;
    }

    // Éléments optionnels 
    private JPanel creerSectionElements() {
        JPanel p = creerSection("Éléments optionnels");

        //  Secondes 
        JCheckBox cbSec = new JCheckBox("Secondes");
        styliserCheckbox(cbSec);
        // On utilise l'attribut de classe cbTypeSec
        cbTypeSec = new JComboBox<>(TypeSecondes.values());
        styliserCombo(cbTypeSec);

        cbSec.addActionListener(e -> {
            getMontre().retirerElementsDuType(ElementSecondes.class);
            if (cbSec.isSelected()) {
                ElementSecondes es = new ElementSecondes();
                TypeSecondes type = (TypeSecondes) cbTypeSec.getSelectedItem();
                if (getMontre().getAffichageHeure() instanceof AffichageNumerique
                        && type == TypeSecondes.TROTTEUSE) {
                    cbTypeSec.setSelectedItem(TypeSecondes.MINI_CADRAN);
                    type = TypeSecondes.MINI_CADRAN;
                }
                es.setType(type);
                getMontre().ajouterElement(es);
            }
            rafraichir();
        });

        cbTypeSec.addActionListener(e -> {
            getMontre().retirerElementsDuType(ElementSecondes.class);
            TypeSecondes type = (TypeSecondes) cbTypeSec.getSelectedItem();
            if (getMontre().getAffichageHeure() instanceof AffichageNumerique
                    && type == TypeSecondes.TROTTEUSE) {
                cbTypeSec.setSelectedItem(TypeSecondes.MINI_CADRAN);
                return;
            }
            if (cbSec.isSelected()) {
                ElementSecondes es = new ElementSecondes();
                es.setType(type);
                getMontre().ajouterElement(es);
            }
            rafraichir();
        });

        // Date 
        JCheckBox cbDate = new JCheckBox("Date");
        styliserCheckbox(cbDate);
        JComboBox<FormatDate> cbFmtDate = new JComboBox<>(FormatDate.values());
        styliserCombo(cbFmtDate);

        cbDate.addActionListener(e -> {
            getMontre().retirerElementsDuType(ElementDate.class);
            if (cbDate.isSelected()) {
                ElementDate ed = new ElementDate();
                ed.setFormat((FormatDate) cbFmtDate.getSelectedItem());
                getMontre().ajouterElement(ed);
            }
            rafraichir();
        });
        cbFmtDate.addActionListener(e -> {
            getMontre().retirerElementsDuType(ElementDate.class);
            if (cbDate.isSelected()) {
                ElementDate ed = new ElementDate();
                ed.setFormat((FormatDate) cbFmtDate.getSelectedItem());
                getMontre().ajouterElement(ed);
            }
            rafraichir();
        });

        //  Rythme cardiaque 
        JCheckBox cbBpm = new JCheckBox("Rythme cardiaque");
        styliserCheckbox(cbBpm);
        cbBpm.addActionListener(e -> {
            getMontre().retirerElementsDuType(ElementRythmeCardiaque.class);
            if (cbBpm.isSelected()) getMontre().ajouterElement(new ElementRythmeCardiaque());
            rafraichir();
        });

        // Batterie 
        JCheckBox cbBat = new JCheckBox("Batterie");
        styliserCheckbox(cbBat);
        cbBat.addActionListener(e -> {
            getMontre().retirerElementsDuType(ElementBatterie.class);
            if (cbBat.isSelected()) getMontre().ajouterElement(new ElementBatterie());
            rafraichir();
        });

        p.add(cbSec);
        p.add(cbTypeSec);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(cbDate);
        p.add(cbFmtDate);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(cbBpm);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(cbBat);
        return p;
    }

    // Simulation 
    private JPanel creerSectionSimulation() {
        JPanel p = creerSection("Simulation");

        JCheckBox cbSimul = new JCheckBox("Heure personnalisée");
        styliserCheckbox(cbSimul);

        SpinnerNumberModel mH = new SpinnerNumberModel(12, 0, 23, 1);
        SpinnerNumberModel mM = new SpinnerNumberModel(0, 0, 59, 1);
        SpinnerNumberModel mS = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner spH = new JSpinner(mH);
        JSpinner spM = new JSpinner(mM);
        JSpinner spS = new JSpinner(mS);
        styliserSpinner(spH); styliserSpinner(spM); styliserSpinner(spS);

        Runnable appliquerSimul = () -> {
            if (cbSimul.isSelected()) {
                getMontre().setHeureSimulee((int) spH.getValue(), (int) spM.getValue(), (int) spS.getValue());
            } else {
                getMontre().utiliserHeureSystème();
            }
            rafraichir();
        };

        cbSimul.addActionListener(e -> appliquerSimul.run());
        spH.addChangeListener(e -> appliquerSimul.run());
        spM.addChangeListener(e -> appliquerSimul.run());
        spS.addChangeListener(e -> appliquerSimul.run());

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        row.setBackground(new Color(22, 25, 40));
        row.add(labelStyle("H:")); row.add(spH);
        row.add(labelStyle(" M:")); row.add(spM);
        row.add(labelStyle(" S:")); row.add(spS);

        p.add(cbSimul);
        p.add(row);
        return p;
    }

    // Utilitaires UI 

    private JPanel creerSection(String titre) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(28, 32, 50));
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 90, 130), 1), titre);
        border.setTitleColor(new Color(140, 180, 255));
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        p.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 8, 8, 8)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JLabel labelStyle(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(new Color(180, 200, 230));
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        return l;
    }

    private JButton creerBoutonCouleur(String label, Color initial, java.util.function.Consumer<Color> onChange) {
        JButton btn = new JButton(label);
        btn.setBackground(initial);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(80, 100, 150)));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        btn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choisir une couleur", btn.getBackground());
            if (c != null) {
                btn.setBackground(c);
                onChange.accept(c);
            }
        });
        return btn;
    }

    private void styliserCombo(JComboBox<?> cb) {
        cb.setBackground(new Color(40, 45, 65));
        cb.setForeground(new Color(200, 220, 255));
        cb.setFont(new Font("SansSerif", Font.PLAIN, 11));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
    }

    private void styliserCheckbox(JCheckBox cb) {
        cb.setBackground(new Color(28, 32, 50));
        cb.setForeground(new Color(200, 220, 255));
        cb.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styliserRadio(JRadioButton rb) {
        rb.setBackground(new Color(28, 32, 50));
        rb.setForeground(new Color(200, 220, 255));
        rb.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rb.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styliserSpinner(JSpinner sp) {
        sp.setPreferredSize(new Dimension(52, 22));
        sp.setFont(new Font("Courier New", Font.BOLD, 11));
    }

    private Montre getMontre() { return panneauMontre.getMontre(); }
    private void rafraichir() { panneauMontre.repaint(); }
}