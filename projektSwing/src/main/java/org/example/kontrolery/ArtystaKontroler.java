package org.example.kontrolery;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.modele.*;
import org.example.repozytoria.ArtystaKomentarzeRepository;
import org.example.repozytoria.ArtystaOcenaRepository;
import org.example.repozytoria.ArtystaRepository;

import org.example.serwisy.ImageService;
import org.example.serwisy.LastFm;
import org.example.serwisy.SpotifyAuth;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ArtystaKontroler {

    static SpotifyAuth spotifyAuth = new SpotifyAuth();
    static ObjectMapper objectMapper= new ObjectMapper();

    LastFm lastFm = new LastFm();

    private ImageService imageService =new ImageService();


    public List<ArtystaPopularnosc> pokazTopUzytkowniak(String nazwa) throws IOException {

        LastFm lf = new LastFm();
        return lf.getChartArtist(nazwa);
    }


    public static List<Artysta> znajdzArtystaGatunek(String gatunek) {
        ArtystaRepository repository = new ArtystaRepository();

        List<Artysta> lista = repository.getArtystaGatunek(gatunek);
        for(Artysta a : lista){
            System.out.println("Kont "+a.getNazwa());
        }
        return lista;


    }

    public  static List<String> getTopArtysci() throws IOException {
        LastFm lf = new LastFm();
        return lf.getTopArtist();
    }

    public ArtystaSpotify artystaSzczegoly(String spotifyId) throws IOException {


        JSONObject o = spotifyAuth.getArtysta(spotifyId);



        ArtystaSpotify a = objectMapper.readValue(o.toString(), ArtystaSpotify.class);





        return a;


    }

    public float getOcena(String id){
        ArtystaOcenaRepository repository = new ArtystaOcenaRepository();

        List<ArtystaOcena> a = repository.findArtystaOcenaBySpotifyidartysty(id);

        if(a.isEmpty()){
            return 0;
        }

        int sumaOcen = a.stream()
                .mapToInt(ArtystaOcena::getOcena)
                .sum();


        float srednia = (float) sumaOcen / a.size();
        System.out.println("Ocena to "+srednia+" Suma to "+sumaOcen+" ile "+a.size());
        return srednia;
    }

    public void ocenArtyste(String spotifyId, int ocena){
        ArtystaOcenaRepository repository = new ArtystaOcenaRepository();
        ArtystaOcena artystaOcena =new ArtystaOcena(spotifyId,ocena,1);
        repository.save(artystaOcena);
    }

    public void dodajKomentarz(String spotifyidartysty, String nazwaKonta, String komentarz){
        ArtystaKomentarze artystaKomentarze = new ArtystaKomentarze(spotifyidartysty,nazwaKonta,komentarz,1);
        ArtystaKomentarzeRepository artystaKomentarzeRepository = new ArtystaKomentarzeRepository();
        artystaKomentarzeRepository.save(artystaKomentarze);


    }

    public List<ArtystaKomentarze> getKomentarze(String spotifyidartysty){
        ArtystaKomentarzeRepository artystaKomentarzeRepository = new ArtystaKomentarzeRepository();

        return artystaKomentarzeRepository.getKomentarze(spotifyidartysty);

    }

    public List<String> getSimillar(List<ArtystaPopularnosc> lista){
        LastFm lastFm1 = new LastFm();
        return lastFm1.getSimillar(lista);


    }







}
