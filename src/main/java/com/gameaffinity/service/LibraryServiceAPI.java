package com.gameaffinity.service;

import com.gameaffinity.model.Game;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class LibraryServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api/library"; // URL de tu backend

    private final RestTemplate restTemplate;

    public LibraryServiceAPI() {
        this.restTemplate = new RestTemplate();
    }

    public List<String> getAllGenres() {
        String url = BASE_URL + "/genres";
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
        });
        return response.getBody();
    }

    public List<Game> getGamesByUserId(int userId) {
        String url = BASE_URL + "/user/" + userId;
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Game>>() {
        });
        return response.getBody();
    }

    public List<Game> getGamesByNameUser(int userId, String name) {
        String url = BASE_URL + "/user/" + userId + "/name?name={name}";
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Game>>() {
        }, name);
        return response.getBody();
    }

    public List<Game> getGamesByGenreUser(int userId, String genre) {
        String url = BASE_URL + "/user/" + userId + "/genre?genre={genre}";
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Game>>() {
        }, genre);
        return response.getBody();
    }

    public List<Game> getGamesByGenreAndNameUser(int userId, String genre, String name) {
        String url = BASE_URL + "/user/" + userId;
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("genre", genre)
                .queryParam("name", name)
                .toUriString();
        ResponseEntity<List<Game>> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Game>>() {
        });
        return response.getBody();
    }

    public List<Game> getAllGames() {
        String url = BASE_URL + "/all";
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Game>>() {
        });
        return response.getBody();
    }

    public boolean addGameToLibrary(int userId, String gameName) {
        String url = BASE_URL + "/add";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("userId", userId)
                .queryParam("gameName", gameName)
                .toUriString();
        return restTemplate.postForObject(finalUrl, null, Boolean.class);
    }

    public boolean updateGameState(int gameId, int userId, String newState) {
        String url = BASE_URL + "/update/state";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("gameId", gameId)
                .queryParam("userId", userId)
                .queryParam("newState", newState)
                .toUriString();
        HttpEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, null, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    public boolean updateGameScore(int gameId, int userId, Integer newScore) {
        String url = BASE_URL + "/update/score";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("gameId", gameId)
                .queryParam("userId", userId)
                .queryParam("score", newScore)
                .toUriString();
        HttpEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, null, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    public boolean removeGameFromLibrary(int userId, int gameId) {
        String url = BASE_URL + "/remove";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("userId", userId)
                .queryParam("gameId", gameId)
                .toUriString();
        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.DELETE, null, Boolean.class);
        return response.getBody() != null && response.getBody();
    }
}
