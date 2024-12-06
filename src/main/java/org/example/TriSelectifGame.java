package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TriSelectifGame extends JFrame {

    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JButton startButton;
    private JPanel gamePanel;
    private JPanel recycleBin;
    private JPanel compostBin;
    private JPanel trashBin;
    private JLabel itemLabel;
    private String currentItem;
    private int score;
    private JLabel scoreLabel;
    private List<Dechet> dechets;

    public TriSelectifGame() {
        setTitle("Tri Sélectif Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Label de bienvenue
        welcomeLabel = new JLabel("Bienvenue dans le jeu Tri Sélectif !", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Bouton pour démarrer le jeu
        startButton = new JButton("Commencer");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(startButton, BorderLayout.SOUTH);

        // Ajout du panel principal à la fenêtre
        setContentPane(mainPanel);
    }

    private void startGame() {
        // Charger les données depuis le fichier texte
        loadDechetsFromFile();

        // Configuration de la fenêtre de jeu
        welcomeLabel.setText("Trier les objets dans la bonne poubelle !");
        welcomeLabel.setForeground(Color.BLACK);

        // Désactivation du bouton "Commencer"
        startButton.setEnabled(false);

        // Initialiser le score
        score = 0;

        // Création du label de score
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Création des catégories de tri
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(1, 3)); // Trois zones de tri (recyclage, compost, poubelle)

        recycleBin = createBinPanel("Recyclage", Color.BLUE);
        compostBin = createBinPanel("Compost", Color.GREEN);
        trashBin = createBinPanel("Poubelle", Color.RED);

        gamePanel.add(recycleBin);
        gamePanel.add(compostBin);
        gamePanel.add(trashBin);

        // Création d'un objet à trier
        itemLabel = new JLabel();
        itemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        itemLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Initialiser l'objet à trier
        generateItem();

        // Ajouter l'objet à trier à la fenêtre
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.add(itemLabel, BorderLayout.CENTER);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de jeu avec l'objet à trier et les poubelles
        JPanel gameContainer = new JPanel();
        gameContainer.setLayout(new BorderLayout());
        gameContainer.add(scoreLabel, BorderLayout.NORTH);
        gameContainer.add(itemPanel, BorderLayout.CENTER);
        gameContainer.add(gamePanel, BorderLayout.SOUTH);

        // Remplacer le contenu de la fenêtre par le panel de jeu
        setContentPane(gameContainer);
        revalidate();
    }

    private JPanel createBinPanel(String binName, Color color) {
        JPanel binPanel = new JPanel();
        binPanel.setBackground(color);
        binPanel.setBorder(BorderFactory.createTitledBorder(binName));
        binPanel.setLayout(new BorderLayout());

        binPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkBinSelection(binName);
            }
        });

        return binPanel;
    }

    private void generateItem() {
        // Sélectionner un objet au hasard depuis la liste de déchets
        Random random = new Random();
        Dechet selectedDechet = dechets.get(random.nextInt(dechets.size()));
        currentItem = selectedDechet.getNom();
        itemLabel.setText(currentItem);
    }

    private void checkBinSelection(String selectedBin) {
        // Trouver la catégorie correcte pour l'objet actuel
        Dechet selectedDechet = dechets.stream()
                .filter(dechet -> dechet.getNom().equals(currentItem))
                .findFirst()
                .orElse(null);

        if (selectedDechet != null && selectedBin.equals(selectedDechet.getCategorie())) {
            score++;
            JOptionPane.showMessageDialog(this, "Bon tri !", "Correct", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Mauvais tri, essaye encore !", "Incorrect", JOptionPane.WARNING_MESSAGE);
        }

        // Mettre à jour le score et générer un nouvel objet
        scoreLabel.setText("Score: " + score);
        generateItem();
    }

    private void loadDechetsFromFile() {
        dechets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("dechets.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|"); // Séparer le nom et la catégorie
                if (parts.length == 2) {
                    String nom = parts[0].trim();
                    String categorie = parts[1].trim();
                    dechets.add(new Dechet(nom, categorie));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TriSelectifGame game = new TriSelectifGame();
                game.setVisible(true);
            }
        });
    }

    // Classe pour représenter un déchet
    public static class Dechet {
        private String nom;
        private String categorie;

        public Dechet(String nom, String categorie) {
            this.nom = nom;
            this.categorie = categorie;
        }

        // Getters
        public String getNom() {
            return nom;
        }

        public String getCategorie() {
            return categorie;
        }
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
