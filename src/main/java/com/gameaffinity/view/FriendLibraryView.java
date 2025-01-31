package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;
import com.gameaffinity.model.UserBase;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FriendLibraryView {

    @FXML
    private FlowPane imageContainer;
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

    private UserBase user;

    public void setUser(UserBase user) {
        this.user = user;
        if (this.user != null) {
            loadGameImages(libraryController.getAllGamesByFriend(this.user.getId()));
            refreshGamesList();
        }
    }

    public void initialize() {
        loadGenres();

        filterButton.setOnAction(e -> {
            String selectedGenre = genreComboBox.getValue();
            String search = searchField.getText().trim();
            if ("All".equalsIgnoreCase(selectedGenre) && searchField.getText().trim().isEmpty()) {
                refreshGamesList();
            } else if ("All".equalsIgnoreCase(selectedGenre) && !searchField.getText().trim().isEmpty()) {
                //refreshGamesListByName(search);
            } else if (!searchField.getText().trim().isEmpty()) {
                //refreshGamesListByGenreAndName(selectedGenre, search);
            } else {
                //refreshGamesListByGenre(selectedGenre);
            }
        });
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
            Button addButton = new Button("+");
            addButton.getStyleClass().add("addButton");
            addButton.setUserData(game.getName()); // Asocia el nombre del juego al botón
            addButton.setOnAction(e -> addGameToLibrary((String) addButton.getUserData(), addButton));

            // Label del score (ahora editable)
            Label scoreLabel = new Label(game.getScore() + "");
            scoreLabel.getStyleClass().add("number-box");
            StackPane.setAlignment(scoreLabel, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(scoreLabel, new Insets(0, 5, 5, 0));

            // Botón de información en la esquina superior izquierda
            Button infoButton = new Button("ℹ");
            infoButton.getStyleClass().add("info-button");
            StackPane.setAlignment(infoButton, Pos.BOTTOM_LEFT);
            StackPane.setMargin(infoButton, new Insets(0, 0, 5, 5));

            // Acción del botón de información
            infoButton.setOnAction(e -> openGameInfoView(game));

            // Menú desplegable para el estado del juego
            Label statusLabel = new Label(game.getState());
            statusLabel.getStyleClass().add("status-label");
            StackPane.setAlignment(statusLabel, Pos.TOP_CENTER);
            StackPane.setMargin(statusLabel, new Insets(5, 0, 0, 0));

            // StackPane para contener todos los elementos
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("image-container");
            stackPane.getChildren().addAll(imageView, addButton, scoreLabel, infoButton, statusLabel);

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
        List<Game> games = libraryController.getAllGamesByFriend(this.user.getId());
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

//    private void refreshGamesListByName(String name) {
//        List<Game> games = libraryController.getGamesByNameUser(name);
//        if (games == null) {
//            games = new ArrayList<>();
//        }
//        loadGameImages(games);
//    }
//
//    private void refreshGamesListByGenre(String genre) {
//        List<Game> games = libraryController.getGamesByGenreUser(genre);
//        if (games == null) {
//            games = new ArrayList<>();
//        }
//        loadGameImages(games);
//    }
//
//    private void refreshGamesListByGenreAndName(String genre, String name) {
//        List<Game> games = libraryController.getGamesByGenreAndNameUser(genre, name);
//        if (games == null) {
//            games = new ArrayList<>();
//        }
//        loadGameImages(games);
//    }

    private void addGameToLibrary(String gameName, Button addButton) {
        if (libraryController.addGameToLibrary(gameName)) {
            addButton.setText("✔");
            addButton.getStyleClass().add("added");
            System.out.println("El juego se ha añadido a la biblioteca.");
        } else {
            System.out.println("El juego ya está en la biblioteca.");
        }
    }

    private void openGameInfoView(Game game) {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/game/game_info_view.fxml");
            Parent gameInfoView = loader.load();

            GameInfoView controller = loader.getController();
            controller.setGame(game);
            controller.disableForFriendView();

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
