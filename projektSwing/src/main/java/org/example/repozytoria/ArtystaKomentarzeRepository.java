package org.example.repozytoria;


import org.example.modele.ArtystaKomentarze;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtystaKomentarzeRepository {


    private static final String DATABASE_URL = System.getenv("DATABASE_URL");
    private static final String USERNAME = "Projekt_callspoken";
    private static final String PASSWORD = System.getenv("DATABASE_PASSWORD");


    public void save(ArtystaKomentarze artystaKomentarze) {
        Connection connection1;

        try {
            connection1 = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "INSERT INTO artystaKomentarze (spotifyidartysty, nazwa_uzytkownika ,komentarz) VALUES (?, ?,?)";
        try (PreparedStatement statement = connection1.prepareStatement(query)) {

            statement.setString(1, artystaKomentarze.getSpotifyidartysty());
            statement.setString(2, artystaKomentarze.getNazwaUzytkownika());
            statement.setString(3,artystaKomentarze.getKomentarz());


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


    public List<ArtystaKomentarze> getKomentarze(String spotifyidartysty) {

        Connection connection1;

        try {
            connection1 = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<ArtystaKomentarze> lista = new ArrayList<>();
        String query = "SELECT * FROM artystaKomentarze a where a.spotifyidartysty = ?";

        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            statement.setString(1, spotifyidartysty);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                ArtystaKomentarze artystaKomentarze = new ArtystaKomentarze();
                artystaKomentarze.setKomentarz(resultSet.getString("komentarz"));
                artystaKomentarze.setSpotifyidartysty(resultSet.getString("spotifyidartysty"));
                artystaKomentarze.setNazwaUzytkownika(resultSet.getString("nazwa_uzytkownika"));

                artystaKomentarze.setId(resultSet.getInt("id"));



                lista.add(artystaKomentarze);
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
}
