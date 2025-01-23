package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserServiceAPI;

public class UserController {

    private final UserServiceAPI userServiceAPI;

    public UserController() {
        this.userServiceAPI = new UserServiceAPI();
    }

//    public UserBase authenticate(String email, String password) {
//        return userServiceAPI.login(email, password);
//    }

    public boolean updateProfile(String password, String newName, String newEmail, String newPassword) {
        return userServiceAPI.updateProfile(password, newName, newEmail, newPassword);
    }
}
