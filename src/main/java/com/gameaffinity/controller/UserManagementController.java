package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserManagementController {

    private final UserServiceAPI userServiceAPI;

    @Autowired
    public UserManagementController(UserServiceAPI userServiceAPI) {
        this.userServiceAPI = userServiceAPI;
    }

    public boolean updateUserRole(UserBase user, String newRole) {
        return userServiceAPI.updateUserRole(user, newRole);
    }

    public List<UserBase> getAllUsers() {
        return userServiceAPI.getAllUsers();
    }

    public boolean deleteUser(UserBase user) {
        return userServiceAPI.deleteUser(user);
    }
}
