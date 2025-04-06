package org.example.kontrolery;

import lombok.Getter;
import lombok.Setter;
import org.example.modele.Uzytkownicy;

public class Sesja {
    private static Uzytkownicy zalogowanyUzytkownik;

    public static Uzytkownicy getZalogowanyUzytkownik() {
        return zalogowanyUzytkownik;
    }

    public static void setZalogowanyUzytkownik(Uzytkownicy uzytkownik) {
        zalogowanyUzytkownik = uzytkownik;
    }
}

