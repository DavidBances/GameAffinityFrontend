package com.gameaffinity.service;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class FriendshipServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api/friendships"; // URL de tu backend

    private final RestTemplate restTemplate;

    public FriendshipServiceAPI() {
        this.restTemplate = new RestTemplate();
    }

    // Obtener amigos de un usuario por su ID
    public List<UserBase> getFriends(int userId) {
        String url = BASE_URL + "/friends/" + userId;
        ResponseEntity<List<UserBase>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<UserBase>>() {
                });
        return response.getBody();
    }

    // Obtener las solicitudes de amistad pendientes por ID de usuario
    public List<Friendship> getFriendRequests(int userId) {
        String url = BASE_URL + "/requests/" + userId;
        ResponseEntity<List<Friendship>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Friendship>>() {
                });
        return response.getBody();
    }

    // Responder a una solicitud de amistad pasando el objeto Friendship completo
    public boolean respondToFriendRequest(Friendship friendship, String status) {
        String url = BASE_URL + "/respond?status=" + status;
        HttpEntity<Friendship> entity = new HttpEntity<>(friendship);
        HttpEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }


    // Obtener el ID de usuario por su email
    public int getUserIdByEmail(String email) {
        String url = BASE_URL + "/user-id?userEmail={email}";
        return restTemplate.getForObject(url, Integer.class, email);
    }

    // Enviar solicitud de amistad pasando el objeto Friendship
    public boolean sendFriendRequest(Friendship friendship) {
        String url = BASE_URL + "/request";
        HttpEntity<Friendship> entity = new HttpEntity<>(friendship);
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    // Eliminar amigo
    public boolean deleteFriend(int userId, int friendId) {
        String url = BASE_URL + "/delete";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("userId", userId)
                .queryParam("friendId", friendId)
                .toUriString();
        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.DELETE, null, Boolean.class);
        return response.getBody() != null && response.getBody();
    }
}
