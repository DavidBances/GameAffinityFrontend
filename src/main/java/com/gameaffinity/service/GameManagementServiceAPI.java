package com.gameaffinity.service;

import com.gameaffinity.model.Game;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class GameManagementServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api/games"; // URL del backend

    private final RestTemplate restTemplate;

    public GameManagementServiceAPI() {
        this.restTemplate = new RestTemplate(); // Inicializa RestTemplate
    }

    public boolean addGame(String name, String genre, String priceText) {
        String url = BASE_URL + "/add";

        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", name)
                .queryParam("genre", genre)
                .queryParam("priceText", priceText)
                .toUriString();

        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.POST, null, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    public List<Game> getAllGames() {
        ResponseEntity<List<Game>> response = restTemplate.exchange(
                BASE_URL + "/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Game>>() {
                });
        return response.getBody();
    }

    public boolean deleteGame(int gameId) {
        String url = BASE_URL + "/delete?gameId={gameId}";
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Boolean.class, gameId);
        return response.getBody() != null && response.getBody();
    }
}

