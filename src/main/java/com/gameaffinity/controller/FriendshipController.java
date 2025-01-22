package com.gameaffinity.controller;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.FriendshipServiceAPI;

import java.util.List;

public class FriendshipController {

    private final FriendshipServiceAPI friendshipServiceAPI;

    // Constructor
    public FriendshipController() {
        this.friendshipServiceAPI = new FriendshipServiceAPI(); // Inicializa el ApiService
    }

    public List<UserBase> getFriends(int userId) {
        return friendshipServiceAPI.getFriends(userId); // Llama al ApiService para obtener los amigos
    }

    public List<Friendship> getFriendRequests(int userId) {
        return friendshipServiceAPI.getFriendRequests(userId); // Llama al ApiService para obtener las solicitudes de amistad
    }

    public boolean respondToFriendRequest(Friendship friendship, String accepted) {
        return friendshipServiceAPI.respondToFriendRequest(friendship, accepted); // Llama al ApiService para responder a una solicitud
    }

    public int getUserIdByEmail(String email) {
        return friendshipServiceAPI.getUserIdByEmail(email); // Llama al ApiService para obtener el ID del usuario por correo
    }

    public boolean sendFriendRequest(Friendship newFriendship) {
        return friendshipServiceAPI.sendFriendRequest(newFriendship); // Llama al ApiService para enviar una solicitud de amistad
    }

    public boolean deleteFriend(int userId, int friendId) {
        return friendshipServiceAPI.deleteFriend(userId, friendId); // Llama al ApiService para eliminar un amigo
    }
}
