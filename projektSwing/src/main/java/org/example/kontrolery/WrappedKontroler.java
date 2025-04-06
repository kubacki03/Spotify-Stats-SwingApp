package org.example.kontrolery;

import lombok.Getter;
import lombok.Setter;
import org.example.modele.Album;
import org.example.modele.ArtystaPopularnosc;
import org.example.modele.Piosenka;
import org.example.serwisy.LastFm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class WrappedKontroler {


    public List<Object> getWrappedByUser(String nameLastFm) throws IOException {
        LastFm lastFm = new LastFm();

        List<Object > lista = new ArrayList<>();


        List<ArtystaPopularnosc> listArtists = lastFm.getWrappedArtist(nameLastFm);
       List<Album> listAlbums=lastFm.getWrappedAlbum(nameLastFm);
        List<Piosenka> listSongs=lastFm.getWrappedSong(nameLastFm);

        lista.add(listArtists);
        lista.add(listAlbums);
        lista.add(listSongs);

        return lista;
    }
}
