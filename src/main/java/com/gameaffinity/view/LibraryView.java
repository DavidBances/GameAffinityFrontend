package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;
import com.gameaffinity.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
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

            // Crear botón superpuesto
            Button removeButton = new Button("❌");
            removeButton.getStyleClass().add("removeButton");
            removeButton.setUserData(game.getName()); // Asocia el nombre del juego al botón
            removeButton.setOnAction(e -> removeGame((String) removeButton.getUserData(), removeButton));

            // Label del score (ahora editable)
            Label scoreLabel = new Label(game.getScore() + "");
            scoreLabel.getStyleClass().add("number-box");
            StackPane.setAlignment(scoreLabel, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(scoreLabel, new Insets(0, 5, 5, 0));

            // Hacer que el score sea editable
            scoreLabel.setOnMouseClicked(event -> {
                String newScoreStr = showInputDialog("Enter new score (0-10):");
                try {
                    int newScore = Integer.parseInt(newScoreStr);
                    updateGameScore(game, newScore);
                } catch (NumberFormatException e) {
                    showAlert("Invalid input. Please enter a number between 0 and 10.", Alert.AlertType.WARNING);
                }
            });

            // Botón de información en la esquina superior izquierda
            Button infoButton = new Button("ℹ");
            infoButton.getStyleClass().add("info-button");
            StackPane.setAlignment(infoButton, Pos.BOTTOM_LEFT);
            StackPane.setMargin(infoButton, new Insets(0, 0, 5, 5));

            // Acción del botón de información
            infoButton.setOnAction(e -> openGameInfoView(game));

            // Menú desplegable para el estado del juego
            ComboBox<String> statusDropdown = new ComboBox<>();
            statusDropdown.getItems().addAll("Jugando", "Completado", "Pendiente");
            statusDropdown.setValue(game.getState()); // Usar el estado actual del juego
            statusDropdown.getStyleClass().add("status-dropdown");
            StackPane.setAlignment(statusDropdown, Pos.TOP_CENTER);
            StackPane.setMargin(statusDropdown, new Insets(5, 0, 0, 0));

            // Llamar a updateGameState() cuando el usuario cambie el estado
            statusDropdown.setOnAction(e -> updateGameState(game, statusDropdown.getValue()));

            // StackPane para contener todos los elementos
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("image-container");
            stackPane.getChildren().addAll(imageView, removeButton, scoreLabel, infoButton, statusDropdown);

            imageContainer.getChildren().add(stackPane);
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

    private void removeGame(String gameName, Button addButton) {
        if (libraryController.removeGameFromLibrary(gameName)) {
            showAlert("Game removed successfully!", Alert.AlertType.INFORMATION);
            refreshGamesList();
        } else {
            showAlert("Failed to remove game.", Alert.AlertType.ERROR);
        }
    }

    private void openGameInfoView(Game game) {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/game/game_info_view.fxml");
            Parent gameInfoView = loader.load();

            GameInfoView controller = loader.getController();
            controller.setGame(game);

            Scene gameInfoViewScene = new Scene(gameInfoView);
            newStage.setScene(gameInfoViewScene);

            newStage.setOnCloseRequest(event -> refreshGamesList());

            newStage.setTitle("Game Info - " + game.getName());
            newStage.show();
        } catch (Exception ex) {
            showAlert("Error loading game info: " + ex.getMessage(), Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

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
