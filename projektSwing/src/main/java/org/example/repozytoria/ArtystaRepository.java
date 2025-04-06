package org.example.repozytoria;

import org.example.modele.Artysta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtystaRepository {

    private static final String DATABASE_URL = System.getenv("DATABASE_URL");
    private static final String USERNAME = "Projekt_callspoken";
    private static final String PASSWORD = System.getenv("DATABASE_PASSWORD");

    public  List<Artysta> getArtystaGatunek(String gatunek) {

        Connection connection1;

        try {
            connection1 = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Artysta> lista = new ArrayList<>();
        String query = "SELECT * FROM artysta a WHERE a.spotify_id IN (" +
                "SELECT ag.spotify_id_artysty FROM artysta_gatunek ag " +
                "JOIN gatunek g ON ag.id_gatunku = g.id_gatunku " +
                "WHERE LOWER(g.nazwa) = LOWER(?)) ";

        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            statement.setString(1, gatunek);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                Artysta artysta = new Artysta();
                artysta.setMbid(resultSet.getString("mbid"));
                artysta.setNazwa(resultSet.getString("nazwa"));
                artysta.setSpotifyId(resultSet.getString("spotify_id"));


                lista.add(artysta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }


    public Artysta findArtystaBySpotifyId(String id){
        Connection connection1;

        try {
            connection1 = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Artysta> lista = new ArrayList<>();
        String query = "SELECT * FROM artysta a WHERE a.spotify_id = ? ";
        Artysta artysta = new Artysta();
        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                artysta.setMbid(resultSet.getString("mbid"));
                artysta.setNazwa(resultSet.getString("nazwa"));
                artysta.setSpotifyId(resultSet.getString("spotify_id"));

                return artysta;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
