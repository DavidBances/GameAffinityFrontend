package com.gameaffinity.controller;

import com.gameaffinity.service.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// LoginController
@Controller
public class LoginController {

    private final UserServiceAPI userServiceAPI;

    @Autowired
    public LoginController(UserServiceAPI userServiceAPI) {
        this.userServiceAPI = userServiceAPI;
    }

    public String login(String email, String password) {
        userServiceAPI.login(email, password);
        return userServiceAPI.getRole();
    }
}
