package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserServiceAPI;

public class LoginController {

    private final UserServiceAPI userServiceAPI;

    public LoginController() {
        this.userServiceAPI = new UserServiceAPI();
    }

    public UserBase login(String email, String password) {
        return userServiceAPI.login(email, password);
    }
}
