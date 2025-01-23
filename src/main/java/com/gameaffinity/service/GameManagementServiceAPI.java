package com.gameaffinity.service;

import com.gameaffinity.model.Game;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    // Crear encabezados con el token JWT
    private HttpHeaders createHttpHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Obtener el token JWT del contexto de seguridad actual
        String jwtToken = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getCredentials()
                .toString();
        headers.setBearerAuth(jwtToken); // Agregar el token como "Bearer"
        return headers;
    }

    // Agregar un juego
    public boolean addGame(String name, String genre, String priceText) {
        String url = BASE_URL + "/add";

        // Construir la URL con los parámetros de consulta
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", name)
                .queryParam("genre", genre)
                .queryParam("priceText", priceText)
                .toUriString();

        // Crear una entidad HTTP con los encabezados que incluyen el token
        HttpEntity<Void> entity = new HttpEntity<>(createHttpHeadersWithToken());

        // Realiza la solicitud al backend (con método POST)
        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.POST, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    // Obtener todos los juegos
    public List<Game> getAllGames() {
        String url = BASE_URL + "/all";

        // Crear una entidad HTTP con los encabezados que incluyen el token
        HttpEntity<Void> entity = new HttpEntity<>(createHttpHeadersWithToken());

        // Realiza la solicitud al backend (con método GET)
        ResponseEntity<List<Game>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                }
        );
        return response.getBody();
    }

    // Eliminar un juego por su ID
    public boolean deleteGame(int gameId) {
        String url = BASE_URL + "/delete";

        // Construir la URL con el parámetro gameId
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("gameId", gameId)
                .toUriString();

        // Crear una entidad HTTP con los encabezados que incluyen el token
        HttpEntity<Void> entity = new HttpEntity<>(createHttpHeadersWithToken());

        // Realiza la solicitud al backend (con método DELETE)
        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.DELETE, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }
}