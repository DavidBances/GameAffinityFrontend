package com.gameaffinity.view;

import com.gameaffinity.controller.GameManagementController;
import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.util.SpringFXMLLoader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
public class GameDatabaseView {

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private GameManagementController gameManagementController;
    @Autowired
    private LibraryController libraryController;

    @FXML
    private FlowPane imageContainer;
    @FXML
    private Button backButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private Button filterButton;

    @FXML
    public void initialize() {
        loadGameImages(gameManagementController.getAllGames());
        loadGenres();

        searchButton.setOnAction(e -> {
            refreshGameDatabaseByName(searchField.getText().trim());
        });
        filterButton.setOnAction(e -> {
            String selectedGenre = genreComboBox.getValue();
            String search = searchField.getText().trim();
            if ("All".equalsIgnoreCase(selectedGenre) && searchField.getText().trim().isEmpty()) {
                refreshGameDatabase();
            } else if ("All".equalsIgnoreCase(selectedGenre) && !searchField.getText().trim().isEmpty()) {
                refreshGameDatabaseByName(search);
            } else if (!searchField.getText().trim().isEmpty()) {
                refreshGameDatabaseByGenreAndName(selectedGenre, search);
            } else {
                refreshGameDatabaseByGenre(selectedGenre);
            }
        });

        backButton.setOnAction(event -> back());
    }

    private void loadGameImages(List<Game> games) {
        imageContainer.getChildren().clear();
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


//    private void configureTableColumns() {
//        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
//        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
//        priceColumn
//                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
//        scoreColumn.setCellValueFactory(cellData ->
//                new SimpleIntegerProperty(libraryController.getGameScore(cellData.getValue().getId())).asObject()
//        );
//        refreshGameDatabase();
//    }

    private void refreshGameDatabase() {
        List<Game> games = gameManagementController.getAllGames();
        System.out.println(games);
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

    private void refreshGameDatabaseByName(String keyword) {
        List<Game> games = gameManagementController.getGamesByName(keyword);
        System.out.println("Texto en el campo de búsqueda: " + keyword);  // Depuración
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

    private void refreshGameDatabaseByGenre(String genre) {
        List<Game> games = gameManagementController.getGamesByGenre(genre);
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

    private void refreshGameDatabaseByGenreAndName(String genre, String name) {
        List<Game> games = gameManagementController.getGamesByGenreAndName(genre, name);
        if (games == null) {
            games = new ArrayList<>();
        }
        loadGameImages(games);
    }

//    private void addGame() {
//        Game selectedGame = databaseTable.getSelectionModel().getSelectedItem();
//        if (selectedGame != null) {
//            try {
//                boolean success = libraryController.addGameToLibrary(selectedGame.getName());
//                showAlert(success ? "Game added to library!" : "Game already in library or not found.", success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
//            } catch (Exception ex) {
//                showAlert(ex.getMessage(), Alert.AlertType.ERROR);
//            }
//        } else {
//            showAlert("Please select a game to add.", Alert.AlertType.WARNING);
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
}
