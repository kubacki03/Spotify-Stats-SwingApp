package org.example;

import kotlin.Pair;
import lombok.extern.java.Log;
import org.example.kontrolery.ArtystaKontroler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Formularz logowania");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 768); 
        CardLayout cardLayout = new CardLayout();
        GatunkiWidok.wczytajDane();
        PowitalnyWidok.listaTopPiosenkarzy= ArtystaKontroler.getTopArtysci();
        Logowanie logowanie = new Logowanie();
        logowanie.widok();
        }

    }
