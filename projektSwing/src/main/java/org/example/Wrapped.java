package org.example;

import org.example.kontrolery.WrappedKontroler;
import org.example.modele.Album;
import org.example.modele.ArtystaPopularnosc;
import org.example.modele.Piosenka;
import org.example.modele.Uzytkownicy;
import org.example.serwisy.InternetTest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Wrapped {
    public static List<ArtystaPopularnosc> artystaPopularnosc ;
    public static List<Piosenka> piosenka ;
    public static List<Album> album ;
    public static JPanel widok(CardLayout cardLayout, JPanel mainPanel, Uzytkownicy u) throws IOException {

        JPanel mainScreenPanel = new JPanel(new BorderLayout(10, 10));
        mainScreenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JLabel headerLabel = new JLabel("Twój Wrapped", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainScreenPanel.add(headerLabel, BorderLayout.NORTH);


        String[] columnNames = {"Artysta", "Piosenka", "Album"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        int rows = Math.min(Math.min(artystaPopularnosc.size(), piosenka.size()), album.size());
        for (int i = 0; i < rows; i++) {
            String artystaNazwa = artystaPopularnosc.get(i).getNazwa();
            String piosenkaNazwa = piosenka.get(i).getSongName();
            String albumNazwa = album.get(i).getAlbumName();
            tableModel.addRow(new Object[]{artystaNazwa, piosenkaNazwa, albumNazwa});
        }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 250));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Podsumowanie Wrapped"));

        mainScreenPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
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
        buttonPanel.add(backButton);




        mainScreenPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainScreenPanel;
    }
}
