package org.example.kontrolery;

import org.example.modele.Uzytkownicy;
import org.example.repozytoria.UzytkownicyRepository;

public class LoginController {

    public Uzytkownicy znajdzUzytkownika(String nazwa, String haslo){
            return UzytkownicyRepository.getUzytkonikByNazwaHaslo(nazwa, haslo);
        }

}
