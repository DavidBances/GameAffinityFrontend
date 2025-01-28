package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;
import com.gameaffinity.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LibraryView {

    @FXML
    private FlowPane imageContainer;
    @FXML
    private Button backButton;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private Button filterButton;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private LibraryController libraryController;

    public void initialize() {
        loadGameImages(libraryController.getAllGamesByUser());
        refreshGamesList();
        loadGenres();

        filterButton.setOnAction(e -> {
            String selectedGenre = genreComboBox.getValue();
            String search = searchField.getText().trim();
            if ("All".equalsIgnoreCase(selectedGenre) && searchField.getText().trim().isEmpty()) {
                refreshGamesList();
            } else if ("All".equalsIgnoreCase(selectedGenre) && !searchField.getText().trim().isEmpty()) {
                refreshGamesListByName(search);
            } else if (!searchField.getText().trim().isEmpty()) {
                refreshGamesListByGenreAndName(selectedGenre, search);
            } else {
                refreshGamesListByGenre(selectedGenre);
            }
        });

        backButton.setOnAction(event -> back());
    }

    private void loadGameImages(List<Game> games) {
        imageContainer.getChildren().clear();
        if (games == null) {
            return;
        }
        for (Game game : games) {
            if (game.getImageUrl() == null) {
                continue;
            }
            ImageView imageView = new ImageView(new Image(game.getImageUrl(), true));
            imageView.setPreserveRatio(false);
            imageView.setFitWidth(200);
            imageView.setFitHeight(300);

            // Activar suavizado para mejorar la calidad visual
            imageView.setSmooth(true);

            imageContainer.getChildren().add(imageView);
        }
    }

    private void loadGenres() {
        genreComboBox.getItems().clear();
        genreComboBox.getItems().add("All");
        genreComboBox.getItems().addAll(libraryController.getAllGenres());
        genreComboBox.getSelectionModel().selectFirst();
    }

    private void refreshGamesList() {
        List<Game> games = libraryController.getAllGamesByUser();
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

    private void refreshGamesListByName(String name) {
        List<Game> games = libraryController.getGamesByNameUser(name);
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

    private void refreshGamesListByGenre(String genre) {
        List<Game> games = libraryController.getGamesByGenreUser(genre);
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

    private void refreshGamesListByGenreAndName(String genre, String name) {
        List<Game> games = libraryController.getGamesByGenreAndNameUser(genre, name);
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

    public void updateGameScore(Game game, Integer newScore) {
        if (newScore != null && newScore >= 0 && newScore <= 10) {
            boolean success = libraryController.updateGameScore(game.getId(), newScore);
            showAlert(success ? "Score updated successfully!" : "Failed to update score.", Alert.AlertType.INFORMATION);
            refreshGamesList();
        } else {
            showAlert("Invalid score. Please enter a value between 0 and 10.", Alert.AlertType.WARNING);
        }
    }

    public void updateGameState(Game game, String newState) {
        boolean success = libraryController.updateGameState(game.getId(), newState);
        showAlert(success ? "State updated successfully!" : "Failed to update state.", Alert.AlertType.INFORMATION);
        refreshGamesList();
    }


    private void addGame() {
        String gameName = showInputDialog("Enter Game Name to Add:");
        if (gameName != null && !gameName.isEmpty()) {
            try {
                boolean success = libraryController.addGameToLibrary(gameName);
                showAlert(success ? "Game added successfully!" : "Failed to add game.", Alert.AlertType.INFORMATION);
                refreshGamesList();
            } catch (Exception ex) {
                showAlert("Error adding game: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

//    private void removeGame() {
//        Game selectedGame = gamesTable.getSelectionModel().getSelectedItem();
//        if (selectedGame != null) {
//            try {
//                boolean success = libraryController.removeGameFromLibrary(selectedGame.getId());
//                showAlert(success ? "Game removed successfully!" : "Failed to remove game.", Alert.AlertType.INFORMATION);
//                refreshGamesList();
//            } catch (Exception ex) {
//                showAlert("Error removing game: " + ex.getMessage(), Alert.AlertType.ERROR);
//            }
//        } else {
//            showAlert("No game selected.", Alert.AlertType.WARNING);
//        }
//    }

    private void back() {
        try {
            Stage currentStage = (Stage) imageContainer.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/user/user_dashboard.fxml");
            Parent userDashboard = loader.load();

            Scene userScene = new Scene(userDashboard);
            currentStage.setScene(userScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(message);
        dialog.setTitle("Input");
        return dialog.showAndWait().orElse(null);
    }
}
