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
        String token = userServiceAPI.login(email, password);
        System.out.println(token);
        if (token != null && !token.isEmpty()) {
            userServiceAPI.setToken(token);
        }
        return userServiceAPI.getRoleFromToken();
    }
}
