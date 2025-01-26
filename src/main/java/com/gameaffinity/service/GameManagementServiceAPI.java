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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameManagementServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api/games"; // URL del backend
    private final RestTemplate restTemplate;
    private final UserServiceAPI userServiceAPI;  // Inyección de dependencia de UserServiceAPI

    @Autowired
    public GameManagementServiceAPI(UserServiceAPI userServiceAPI) {
        this.restTemplate = new RestTemplate();
        this.userServiceAPI = userServiceAPI;  // Guardamos la referencia de UserServiceAPI
    }

    // Crear encabezados con el token JWT
    private HttpHeaders createHttpHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Obtener el token JWT desde UserServiceAPI
        String jwtToken = userServiceAPI.getToken();  // Obtienes el token del servicio centralizado
        if (jwtToken == null) {
            throw new IllegalStateException("Token no disponible. El usuario debe iniciar sesión.");
        }

        headers.setBearerAuth(jwtToken);  // Agregar el token como "Bearer"
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

        System.out.println(finalUrl);
        // Crear una entidad HTTP con los encabezados que incluyen el token
        HttpEntity<Void> entity = new HttpEntity<>(createHttpHeadersWithToken());

        // Realiza la solicitud al backend (con método POST)
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        System.out.println(response.getBody());
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
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

    // Obtener juegos del usuario por género
    public List<Game> getGamesByGenre(String genre) {
        String url = BASE_URL + "/genre?genre={genre}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                }, genre);
        return response.getBody();
    }

    // Obtener juegos del usuario por nombre
    public List<Game> getGamesByName(String name) {
        String url = BASE_URL + "/name?name={name}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                }, name);
        return response.getBody();
    }

    // Obtener juegos del usuario por género y nombre
    public List<Game> getGamesByGenreAndName(String genre, String name) {
        String url = BASE_URL + "/genre-and-name?genre={genre}&name={name}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Game>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Game>>() {
                }, genre, name);
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
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.DELETE, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }
}
