

package org.example.repozytoria;

import org.example.modele.Uzytkownicy;

import java.sql.*;

public class UzytkownicyRepository  {

    private static final String DATABASE_URL = System.getenv("DATABASE_URL");
    private static final String USERNAME = "Projekt_callspoken";
    private static final String PASSWORD = System.getenv("DATABASE_PASSWORD");

    private Connection connection;



    public static Uzytkownicy getUzytkonikByNazwaHaslo(String nazwa, String haslo) {

        Connection connection1 = null;

        try {
            connection1 = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Uzytkownicy uzytkownik = null;
        String query = "SELECT * FROM uzytkownicy WHERE nazwa = ? and haslo =?";

        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            statement.setString(1, nazwa);
            statement.setString(2, haslo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                uzytkownik = new Uzytkownicy();
                uzytkownik.nazwa = resultSet.getString("nazwa");
                uzytkownik.data_urodzin = resultSet.getDate("data_urodzin").toLocalDate();
                uzytkownik.id_konta = resultSet.getInt("id_konta");
                uzytkownik.email = resultSet.getString("email");
                uzytkownik.haslo = resultSet.getString("haslo");
                uzytkownik.nazwaLastFm = resultSet.getString("nazwaLastFm");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uzytkownik;
    }
}
