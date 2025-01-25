package com.gameaffinity.service;

import com.gameaffinity.model.Game;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LibraryServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api/library"; // Base URL del backend
    private final RestTemplate restTemplate;
    private final UserServiceAPI userServiceAPI; // Inyección de dependencia para UserServiceAPI

    @Autowired
    public LibraryServiceAPI(UserServiceAPI userServiceAPI) {
        this.restTemplate = new RestTemplate();
        this.userServiceAPI = userServiceAPI; // Guardamos la referencia de UserServiceAPI
    }

    // Crear encabezados con el token JWT
    private HttpHeaders createHttpHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Obtener el token JWT desde UserServiceAPI
        String jwtToken = userServiceAPI.getToken(); // Obtienes el token desde el servicio centralizado
        System.out.println("Token JWT: " + jwtToken);
        if (jwtToken == null) {
            throw new IllegalStateException("Token no disponible. El usuario debe iniciar sesión.");
        }

        headers.setBearerAuth(jwtToken); // Agregar el token como "Bearer"
        return headers;
    }

    // Obtener todos los géneros
    public List<String> getAllGenres() {
        String url = BASE_URL + "/genres";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<String>>() {
                });
        return response.getBody();
    }

    // Obtener juegos del usuario autenticado
    public List<Game> getAllGamesByUser() {
        String url = BASE_URL + "/user";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                });
        System.out.println(response.getBody());
        return response.getBody();
    }

    // Obtener biblioteca de un amigo usando su userId
    public List<Game> getAllGamesByFriend(int friendId) {
        String url = BASE_URL + "/friendId?friendId=" + friendId;

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                });
        return response.getBody();
    }

    // Obtener juegos del usuario por género
    public List<Game> getGamesByGenreUser(String genre) {
        String url = BASE_URL + "/user/genre?genre={genre}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                }, genre);
        return response.getBody();
    }

    // Obtener juegos del usuario por nombre
    public List<Game> getGamesByNameUser(String name) {
        String url = BASE_URL + "/user/name?name={name}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                }, name);
        return response.getBody();
    }

    // Obtener juegos del usuario por género y nombre
    public List<Game> getGamesByGenreAndNameUser(String genre, String name) {
        String url = BASE_URL + "/user/genre-and-name?genre={genre}&name={name}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                }, genre, name);
        return response.getBody();
    }

    // Obtener la puntuación promedio de un juego
    public int getGameScore(int gameId) {
        String url = BASE_URL + "/avgScore/{gameId}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Integer.class, gameId);
        return response.getBody();
    }

    // Añadir juego a la biblioteca
    public boolean addGameToLibrary(String gameName) {
        String url = BASE_URL + "/add";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("gameName", gameName)
                .toUriString();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }

    // Actualizar el estado del juego
    public boolean updateGameState(int gameId, String newState) {
        String url = BASE_URL + "/update/state";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("gameId", gameId)
                .queryParam("newState", newState)
                .toUriString();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }

    // Actualizar la puntuación del juego
    public boolean updateGameScore(int gameId, int score) {
        String url = BASE_URL + "/update/score";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("gameId", gameId)
                .queryParam("score", score)
                .toUriString();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }

    // Eliminar un juego de la biblioteca
    public boolean removeGameFromLibrary(int gameId) {
        String url = BASE_URL + "/remove";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("gameId", gameId)
                .toUriString();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.DELETE, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }
}
