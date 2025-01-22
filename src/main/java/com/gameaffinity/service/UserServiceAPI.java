package com.gameaffinity.service;

import com.gameaffinity.model.UserBase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class UserServiceAPI {

    private static final String BASE_URL = "http://localhost:8080/api";
    private final RestTemplate restTemplate;

    public UserServiceAPI() {
        this.restTemplate = new RestTemplate();
    }

    public UserBase login(String email, String password) {
        String url = BASE_URL + "/login";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("email", email)
                .queryParam("password", password)
                .toUriString();
        ResponseEntity<UserBase> response = restTemplate.exchange(finalUrl, HttpMethod.POST, null, UserBase.class);
        return response.getBody();
    }

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


    public UserBase authenticate(String email, String password) {
        String url = BASE_URL + "/users/authenticate";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("email", email)
                .queryParam("password", password)
                .toUriString();
        ResponseEntity<UserBase> response = restTemplate.exchange(finalUrl, HttpMethod.POST, null, UserBase.class);
        return response.getBody();
    }

    public boolean updateProfile(String email, String password, String newName, String newEmail, String newPassword) {
        String url = BASE_URL + "/users/update-profile";
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("email", email)
                .queryParam("password", password)
                .queryParam("newName", newName != null ? newName : "")
                .queryParam("newEmail", newEmail != null ? newEmail : "")
                .queryParam("newPassword", newPassword != null ? newPassword : "")
                .toUriString();
        ResponseEntity<Boolean> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, null, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    public boolean updateUserRole(UserBase user, String newRole) {
        String url = BASE_URL + "/user-management/update-role?newRole={newRole}";
        HttpEntity<UserBase> entity = new HttpEntity<>(user);
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class, newRole);
        return response.getBody() != null && response.getBody();
    }

    public List<UserBase> getAllUsers() {
        String url = BASE_URL + "/user-management/all";
        ResponseEntity<List<UserBase>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<UserBase>>() {
                });
        return response.getBody();
    }

    public boolean deleteUser(UserBase user) {
        String url = BASE_URL + "/user-management/delete";
        HttpEntity<UserBase> entity = new HttpEntity<>(user);
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }
}
