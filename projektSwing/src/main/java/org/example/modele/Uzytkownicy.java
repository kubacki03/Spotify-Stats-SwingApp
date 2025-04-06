package org.example.modele;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Uzytkownicy {


    public String nazwa;


    public LocalDate data_urodzin;

    public int id_konta;


    public String email;


    public String haslo;


    public String nazwaLastFm;
}
