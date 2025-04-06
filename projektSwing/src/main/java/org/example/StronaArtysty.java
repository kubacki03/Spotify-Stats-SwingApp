package org.example;
import org.example.kontrolery.ArtystaKontroler;
import org.example.modele.Artysta;
import org.example.modele.ArtystaKomentarze;
import org.example.modele.ArtystaSpotify;
import org.example.modele.Uzytkownicy;
import org.example.serwisy.ImageService;
import org.example.serwisy.InternetTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class StronaArtysty {

    public JPanel widok(CardLayout cardLayout, JPanel mainPanel, Artysta a, Uzytkownicy u) throws IOException {
        JPanel mainScreenPanel = new JPanel(new BorderLayout());
        JButton glowna = new JButton("Glowna");
        glowna.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel mainScreenPanel = null;
                try {
                    mainScreenPanel = PowitalnyWidok.widok(cardLayout, mainPanel, u);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                mainPanel.add(mainScreenPanel, "MainScreen");


                cardLayout.show(mainPanel, "MainScreen");
            }
        });
        JButton backButton = new JButton("Powrót");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            try {
                JPanel powitalnyPanel = PowitalnyWidok.widok(cardLayout, mainPanel, u);
                mainPanel.add(powitalnyPanel, "MainScreen");
                cardLayout.show(mainPanel, "MainScreen");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        JLabel label = new JLabel("Artysta: " + a.getNazwa(), SwingConstants.CENTER);

        ArtystaKontroler ar = new ArtystaKontroler();
        ArtystaSpotify as = ar.artystaSzczegoly(a.getSpotifyId());



        BufferedImage image = ImageService.getImage(as.getImages().get(0).getUrl());

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int newWidth = 450;
        int newHeight = (newWidth * originalHeight) / originalWidth;

        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);


        ImageIcon icon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(icon);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel ratingPanel = new JPanel(new FlowLayout());
        JLabel rateLabel = new JLabel("Oceń artystę (1-5):");
        Integer[] oceny = {1, 2, 3, 4, 5};
        JComboBox<Integer> ocenaComboBox = new JComboBox<>(oceny);
        JButton dodajOcene = new JButton("Dodaj ocenę");

        float ocena = ar.getOcena(a.getSpotifyId());
        Label poleOcena = new Label("Ocena: " + String.format("%.2f", ocena));

        dodajOcene.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ocena = (int) ocenaComboBox.getSelectedItem();
                ar.ocenArtyste(a.getSpotifyId(), ocena);
                float nowaOcena = ar.getOcena(a.getSpotifyId());
                poleOcena.setText("Ocena: " + nowaOcena);
                JOptionPane.showMessageDialog(mainScreenPanel,
                        "Dziękujemy za ocenę " + ocena + " dla artysty " + a.getNazwa() + "!",
                        "Ocena dodana",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ratingPanel.add(poleOcena);
        ratingPanel.add(rateLabel);
        ratingPanel.add(ocenaComboBox);
        ratingPanel.add(dodajOcene);

        // Panel z komentarzami
        JPanel commentsPanel = new JPanel(new BorderLayout());
        JLabel commentsLabel = new JLabel("Komentarze:");

        // Pole tekstowe do dodawania komentarzy
        JTextField komentarzField = new JTextField(60);
        JButton dodajKomentarz = new JButton("Dodaj komentarz");

        // Lista komentarzy
        DefaultListModel<String> commentsListModel = new DefaultListModel<>();
        JList<String> commentsList = new JList<>(commentsListModel);

        // Inicjalizacja listy komentarzy
        List<ArtystaKomentarze> listaKomentarzy = ar.getKomentarze(a.getSpotifyId());
        for (ArtystaKomentarze komentarz : listaKomentarzy) {
            commentsListModel.addElement(komentarz.getNazwaUzytkownika() + ": " + komentarz.getKomentarz());
        }

        dodajKomentarz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nowyKomentarz = komentarzField.getText();
                if (nowyKomentarz.isEmpty()) {
                    JOptionPane.showMessageDialog(mainScreenPanel,
                            "Komentarz nie może być pusty!",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ar.dodajKomentarz(a.getSpotifyId(),u.getNazwa(), nowyKomentarz);
                commentsListModel.addElement(u.getNazwa() + ": " + nowyKomentarz);
                komentarzField.setText(""); // Czyszczenie pola tekstowego
                JOptionPane.showMessageDialog(mainScreenPanel,
                        "Twój komentarz został dodany!",
                        "Komentarz dodany",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        commentsPanel.add(commentsLabel, BorderLayout.NORTH);
        commentsPanel.add(new JScrollPane(commentsList), BorderLayout.CENTER);

        JPanel addCommentPanel = new JPanel(new FlowLayout());
        addCommentPanel.add(new JLabel("Twój komentarz:"));
        addCommentPanel.add(komentarzField);
        addCommentPanel.add(dodajKomentarz);

        commentsPanel.add(addCommentPanel, BorderLayout.SOUTH);


        bottomPanel.add(ratingPanel, BorderLayout.NORTH);
        bottomPanel.add(commentsPanel, BorderLayout.CENTER);

        mainScreenPanel.add(label, BorderLayout.NORTH);

        mainScreenPanel.add(imageLabel, BorderLayout.CENTER);
        mainScreenPanel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Przyciski obok siebie z odstępem
        buttonPanel.add(backButton);
        buttonPanel.add(glowna);

        mainScreenPanel.add(buttonPanel, BorderLayout.NORTH);

        return mainScreenPanel;
    }

}