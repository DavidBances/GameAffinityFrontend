package com.gameaffinity.controller;

import com.gameaffinity.service.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RegisterController {

    private final UserServiceAPI userServiceAPI;

    @Autowired
    public RegisterController(UserServiceAPI userServiceAPI) {
        this.userServiceAPI = userServiceAPI;
    }

    public String register(String name, String email, String password) {
        return userServiceAPI.register(name, email, password);
    }
}
