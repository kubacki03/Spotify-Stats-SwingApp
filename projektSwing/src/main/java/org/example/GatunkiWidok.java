package org.example;

import org.example.kontrolery.ArtystaKontroler;
import org.example.modele.Artysta;
import org.example.modele.Uzytkownicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GatunkiWidok {

    static List<Artysta> artysciPop = new ArrayList<>();
    static List<Artysta> artysciRock = new ArrayList<>();
    static List<Artysta> artysciMetal = new ArrayList<>();

    public static JPanel widok(CardLayout cardLayout, JPanel mainPanel, Uzytkownicy u) {



        JPanel mainScreenPanel = new JPanel(new BorderLayout(10, 10));
        mainScreenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton rockButton = new JButton("Rock");
        JButton popButton = new JButton("Pop");
        JButton metalButton = new JButton("Metal");



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
        buttonsPanel.add(backButton,BorderLayout.WEST);



        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        rockButton.setFont(buttonFont);
        popButton.setFont(buttonFont);
        metalButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
        buttonsPanel.add(rockButton);
        buttonsPanel.add(popButton);
        buttonsPanel.add(metalButton);


        JPanel artistsPanel = new JPanel();
        artistsPanel.setLayout(new BoxLayout(artistsPanel, BoxLayout.Y_AXIS));
        artistsPanel.setBorder(BorderFactory.createTitledBorder("Lista Artystów"));

        JScrollPane scrollPane = new JScrollPane(artistsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));


        mainScreenPanel.add(buttonsPanel, BorderLayout.NORTH);
        mainScreenPanel.add(scrollPane, BorderLayout.CENTER);




        rockButton.addActionListener(e -> pokazArtystow(artysciRock, artistsPanel, cardLayout, mainPanel, u));
        popButton.addActionListener(e -> pokazArtystow(artysciPop, artistsPanel, cardLayout, mainPanel, u));
        metalButton.addActionListener(e -> pokazArtystow(artysciMetal, artistsPanel, cardLayout, mainPanel, u));

        return mainScreenPanel;
    }

    static void wczytajDane() {
        artysciPop = ArtystaKontroler.znajdzArtystaGatunek("pop");
        artysciRock = ArtystaKontroler.znajdzArtystaGatunek("rock");
        artysciMetal = ArtystaKontroler.znajdzArtystaGatunek("metal");
    }

    private static void pokazArtystow(List<Artysta> artysci, JPanel artistsPanel, CardLayout cardLayout, JPanel mainPanel, Uzytkownicy u) {

        artistsPanel.removeAll();


        Font artistButtonFont = new Font("Arial", Font.PLAIN, 14);
        Dimension buttonSize = new Dimension(200, 40);

        for (Artysta artysta : artysci) {
            JButton artistButton = new JButton(artysta.getNazwa());
            artistButton.setFont(artistButtonFont);
            artistButton.setPreferredSize(buttonSize);
            artistButton.setMaximumSize(buttonSize);
            artistButton.setAlignmentX(Component.CENTER_ALIGNMENT);


            artistButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    animateButtonFadeOut(artistButton, () -> {

                        StronaArtysty stronaArtysty = new StronaArtysty();
                        JPanel artystaPanel = null;
                        try {
                            artystaPanel = stronaArtysty.widok(cardLayout, mainPanel, artysta, u);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        mainPanel.add(artystaPanel, "StronaArtysty");
                        cardLayout.show(mainPanel, "StronaArtysty");
                    });
                }
            });

            artistsPanel.add(artistButton);
            artistsPanel.add(Box.createVerticalStrut(10));
        }


        artistsPanel.revalidate();
        artistsPanel.repaint();
    }

    private static void animateButtonFadeOut(JButton button, Runnable onComplete) {
        Timer timer = new Timer(140, null);
        final float[] blueIntensity = {0.0f};

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blueIntensity[0] += 0.1f;
                if (blueIntensity[0] >= 1.0f) {
                    blueIntensity[0] = 1.0f;
                    timer.stop();
                    onComplete.run();
                }


                Color newColor = new Color(0.0f, 0.0f, blueIntensity[0]);
                button.setBackground(newColor); // Ustaw nowy kolor tła


                button.setForeground(Color.WHITE);

                button.repaint();
            }
        });
        timer.start();
    }


}