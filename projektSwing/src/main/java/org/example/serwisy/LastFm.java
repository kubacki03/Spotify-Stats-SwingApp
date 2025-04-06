package org.example.serwisy;


import lombok.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.modele.Album;
import org.example.modele.ArtystaPopularnosc;
import org.example.modele.Piosenka;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LastFm {

    String API_KEY = System.getenv("LASTFM_KEY");
    String URL = "http://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=" + API_KEY + "&format=json&limit=10";
    String URL2 = "https://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=poland&api_key="+API_KEY+"&format=json&limit=10";
    String URL3=" http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&limit=10&api_key="+API_KEY+"&format=json";



    public ArrayList<String> getTopArtist() throws IOException {




        OkHttpClient client = new OkHttpClient();
        ArrayList<String> lista = new ArrayList<>();

        Request request = new Request.Builder()
                .url(URL3)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();


                JSONObject jsonResponse = new JSONObject(responseBody);


                JSONObject tracksObject = jsonResponse.getJSONObject("artists");
                JSONArray trackArray = tracksObject.getJSONArray("artist");


                for (int i = 0; i < trackArray.length(); i++) {
                    JSONObject track = trackArray.getJSONObject(i);


                    String trackName = track.getString("name");

                    lista.add(trackName);
                }

            } else {
                System.out.println("Request failed with code: " + response.code());
            }
            return lista;
        }
    }


    public ArrayList<ArtystaPopularnosc> getChartArtist(String nazwa) throws IOException {

        String URL_CHART_ARTIST = "http://ws.audioscrobbler.com/2.0/?method=user.getweeklyartistchart&limit=10&user=" + nazwa + "&api_key=" + API_KEY + "&format=json";

        OkHttpClient client = new OkHttpClient();
        ArrayList<ArtystaPopularnosc> lista = new ArrayList<>();

        Request request = new Request.Builder()
                .url(URL_CHART_ARTIST)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();


                JSONObject jsonResponse = new JSONObject(responseBody);


                JSONObject tracksObject = jsonResponse.getJSONObject("weeklyartistchart");
                JSONArray trackArray = tracksObject.getJSONArray("artist");


                for (int i = 0; i < trackArray.length(); i++) {
                    JSONObject track = trackArray.getJSONObject(i);
                    String artistName = track.getString("name");
                    long playcount = track.getLong("playcount");
                    String mbid=track.getString("mbid");
                    ArtystaPopularnosc a = new ArtystaPopularnosc(artistName, playcount,mbid);



                    lista.add(a);
                }

            } else {
                System.out.println("Error: " + response.code());
            }
            return lista;
        }
    }


    public String getBio(String id) {
        String URL_BIO_ARTIST = "https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&mbid=" + id + "&api_key=" + API_KEY + "&format=json";


        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(URL_BIO_ARTIST)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();


                return getString(responseBody);
            } else {
                System.out.println("Error: " + response.code());
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String getString(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);


        JSONObject artistObject = jsonResponse.getJSONObject("artist");


        JSONObject bioObject = artistObject.getJSONObject("bio");


        String bioSummary = bioObject.getString("content");


        bioSummary = bioSummary.replaceAll(". User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.", "").trim(); // Usuwa tagi <a> i zawartość między nimi
        bioSummary = bioSummary.replaceAll("<a.*?>.*?</a>", "").trim();
        return bioSummary;
    }


    public List<String> getSimillar(List<ArtystaPopularnosc> ap) {
        List<String> simillar = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        for (int i=0;i<3;i++) {
            ArtystaPopularnosc artystaPopularnosc = ap.get(i);
            String tekst;
            String URL3 = "http://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&mbid="
                    + artystaPopularnosc.getMbid() + "&api_key=" + API_KEY + "&format=json";

            Request request = new Request.Builder()
                    .url(URL3)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);

                    if (jsonResponse.has("error")) {

                        continue;
                    }

                    JSONObject similarartists = jsonResponse.getJSONObject("similarartists");
                    JSONArray similarartistsJSONArray = similarartists.getJSONArray("artist");

                    for(int j=0;j<3;j++){
                    JSONObject artist = similarartistsJSONArray.getJSONObject(j);
                    tekst = artist.getString("name");
                    simillar.add(tekst);}
                }
            } catch (IOException e) {
                System.out.println("Błąd podczas wykonania zapytania: " + e.getMessage());
            } catch (JSONException e) {
                System.out.println("Błąd podczas przetwarzania odpowiedzi JSON: " + e.getMessage());
            }
        }
        return simillar;
    }


    public ArrayList<ArtystaPopularnosc> getWrappedArtist(String nazwaLastFm) throws IOException {

        String URL_CHART_ARTIST = "http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user="+nazwaLastFm+"&api_key="+API_KEY+"&format=json&period=12month&limit=15";

        OkHttpClient client = new OkHttpClient();
        ArrayList<ArtystaPopularnosc> lista = new ArrayList<>();

        Request request = new Request.Builder()
                .url(URL_CHART_ARTIST)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();


                JSONObject jsonResponse = new JSONObject(responseBody);


                JSONObject topArtists = jsonResponse.getJSONObject("topartists");
                JSONArray artistArray = topArtists.getJSONArray("artist");


                for (int i = 0; i < artistArray.length(); i++) {
                    JSONObject artist = artistArray.getJSONObject(i);
                    String artistName = artist.getString("name");
                    long playcount = artist.getLong("playcount");
                    String mbid=artist.getString("mbid");
                    ArtystaPopularnosc a = new ArtystaPopularnosc(artistName, playcount,mbid);



                    lista.add(a);
                }

            } else {
                System.out.println("Error: " + response.code());
            }
            return lista;
        }
    }


    public ArrayList<Piosenka> getWrappedSong(String nazwaLastFm) throws IOException {

        String URL_CHART_ARTIST = "http://ws.audioscrobbler.com/2.0/?method=user.gettoptracks&user="+nazwaLastFm+"&api_key="+API_KEY+"&format=json&period=12month&limit=15";

        OkHttpClient client = new OkHttpClient();
        ArrayList<Piosenka> lista = new ArrayList<>();

        Request request = new Request.Builder()
                .url(URL_CHART_ARTIST)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();


                JSONObject jsonResponse = new JSONObject(responseBody);


                JSONObject tracksObject = jsonResponse.getJSONObject("toptracks");
                JSONArray trackArray = tracksObject.getJSONArray("track");


                for (int i = 0; i < trackArray.length(); i++) {
                    JSONObject track = trackArray.getJSONObject(i);
                    JSONObject artist = track.getJSONObject("artist");

                    String songName = track.getString("name");
                    long count= track.getLong("playcount");


                    String artistName = artist.getString("name");

                    Piosenka p = new Piosenka(artistName,songName,count);

                    lista.add(p);
                }

            } else {
                System.out.println("Error: " + response.code());
            }
            return lista;
        }
    }


    public ArrayList<Album> getWrappedAlbum(String nazwaLastFm) throws IOException {

        String URL_CHART_ARTIST = "https://ws.audioscrobbler.com/2.0/?method=user.gettopalbums&user="+nazwaLastFm+"&api_key="+API_KEY+"&format=json&period=12month&limit=15";

        OkHttpClient client = new OkHttpClient();
        ArrayList<Album> lista = new ArrayList<>();

        Request request = new Request.Builder()
                .url(URL_CHART_ARTIST)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();


                JSONObject jsonResponse = new JSONObject(responseBody);


                JSONObject tracksObject = jsonResponse.getJSONObject("topalbums");
                JSONArray trackArray = tracksObject.getJSONArray("album");


                for (int i = 0; i < trackArray.length(); i++) {
                    JSONObject alb = trackArray.getJSONObject(i);
                    JSONObject artist = alb.getJSONObject("artist");

                    String albumName = alb.getString("name");
                    long count= alb.getLong("playcount");


                    String artistName = artist.getString("name");

                    Album album = new Album(albumName,artistName,count);

                    lista.add(album);
                }

            } else {
                System.out.println("Error: " + response.code());
            }
            return lista;
        }
    }

}





