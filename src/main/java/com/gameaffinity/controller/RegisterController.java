package com.gameaffinity.controller;

import com.gameaffinity.service.UserServiceAPI;

public class RegisterController {

    private final UserServiceAPI userServiceAPI;

    public RegisterController() {
        this.userServiceAPI = new UserServiceAPI();
    }

    public String register(String name, String email, String password) {
        return userServiceAPI.register(name, email, password);
    }
}
