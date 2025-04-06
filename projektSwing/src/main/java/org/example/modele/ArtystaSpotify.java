package org.example.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtystaSpotify {
    private List<ImageData> images; // Nowa klasa do reprezentacji danych obrazu
    private Followers followers;
    private List<String> genres;
    private int popularity;
    private String name;
    private String href;
    private String id;
    private String type;
    private Map<String, String> externalUrls;
    private String uri;
}
