package com.gameaffinity.controller;

import com.gameaffinity.service.UserServiceAPI;

// LoginController
public class LoginController {

    private final UserServiceAPI userServiceAPI;

    public LoginController() {
        this.userServiceAPI = new UserServiceAPI();
    }

    public String login(String email, String password) {
        userServiceAPI.login(email, password);
        return userServiceAPI.getRole();
    }
}
