package com.gameaffinity.controller;

import com.gameaffinity.service.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final UserServiceAPI userServiceAPI;

    @Autowired
    public UserController(UserServiceAPI userServiceAPI) {
        this.userServiceAPI = userServiceAPI;
    }

    public boolean updateProfile(String newName, String newEmail, String newPassword) {
        return userServiceAPI.updateProfile(newName, newEmail, newPassword);
    }

    public void logout() {
        userServiceAPI.logout();
    }
}
