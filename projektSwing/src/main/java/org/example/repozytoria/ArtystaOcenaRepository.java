package org.example.repozytoria;


import org.example.modele.ArtystaOcena;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtystaOcenaRepository {
    private static final String DATABASE_URL = System.getenv("DATABASE_URL");
    private static final String USERNAME = "Projekt_callspoken";
    private static final String PASSWORD = System.getenv("DATABASE_PASSWORD");


    public List<ArtystaOcena> findArtystaOcenaBySpotifyidartysty(String idSpotify){
        Connection connection1;

        try {
            connection1 = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<ArtystaOcena> lista = new ArrayList<>();
        String query = "SELECT * FROM artysta_ocena a WHERE a.spotifyidartysty = ? ";

        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            statement.setString(1, idSpotify);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                ArtystaOcena artysta = new ArtystaOcena();
                artysta.setSpotifyidartysty(resultSet.getString("spotifyidartysty"));
                artysta.setOcena(resultSet.getInt("ocena"));
                artysta.setId(resultSet.getInt("id"));

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
        System.out.println("Lista "+lista);
        return lista;
    }


    public void save(ArtystaOcena artystaOcena) {
        Connection connection1;

        try {

            connection1 = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String query = "INSERT INTO artysta_ocena (spotifyidartysty, ocena) VALUES (?, ?)";
        try (PreparedStatement statement = connection1.prepareStatement(query)) {

            statement.setString(1, artystaOcena.getSpotifyidartysty());
            statement.setInt(2, artystaOcena.getOcena());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection1 != null) {
                    connection1.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

