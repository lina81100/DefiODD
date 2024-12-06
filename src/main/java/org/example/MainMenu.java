package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        // Configuration de la fenêtre principale
        setTitle("Menu Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Label d'introduction
        JLabel labelIntro = new JLabel("Bienvenue dans les jeux éducatifs !", JLabel.CENTER);
        labelIntro.setFont(new Font("Arial", Font.PLAIN, 20));
        add(labelIntro, BorderLayout.NORTH);

        // Panneau pour les boutons
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(4, 1));  // 4 lignes de boutons

        JButton triSelectifButton = new JButton("Tri Sélectif");
        triSelectifButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvre la fenêtre du jeu de tri
                TriSelectifGame triSelectifWindow = new TriSelectifGame();
                triSelectifWindow.setVisible(true);
                dispose();  // Ferme la fenêtre actuelle
            }
        });

        JButton quizButton = new JButton("Quiz");
        quizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvre la fenêtre du quiz
                QuizODDGame quizWindow = new QuizODDGame();
                quizWindow.frame.setVisible(true);
                dispose();  // Ferme la fenêtre actuelle
            }
        });

        JButton btnMatchingGame = new JButton("Jeu de Correspondance ODD");
        btnMatchingGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir le jeu de correspondance des ODD
                new ODDMatchingGame();  // Appeler la classe correcte pour le jeu de correspondance des ODD
                setVisible(false);  // Fermer le menu principal
            }
        });

        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Quitter l'application
            }
        });

        // Ajouter les boutons au panneau
        panelButtons.add(triSelectifButton);
        panelButtons.add(quizButton);
        panelButtons.add(btnMatchingGame);
        panelButtons.add(btnQuitter);

        // Ajouter le panneau des boutons à la fenêtre principale
        add(panelButtons, BorderLayout.CENTER);

        // Afficher la fenêtre principale
        setVisible(true);
    }

    public static void main(String[] args) {
        // Lancer le menu principal
        new MainMenu();
    }
}
