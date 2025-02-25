package com.gameaffinity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class FriendshipServiceAPI {

    private static final String BASE_URL = "http://localhost:8081/api/friendships"; // URL de tu backend
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

    // Obtener lista de amigos del usuario por su ID
    public List<UserBase> getFriends() {
        String url = BASE_URL + "/friends";

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
                new ParameterizedTypeReference<List<Friendship>>() {
                });
        return response.getBody();
    }

    public String sendFriendRequest(String friendEmail) {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    BASE_URL + "/request?friendEmail=" + friendEmail,
                    HttpMethod.POST,
                    new HttpEntity<>(createHttpHeadersWithToken()),
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );
            return (String) response.getBody().get("message");
        } catch (HttpClientErrorException.BadRequest e) {
            Map<String, Object> errorResponse = null;
            try {
                errorResponse = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            return (String) errorResponse.get("message");
        }
    }


    // Responder a una solicitud de amistad (Aceptar/Rechazar)
    public boolean respondToFriendRequest(Friendship friendship, String status) {
        String url = BASE_URL + "/respond?status=" + status;

        HttpEntity<Friendship> entity = new HttpEntity<>(friendship, createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.PUT, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }

    // Eliminar a un amigo
    public boolean deleteFriend(int friendId) {
        String url = BASE_URL + "/delete?friendId=" + friendId;

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }
}
