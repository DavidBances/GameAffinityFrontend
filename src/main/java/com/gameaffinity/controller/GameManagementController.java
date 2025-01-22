package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementServiceAPI;

import java.util.List;

public class GameManagementController {

    private final GameManagementServiceAPI gameManagementServiceAPI;

    public GameManagementController() {
        this.gameManagementServiceAPI = new GameManagementServiceAPI();
    }

    public boolean addGame(String name, String genre, String priceText) {
        return gameManagementServiceAPI.addGame(name, genre, priceText);
    }

    public List<Game> getAllGames() {
        return gameManagementServiceAPI.getAllGames();
    }

    public boolean deleteGame(Game game) {
        return gameManagementServiceAPI.deleteGame(game.getId());
    }
}
