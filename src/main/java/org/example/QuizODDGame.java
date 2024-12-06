package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizODDGame {
    public JFrame frame;
    private JPanel panel;
    private JLabel questionLabel;
    private JRadioButton answer1, answer2, answer3, answer4;
    private ButtonGroup answersGroup;
    private List<Question> questions;
    private int currentQuestionIndex;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizODDGame window = new QuizODDGame();
            window.frame.setVisible(true);
        });
    }

    public QuizODDGame() {
        // Chargement des questions depuis le fichier texte
        questions = loadQuestions("questions.txt");

        // Vérifie si des questions ont été chargées
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucune question chargée, veuillez vérifier votre fichier de questions.");
            System.exit(0);  // Termine l'exécution si aucune question n'est chargée
        }

        currentQuestionIndex = 0;

        // Configuration de la fenêtre principale
        frame = new JFrame("Quiz sur les ODD");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        questionLabel = new JLabel("Question", SwingConstants.CENTER);
        panel.add(questionLabel);

        answer1 = new JRadioButton("Réponse 1");
        answer2 = new JRadioButton("Réponse 2");
        answer3 = new JRadioButton("Réponse 3");
        answer4 = new JRadioButton("Réponse 4");

        answersGroup = new ButtonGroup();
        answersGroup.add(answer1);
        answersGroup.add(answer2);
        answersGroup.add(answer3);
        answersGroup.add(answer4);

        panel.add(answer1);
        panel.add(answer2);
        panel.add(answer3);
        panel.add(answer4);

        JButton nextButton = new JButton("Suivant");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion(currentQuestionIndex);
                } else {
                    JOptionPane.showMessageDialog(frame, "Fin du quiz !");
                    System.exit(0);  // Quitte le programme après la dernière question
                }
            }
        });
        panel.add(nextButton);

        frame.add(panel);
        displayQuestion(currentQuestionIndex);
    }

    // Affiche la question et les réponses
    private void displayQuestion(int index) {
        Question question = questions.get(index);

        questionLabel.setText(question.getQuestion());

        // Mélanger les réponses et les afficher
        List<String> answers = new ArrayList<>();
        answers.add(question.getAnswer1());
        answers.add(question.getAnswer2());
        answers.add(question.getAnswer3());
        answers.add(question.getAnswer4());

        // Mélanger les réponses
        Collections.shuffle(answers);

        // Affecter les réponses mélangées aux JRadioButton
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));

        // Réinitialiser les sélections
        answersGroup.clearSelection();
    }

    // Vérifie si la réponse choisie est correcte
    private void checkAnswer() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        String selectedAnswer = getSelectedAnswer();

        if (selectedAnswer != null && selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            JOptionPane.showMessageDialog(frame, "Bonne réponse !", "Feedback", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Mauvaise réponse. La bonne réponse est : " + currentQuestion.getCorrectAnswer(), "Feedback", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Récupère la réponse sélectionnée par l'utilisateur
    private String getSelectedAnswer() {
        if (answer1.isSelected()) {
            return answer1.getText();
        } else if (answer2.isSelected()) {
            return answer2.getText();
        } else if (answer3.isSelected()) {
            return answer3.getText();
        } else if (answer4.isSelected()) {
            return answer4.getText();
        }
        return null;
    }

    // Charge les questions depuis le fichier texte
    private List<Question> loadQuestions(String fileName) {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {  // Assure que la ligne a bien 5 éléments (question + 4 réponses)
                    // La première réponse est la bonne
                    questions.add(new Question(parts[0], parts[1], parts[2], parts[3], parts[1])); // parts[1] est la bonne réponse
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Classe interne représentant une question avec ses réponses
    class Question {
        private String question;
        private String answer1;
        private String answer2;
        private String answer3;
        private String answer4;
        private String correctAnswer;

        public Question(String question, String answer1, String answer2, String answer3, String correctAnswer) {
            this.question = question;
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.answer4 = answer3;  // Correction possible dans le cas de 4 réponses
            this.correctAnswer = correctAnswer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer1() {
            return answer1;
        }

        public String getAnswer2() {
            return answer2;
        }

        public String getAnswer3() {
            return answer3;
        }

        public String getAnswer4() {
            return answer4;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
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
