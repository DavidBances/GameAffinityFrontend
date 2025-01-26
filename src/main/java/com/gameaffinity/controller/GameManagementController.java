package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameManagementController {

    private final GameManagementServiceAPI gameManagementServiceAPI;

    @Autowired
    public GameManagementController(GameManagementServiceAPI gameManagementServiceAPI) {
        this.gameManagementServiceAPI = gameManagementServiceAPI;
    }

    public boolean addGame(String name, String genre, String priceText) {
        return gameManagementServiceAPI.addGame(name, genre, priceText);
    }

    public List<Game> getAllGames() {
        return gameManagementServiceAPI.getAllGames();
    }

    public List<Game> getGamesByGenre(String genre) {
        return gameManagementServiceAPI.getGamesByGenre(genre);
    }

    public List<Game> getGamesByName(String name) {
        return gameManagementServiceAPI.getGamesByName(name);
    }

    public List<Game> getGamesByGenreAndName(String genre, String name) {
        return gameManagementServiceAPI.getGamesByGenreAndName(genre, name);
    }

    public boolean deleteGame(Game game) {
        return gameManagementServiceAPI.deleteGame(game.getId());
    }
}
