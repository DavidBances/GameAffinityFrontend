package com.gameaffinity.service;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class FriendshipServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api/friendships"; // URL de tu backend
    private final RestTemplate restTemplate;

    public FriendshipServiceAPI() {
        this.restTemplate = new RestTemplate();
    }

    // Crear encabezados con el token JWT (Authorization: Bearer <Token>)
    private HttpHeaders createHttpHeadersWithToken() {
        UserServiceAPI userServiceAPI = new UserServiceAPI();
        if (userServiceAPI.getToken() == null) {
            throw new IllegalStateException("JWT token is not available. User must be logged in.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userServiceAPI.getToken());
        return headers;
    }

    // Obtener email del usuario autenticado a partir del token JWT
    public String getEmailFromToken() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Obtener el ID del usuario autenticado a partir de su token
    public int getUserIdFromToken() {
        String email = getEmailFromToken();
        String url = BASE_URL + "/user-id?userEmail={email}";
        return restTemplate.getForObject(url, Integer.class, email); // El backend devuelve el ID del usuario
    }

    // Obtener lista de amigos del usuario por su ID
    public List<UserBase> getFriends() {
        String url = BASE_URL + "/friends/" + getUserIdFromToken();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<UserBase>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<UserBase>>() {
                });
        return response.getBody();
    }

    // Obtener las solicitudes de amistad pendientes del usuario autenticado
    public List<Friendship> getFriendRequestsByToken() {
        int userId = getUserIdFromToken(); // Obtener el ID del usuario autenticado
        String url = BASE_URL + "/requests/" + userId;

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Friendship>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Friendship>>() {
                });
        return response.getBody();
    }

    // Enviar solicitud de amistad
    public boolean sendFriendRequest(Friendship friendship) {
        String url = BASE_URL + "/request";

        HttpEntity<Friendship> entity = new HttpEntity<>(friendship, createHttpHeadersWithToken());
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    // Responder a una solicitud de amistad (Aceptar/Rechazar)
    public boolean respondToFriendRequest(Friendship friendship, String status) {
        String url = BASE_URL + "/respond?status=" + status;

        HttpEntity<Friendship> entity = new HttpEntity<>(friendship, createHttpHeadersWithToken());
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    // Obtener el ID de un usuario por su email (si no se usa el token para obtenerlo directamente)
    public int getUserIdByEmail(String email) {
        String url = BASE_URL + "/user-id?userEmail={email}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        return restTemplate.exchange(url, HttpMethod.GET, entity, Integer.class, email).getBody();
    }

    // Eliminar a un amigo
    public boolean deleteFriend(int friendId) {
        String url = BASE_URL + "/delete";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("userId", getUserIdFromToken())
                .queryParam("friendId", friendId)
                .toUriString();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.DELETE, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }
}