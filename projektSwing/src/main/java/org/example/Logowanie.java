package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.example.PowitalnyWidok;
import org.example.kontrolery.LoginController;
import org.example.modele.Uzytkownicy;
import org.example.serwisy.InternetTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Logowanie {
    static JFrame frame;
    static CardLayout cardLayout;
    public Logowanie(){
        frame = new JFrame("Formularz logowania");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        cardLayout = new CardLayout();

    }
    public static JPanel widok() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(cardLayout);


        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Nazwa użytkownika:");
        JTextField userField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Hasło:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Zaloguj");

        JLabel infoLabel = new JLabel("");



        // Dodajemy komponenty do loginPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Ustawienie infoLabel pod loginButton
        loginPanel.add(infoLabel, gbc);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Animowany pasek
        progressBar.setVisible(false); // Ukryj na starcie
        loginPanel.add(progressBar, gbc);




        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel z obrazkami
        JLabel imageLabel1 = new JLabel(new ImageIcon(
                new ImageIcon("C:\\Users\\lolki\\IdeaProjects\\projektSwing\\src\\main\\java\\org\\example\\last.png").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
        JLabel imageLabel2 = new JLabel(new ImageIcon(
                new ImageIcon("C:\\Users\\lolki\\IdeaProjects\\projektSwing\\src\\main\\java\\org\\example\\spotify.jpg").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));

        imagePanel.add(imageLabel1);
        imagePanel.add(imageLabel2);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(imagePanel, gbc);



        // Dodanie panelu logowania do mainPanel
        mainPanel.add(loginPanel, "Login");

        LoginController loginController = new LoginController();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progressBar.setVisible(true); // Pokaż pasek postępu
                loginButton.setEnabled(false); // Wyłącz przycisk, aby zapobiec wielokrotnemu klikaniu


                SwingWorker<Uzytkownicy, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Uzytkownicy doInBackground() {

                        String username = userField.getText();
                        String password = new String(passwordField.getPassword());
                        return loginController.znajdzUzytkownika(username, password);
                    }

                    @Override
                    protected void done() {

                        progressBar.setVisible(false); // Ukryj pasek postępu
                        loginButton.setEnabled(true); // Włącz przycisk ponownie

                        try {
                            Uzytkownicy u = get();
                            if (u != null) {

                                JPanel mainScreenPanel = PowitalnyWidok.widok(cardLayout, mainPanel, u);
                                mainPanel.add(mainScreenPanel, "MainScreen");
                                cardLayout.show(mainPanel, "MainScreen");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Nieprawidłowa nazwa użytkownika lub hasło.", "Błąd", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Wystąpił błąd podczas logowania.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                worker.execute();
            }
        });




        InternetTest.getError(cardLayout,mainPanel);

        frame.add(mainPanel);


        frame.setVisible(true);
        return mainPanel;
    }
}
