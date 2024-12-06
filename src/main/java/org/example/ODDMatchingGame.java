package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ODDMatchingGame extends JFrame {

    private ArrayList<String> descriptions;  // Liste des descriptions des ODD
    private ArrayList<String> actionsCorrectes; // Liste des actions correctes
    private ArrayList<String> optionsODD;    // Liste des options correspondant aux ODD
    private int indexActuel;  // L'indice de la description actuelle
    private JLabel labelDescription;  // Label pour afficher la description
    private JButton[] boutonsOptions;   // Boutons pour les options
    private JFrame accueilFrame; // Fenêtre d'accueil

    public ODDMatchingGame() {
        // Initialisation des listes
        descriptions = new ArrayList<>();
        actionsCorrectes = new ArrayList<>();
        optionsODD = new ArrayList<>();
        chargerDonneesDepuisFichier("odds.txt");

        // Créer la fenêtre d'accueil
        accueilFrame = new JFrame("Jeu des ODD");
        accueilFrame.setSize(600, 400);
        accueilFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        accueilFrame.setLayout(new BorderLayout());

        // Ajouter un texte expliquant le but du jeu
        JLabel labelIntro = new JLabel("<html><div style='text-align: center; font-size: 20px;'>Bienvenue dans le Jeu de Correspondance des ODD !<br><br>" +
                "Il faut associer les Objectifs de Développement Durable aux actions quotidiennes correspondantes.<br>" , JLabel.CENTER);
        labelIntro.setFont(new Font("Arial", Font.PLAIN, 18));
        accueilFrame.add(labelIntro, BorderLayout.CENTER);

        // Ajouter un bouton pour commencer le jeu
        JButton boutonCommencer = new JButton("Commencer");
        boutonCommencer.setFont(new Font("Arial", Font.PLAIN, 20));
        boutonCommencer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accueilFrame.setVisible(false);  // Fermer la fenêtre d'accueil
                initialiserJeu();  // Lancer le jeu
            }
        });
        accueilFrame.add(boutonCommencer, BorderLayout.SOUTH);

        accueilFrame.setVisible(true);  // Afficher la fenêtre d'accueil
    }

    // Méthode pour initialiser et démarrer le jeu
    private void initialiserJeu() {
        // Initialisation des données du jeu
        Collections.shuffle(optionsODD);

        indexActuel = 0;  // Initialiser l'indice à 0

        // Configuration de la fenêtre du jeu
        setTitle("Jeu de Correspondance des ODD");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Ajouter le label pour la description
        labelDescription = new JLabel("Description ici", JLabel.CENTER);
        labelDescription.setFont(new Font("Arial", Font.PLAIN, 20));
        add(labelDescription, BorderLayout.NORTH);

        // Créer un panneau pour les boutons
        JPanel panneauOptions = new JPanel();
        panneauOptions.setLayout(new GridLayout(2, 2));  // Grille 2x2 de boutons
        boutonsOptions = new JButton[4];

        // Créer les boutons et les ajouter au panneau
        for (int i = 0; i < 4; i++) {
            boutonsOptions[i] = new JButton();
            boutonsOptions[i].addActionListener(new EcouteurBoutonODD(i));
            panneauOptions.add(boutonsOptions[i]);
        }

        add(panneauOptions, BorderLayout.CENTER);

        // Charger la première description
        chargerProchaineDescription();

        setVisible(true);
    }

    // Méthode pour charger les données du jeu depuis un fichier texte
    private void chargerDonneesDepuisFichier(String cheminFichier) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                // Séparer la ligne en ODD, action et option
                String[] parties = ligne.split("Action :");
                if (parties.length == 2) {
                    descriptions.add(parties[0].trim());  // Description de l'ODD
                    actionsCorrectes.add(parties[1].trim()); // Action correcte
                    optionsODD.add(parties[1].trim());    // L'action de base dans les options
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour charger la prochaine description et les options
    private void chargerProchaineDescription() {
        if (indexActuel < descriptions.size()) {
            labelDescription.setText(descriptions.get(indexActuel));  // Afficher la description actuelle

            // Mélanger les options à chaque fois
            Collections.shuffle(optionsODD);

            // Remplir les boutons avec les options
            for (int i = 0; i < boutonsOptions.length; i++) {
                boutonsOptions[i].setText(optionsODD.get(i));  // Assigner une option à chaque bouton
            }

            indexActuel++;  // Passer à la prochaine description
        } else {
            // Le jeu est terminé, afficher un message
            labelDescription.setText("Félicitations ! Vous avez terminé le jeu.");
            for (JButton bouton : boutonsOptions) {
                bouton.setEnabled(false);  // Désactiver les boutons
            }
        }
    }

    // Classe interne pour gérer l'action des boutons
    private class EcouteurBoutonODD implements ActionListener {
        private int indexBouton;

        public EcouteurBoutonODD(int indexBouton) {
            this.indexBouton = indexBouton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Vérifier si l'option choisie est correcte
            String optionChoisie = boutonsOptions[indexBouton].getText();
            String reponseCorrecte = actionsCorrectes.get(indexActuel - 1);  // L'action correcte pour l'ODD actuel

            if (optionChoisie.equals(reponseCorrecte)) {
                JOptionPane.showMessageDialog(null, "Correct ! Prochaine question.");
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect !");
            }

            // Charger la prochaine question après un délai
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chargerProchaineDescription();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    public static void main(String[] args) {
        new ODDMatchingGame();
    }
    private void addBackButtonToGame(JFrame gameWindow) {
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cache la fenêtre actuelle du jeu
                gameWindow.setVisible(false);

                // Crée et affiche la fenêtre principale
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            }
        });

        // Ajouter le bouton à la fenêtre de jeu
        gameWindow.add(backButton, BorderLayout.SOUTH);
    }

}
