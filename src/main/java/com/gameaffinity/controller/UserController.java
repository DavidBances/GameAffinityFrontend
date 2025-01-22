package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserServiceAPI;

public class UserController {

    private final UserServiceAPI userServiceAPI;

    public UserController() {
        this.userServiceAPI = new UserServiceAPI();
    }

    public UserBase authenticate(String email, String password) {
        return userServiceAPI.authenticate(email, password);
    }

    public boolean updateProfile(String email, String password, String newName, String newEmail, String newPassword) {
        return userServiceAPI.updateProfile(email, password, newName, newEmail, newPassword);
    }
}
