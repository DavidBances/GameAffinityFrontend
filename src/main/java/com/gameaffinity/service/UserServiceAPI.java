package com.gameaffinity.service;

import com.gameaffinity.model.UserBase;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceAPI {

    private String token;  // Almacena el token JWT
    private static final String BASE_URL = "http://localhost:8081/api";  // Asegúrate de que esta URL sea correcta
    private final RestTemplate restTemplate;

    public UserServiceAPI() {
        this.restTemplate = new RestTemplate();
    }

    // Getter y setter del token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Método para hacer login y obtener el token
    public String login(String email, String password) {
        String url = BASE_URL + "/login";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("email", email)
                .queryParam("password", password)
                .toUriString();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.POST, null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        this.token = (String) response.getBody().get("token");  // Almacenar el token
        return this.token;
    }

    public String getEmailFromToken() {
        if (token == null) {
            throw new IllegalStateException("Token no disponible. El usuario debe iniciar sesión.");
        }

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido o corrupto.");
        }
    }

    public int getUserIdFromToken() {
        if (token == null) {
            throw new IllegalStateException("Token no disponible.");
        }

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt(); // Suponiendo que el token tiene un claim "userId"
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido o corrupto.");
        }
    }

    public String getRoleFromToken() {
        if (token == null) {
            throw new IllegalStateException("Token no disponible.");
        }

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roles").asList(String.class).getFirst();  // Suponiendo que el token tiene un claim llamado "role"
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido o corrupto.");
        }
    }


    // Método para crear los headers con el token
    private HttpHeaders createHttpHeadersWithToken() {
        if (this.token == null) {
            throw new IllegalStateException("JWT token is not available. User must be logged in.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);  // Configura el token en la cabecera Authorization
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
        return response.getBody() != null ? response.getBody() : "El resultado ha sido null";
    }

    public boolean updateProfile(String newName, String newEmail, String newPassword) {
        String url = BASE_URL + "/users/update-profile";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("newName", newName != null ? newName : "")
                .queryParam("newEmail", newEmail != null ? newEmail : "")
                .queryParam("newPassword", newPassword != null ? newPassword : "")
                .toUriString();

        HttpEntity<Object> entity = new HttpEntity<>(createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        // Obtener el valor booleano del mapa
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }


    public boolean updateUserRole(UserBase user, String newRole) {
        String url = BASE_URL + "/user-management/update-role?newRole={newRole}";
        HttpEntity<UserBase> entity = new HttpEntity<>(user, createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.PUT, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        }, newRole);
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }


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
        String url = BASE_URL + "/user-management/delete?userId={userId}";

        int userId = user.getId();

        HttpEntity<UserBase> entity = new HttpEntity<>(user, createHttpHeadersWithToken());
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        }, userId);
        Boolean success = (Boolean) response.getBody().get("success");
        return success != null && success;
    }

    public void logout() {
        this.token = null;  // Limpiar el token cuando el usuario cierre sesión
    }
}