package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.LibraryServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LibraryController {

    private final LibraryServiceAPI libraryServiceAPI;

    @Autowired
    public LibraryController(LibraryServiceAPI libraryServiceAPI) {
        this.libraryServiceAPI = libraryServiceAPI;
    }

    public List<String> getAllGenres() {
        return libraryServiceAPI.getAllGenres();
    }

    public List<Game> getAllGamesByUser() {
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

    public boolean addGameToLibrary(String name) {
        return libraryServiceAPI.addGameToLibrary(name);
    }

    public boolean updateGameState(int gameId, String newState) {
        return libraryServiceAPI.updateGameState(gameId, newState);
    }

    public boolean updateGameScore(int gameId, Integer newScore) {
        return libraryServiceAPI.updateGameScore(gameId, newScore);
    }

    public boolean updateGameReview(int gameId, String review) {
        return libraryServiceAPI.updateGameReview(gameId, review);
    }

    public boolean updateTimePlayed(int gameId, Double timePlayed) {
        return libraryServiceAPI.updateTimePlayed(gameId, timePlayed);
    }

    public int getMeanGameScore(int gameId) {
        return libraryServiceAPI.getMeanGameScore(gameId);
    }

    public double getMeanGameTimePlayed(int gameId) {
        return libraryServiceAPI.getMeanTimePlayed(gameId);
    }

    public boolean removeGameFromLibrary(String gameName) {
        return libraryServiceAPI.removeGameFromLibrary(gameName);
    }
}
