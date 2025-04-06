package org.example.serwisy;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class SpotifyAuth {
    private static final String CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("SPOTIFY_CLIENT_SECRET");
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";


    private static final String ARTIST_DETAILS = "https://api.spotify.com/v1/artists/";

    String access_token;


    public void authenticate() throws IOException {
        OkHttpClient client = new OkHttpClient();


        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));


        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();



                JSONObject jsonResponse = new JSONObject(responseBody);
                access_token = jsonResponse.getString("access_token");


                System.out.println("Access Token: " + access_token);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
    }



    public JSONObject getArtysta(String spotifyId) throws IOException {
        authenticate();

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(ARTIST_DETAILS + spotifyId)
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();


                return new JSONObject(responseBody);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
        return null;
    }




}
