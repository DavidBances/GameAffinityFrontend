package com.gameaffinity.controller;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.FriendshipServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FriendshipController {

    private final FriendshipServiceAPI friendshipServiceAPI;

    @Autowired
    public FriendshipController(FriendshipServiceAPI friendshipServiceAPI) {
        this.friendshipServiceAPI = friendshipServiceAPI; // Inicializa el ApiService
    }

    public List<UserBase> getFriends() {
        return friendshipServiceAPI.getFriends(); // Llama al ApiService para obtener los amigos
    }

    public List<Friendship> getFriendRequests() {
        return friendshipServiceAPI.getFriendRequestsByToken(); // Llama al ApiService para obtener las solicitudes de amistad
    }

    public boolean respondToFriendRequest(Friendship friendship, String accepted) {
        return friendshipServiceAPI.respondToFriendRequest(friendship, accepted); // Llama al ApiService para responder a una solicitud
    }

    public String sendFriendRequest(String friendEmail) {
        return friendshipServiceAPI.sendFriendRequest(friendEmail); // Llama al ApiService para enviar una solicitud de amistad
    }

    public boolean deleteFriend(int friendId) {
        return friendshipServiceAPI.deleteFriend(friendId); // Llama al ApiService para eliminar un amigo
    }
}
