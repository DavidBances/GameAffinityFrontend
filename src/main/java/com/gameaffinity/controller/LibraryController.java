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

    public List<Game> getGamesByUserId() {
        return libraryServiceAPI.getAllGamesByUser();
    }

    public List<Game> getAllGamesByFriend(int friendId) {
        return libraryServiceAPI.getAllGamesByFriend(friendId);
    }

    public List<Game> getGamesByNameUser(String name) {
        return libraryServiceAPI.getGamesByNameUser(name);
    }

    public List<Game> getGamesByGenreUser(String genre) {
        return libraryServiceAPI.getGamesByGenreUser(genre);
    }

    public List<Game> getGamesByGenreAndNameUser(String genre, String name) {
        return libraryServiceAPI.getGamesByGenreAndNameUser(genre, name);
    }

    public List<Game> getAllGames() {
        return libraryServiceAPI.getAllGames();
    }

    public boolean addGameToLibrary(String name) {
        return libraryServiceAPI.addGameToLibrary(name);
    }

    public boolean updateGameState(int gameId, String newState) {
        return libraryServiceAPI.updateGameState(gameId, newState);
    }

    public boolean updateGameScore(int gameId, Integer newScore) {
        return libraryServiceAPI.updateGameScore(gameId, newScore);
    }

    public int getGameScore(int gameId) {
        return libraryServiceAPI.getGameScore(gameId);
    }

    public boolean removeGameFromLibrary(int gameId) {
        return libraryServiceAPI.removeGameFromLibrary(gameId);
    }
}
