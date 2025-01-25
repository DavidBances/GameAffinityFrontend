package com.gameaffinity.service;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FriendshipServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api/friendships"; // URL de tu backend
    private final RestTemplate restTemplate;
    private final UserServiceAPI userServiceAPI;

    @Autowired
    public FriendshipServiceAPI(UserServiceAPI userServiceAPI) {
        this.restTemplate = new RestTemplate();
        this.userServiceAPI = userServiceAPI;
    }

    // Crear encabezados con el token JWT (Authorization: Bearer <Token>)
    private HttpHeaders createHttpHeadersWithToken() {
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
        return userServiceAPI.getEmailFromToken();  // Obtiene el email del token JWT
    }

    // Obtener el ID del usuario autenticado a partir de su token
    public int getUserIdFromToken() {
        return userServiceAPI.getUserIdFromToken(); // El backend devuelve el ID del usuario
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

    public List<Friendship> getFriendRequestsByToken() {
        String url = BASE_URL + "/requests";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<Friendship>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Friendship>>() {});
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
        String url = BASE_URL + "/friend-id?friendEmail={email}";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        return restTemplate.exchange(url, HttpMethod.GET, entity, Integer.class, email).getBody();
    }

    // Eliminar a un amigo
    public boolean deleteFriend(int friendId) {
        String url = BASE_URL + "/delete?friendId=" + friendId;

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        return response.getStatusCode().is2xxSuccessful();
    }

}
