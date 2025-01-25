package com.gameaffinity.controller;

import com.gameaffinity.service.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController {

    private final UserServiceAPI userServiceAPI;

    @Autowired
    public AdminController(UserServiceAPI userServiceAPI){
        this.userServiceAPI = userServiceAPI;
    }

    public void logout(){
        userServiceAPI.logout();
    }
}
