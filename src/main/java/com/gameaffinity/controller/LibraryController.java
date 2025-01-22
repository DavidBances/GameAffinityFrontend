package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.LibraryServiceAPI;

import java.util.List;

public class LibraryController {

    private final LibraryServiceAPI libraryServiceAPI;

    public LibraryController() {
        this.libraryServiceAPI = new LibraryServiceAPI();
    }

    public List<String> getAllGenres() {
        return libraryServiceAPI.getAllGenres();
    }

    public List<Game> getGamesByUserId(int id) {
        return libraryServiceAPI.getGamesByUserId(id);
    }

    public List<Game> getGamesByNameUser(int id, String name) {
        return libraryServiceAPI.getGamesByNameUser(id, name);
    }

    public List<Game> getGamesByGenreUser(int id, String genre) {
        return libraryServiceAPI.getGamesByGenreUser(id, genre);
    }

    public List<Game> getGamesByGenreAndNameUser(int id, String genre, String name) {
        return libraryServiceAPI.getGamesByGenreAndNameUser(id, genre, name);
    }

    public List<Game> getAllGames() {
        return libraryServiceAPI.getAllGames();
    }

    public boolean addGameToLibrary(int id, String name) {
        return libraryServiceAPI.addGameToLibrary(id, name);
    }

    public boolean updateGameState(int gameId, int userId, String newState) {
        return libraryServiceAPI.updateGameState(gameId, userId, newState);
    }

    public boolean updateGameScore(int id, int userId, Integer newScore) {
        return libraryServiceAPI.updateGameScore(id, userId, newScore);
    }

    public boolean removeGameFromLibrary(int userId, int gameId) {
        return libraryServiceAPI.removeGameFromLibrary(userId, gameId);
    }
}
