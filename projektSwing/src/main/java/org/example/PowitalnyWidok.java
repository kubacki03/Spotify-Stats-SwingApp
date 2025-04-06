package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.example.kontrolery.ArtystaKontroler;
import org.example.kontrolery.Ustawienia;
import org.example.kontrolery.WrappedKontroler;
import org.example.modele.Album;
import org.example.modele.ArtystaPopularnosc;
import org.example.modele.Piosenka;
import org.example.modele.Uzytkownicy;
import org.example.serwisy.InternetTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
public class PowitalnyWidok {
    public static List<String> listaTopPiosenkarzy ;
    public static  List<ArtystaPopularnosc> listaTopUzytkownika;
public static List<String> simillar;
    public static JPanel widok(CardLayout cardLayout, JPanel mainPanel, Uzytkownicy u) throws IOException {


        JPanel mainScreenPanel = new JPanel(new BorderLayout(10, 10));


        JLabel welcomeLabel = new JLabel("Witaj, " + u.getNazwaLastFm() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainScreenPanel.add(welcomeLabel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton gatunki = new JButton("Artysci");
        JButton wrapped = new JButton("Wrapped");
        JButton wyloguj = new JButton("Wyloguj");
        JButton motyw= new JButton("Zmien motyw");

        gatunki.addActionListener(e -> {
            JPanel gatunkiPanel = GatunkiWidok.widok(cardLayout, mainPanel, u);
            mainPanel.add(gatunkiPanel, "Gatunki");
            cardLayout.show(mainPanel, "Gatunki");
        });

        wrapped.addActionListener(e -> {
            try {
                JPanel wrappedPanel = Wrapped.widok(cardLayout, mainPanel, u);
                mainPanel.add(wrappedPanel, "Wrapped");
                cardLayout.show(mainPanel, "Wrapped");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        wyloguj.addActionListener(e -> {
            JPanel loginPanel = Logowanie.widok();
            mainPanel.add(loginPanel, "Login");
            cardLayout.show(mainPanel, "Login");
        });

        motyw.addActionListener(e -> {
            try {
                if (Ustawienia.getMotyw()) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    Ustawienia.jasny = false;
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    Ustawienia.jasny = true;
                }

                SwingUtilities.updateComponentTreeUI(mainPanel);
                mainPanel.repaint();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });



        buttonPanel.add(wrapped);
        buttonPanel.add(gatunki);
        buttonPanel.add(wyloguj);
        buttonPanel.add(motyw);

        mainScreenPanel.add(buttonPanel, BorderLayout.SOUTH);

        ArtystaKontroler ar = new ArtystaKontroler();

        if(listaTopUzytkownika == null){
         listaTopUzytkownika = ar.pokazTopUzytkowniak(u.getNazwaLastFm());}



        // Panel tabel
        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel podobniPanel = new JPanel(new BorderLayout());
        JPanel glowna = new JPanel(new GridLayout(2, 1, 10, 10));
        glowna.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

// Panel tabel
        tablePanel.setPreferredSize(new Dimension(mainScreenPanel.getWidth() * 2 / 3, mainScreenPanel.getHeight())); // 2/3 szerokości ekranu
// Panel podobnych artystów
        podobniPanel.setPreferredSize(new Dimension(mainScreenPanel.getWidth() / 3, mainScreenPanel.getHeight())); // 1/3 szerokości ekranu

        // Tabela Top Użytkownika
        String[] columnNamesUzytkownika = {"Nazwa", "Popularność"};
        DefaultTableModel tableModelUzytkownika = new DefaultTableModel(columnNamesUzytkownika, 0);
        listaTopUzytkownika.forEach(artysta -> tableModelUzytkownika.addRow(new Object[]{artysta.getNazwa(), artysta.getPopularnosc()}));
        JTable tableUzytkownika = new JTable(tableModelUzytkownika);
        JScrollPane scrollPaneUzytkownika = new JScrollPane(tableUzytkownika);
        scrollPaneUzytkownika.setBorder(BorderFactory.createTitledBorder("Twoi ulubieni artyści"));

        // Tabela Top Piosenkarzy
        String[] columnNamesPiosenkarzy = {"Nazwa"};
        DefaultTableModel tableModelPiosenkarzy = new DefaultTableModel(columnNamesPiosenkarzy, 0);
        listaTopPiosenkarzy.forEach(artysta -> tableModelPiosenkarzy.addRow(new Object[]{artysta}));
        JTable tablePiosenkarzy = new JTable(tableModelPiosenkarzy);
        JScrollPane scrollPanePiosenkarzy = new JScrollPane(tablePiosenkarzy);
        scrollPanePiosenkarzy.setBorder(BorderFactory.createTitledBorder("Najpopularniejsi artyści"));

        if(simillar==null){
         simillar = ar.getSimillar(listaTopUzytkownika);}

        JButton podobniButton = new JButton("Kliknij by zobaczyć poleconych");


        JLabel podobniLabel = new JLabel("Poleceni artyści: ", SwingConstants.CENTER);
        // Panel do przechowywania podobnych artystów

        podobniPanel.add(podobniLabel, BorderLayout.CENTER);




        AtomicInteger currentIndex = new AtomicInteger(0);


        podobniButton.addActionListener(e -> {
            if (currentIndex.get() < simillar.size()) {
                String nextArtist = simillar.get(currentIndex.getAndIncrement());
                podobniLabel.setText("Podobny artysta: " + nextArtist);
                podobniButton.setText("Podobny artysta: " + nextArtist);
                System.out.println("Podobny artysta: " + nextArtist);
                SwingUtilities.updateComponentTreeUI(mainPanel);
                mainPanel.repaint(); // Dodatkowe odświeżenie wizualne
            } else {
                podobniLabel.setText("Brak więcej artystów.");
                currentIndex.set(0); // Opcjonalne - zresetuj indeks, jeśli chcesz zacząć od początku
                SwingUtilities.updateComponentTreeUI(mainPanel);
                mainPanel.repaint(); // Dodatkowe odświeżenie wizualne
            }
        });





        tablePanel.add(scrollPaneUzytkownika);
        tablePanel.add(scrollPanePiosenkarzy);
podobniPanel.add(podobniButton);
        glowna.add(tablePanel,BorderLayout.NORTH);
        glowna.add(podobniPanel,BorderLayout.SOUTH);
        mainScreenPanel.add(glowna);

        // Sprawdzanie połączenia internetowego w osobnym wątku
        ExecutorService executor = Executors.newSingleThreadExecutor();
        AtomicInteger x = new AtomicInteger(0);
        executor.submit(() -> {

            WrappedKontroler wrappedKontroler = new WrappedKontroler();
            List<Object> lista = null;
            try {
                lista = wrappedKontroler.getWrappedByUser(u.getNazwaLastFm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Wrapped.artystaPopularnosc= (List<ArtystaPopularnosc>) lista.get(0);
            Wrapped.piosenka = (List<Piosenka>) lista.get(2);
            Wrapped.album = (List<Album>) lista.get(1);



        });

        return mainScreenPanel;
    }
}
