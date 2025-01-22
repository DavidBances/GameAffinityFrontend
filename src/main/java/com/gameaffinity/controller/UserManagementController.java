package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserServiceAPI;

import java.util.List;

public class UserManagementController {

    private final UserServiceAPI userServiceAPI;

    public UserManagementController() {
        this.userServiceAPI = new UserServiceAPI();
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
