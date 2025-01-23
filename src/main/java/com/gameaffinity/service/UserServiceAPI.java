package com.gameaffinity.service;

import com.gameaffinity.model.UserBase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class UserServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api";
    private final RestTemplate restTemplate;
    private String jwtToken;

    public UserServiceAPI() {
        this.restTemplate = new RestTemplate();
    }

    public String login(String email, String password) {
        String url = BASE_URL + "/login";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("email", email)
                .queryParam("password", password)
                .toUriString();
        ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.POST, null, String.class);
        this.jwtToken = response.getBody();
        return this.jwtToken;
    }

    private HttpHeaders createHttpHeadersWithToken() {
        if (this.jwtToken == null) {
            throw new IllegalStateException("JWT token is not available. User must be logged in.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        return headers;
    }


    // Método para registrar un usuario (no requiere token)
    public String register(String name, String email, String password) {
        String url = BASE_URL + "/register";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", name)
                .queryParam("email", email)
                .queryParam("password", password)
                .toUriString();
        ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.POST, null, String.class);
        return response.getBody();
    }

    // Actualizar perfil del usuario autenticado (email tomado directamente del token)
    public boolean updateProfile(String password, String newName, String newEmail, String newPassword) {
        // Eliminar parámetros de email, ya no se pasa porque se toma del token
        String url = BASE_URL + "/users/update-profile";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("password", password)
                .queryParam("newName", newName != null ? newName : "")
                .queryParam("newEmail", newEmail != null ? newEmail : "")
                .queryParam("newPassword", newPassword != null ? newPassword : "")
                .toUriString();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    // Actualizar el rol de un usuario (requiere token de usuario con permisos de administrador)
    public boolean updateUserRole(UserBase user, String newRole) {
        String url = BASE_URL + "/user-management/update-role?newRole={newRole}";

        HttpEntity<UserBase> entity = new HttpEntity<>(user, createHttpHeadersWithToken());
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class, newRole);
        return response.getBody() != null && response.getBody();
    }

    // Obtener todos los usuarios (requiere token de usuario con permisos de administrador)
    public List<UserBase> getAllUsers() {
        String url = BASE_URL + "/user-management/all";

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<List<UserBase>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<UserBase>>() {
                });
        return response.getBody();
    }

    // Eliminar un usuario (requiere token de usuario con permisos de administrador)
    public boolean deleteUser(UserBase user) {
        String url = BASE_URL + "/user-management/delete";

        HttpEntity<UserBase> entity = new HttpEntity<>(user, createHttpHeadersWithToken());
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    public String getRole() {
        String url = BASE_URL + "/users/getRole";
        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}