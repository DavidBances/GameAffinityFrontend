package com.gameaffinity.view;

import com.gameaffinity.controller.GameManagementController;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GameDatabaseView {

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
    private GameManagementController gameManagementController;
    @Autowired
    private LibraryController libraryController;

    private final Map<String, Button> gameButtonMap = new HashMap<>();


    @FXML
    public void initialize() {
        loadGameImages(gameManagementController.getAllGames());
        refreshGameDatabase();
        loadGenres();

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

    public void loadLibraryState() {
        List<Game> gamesInLibrary = libraryController.getAllGamesByUser();
        List<Game> gamesInDataBase = gameManagementController.getAllGames();

        if (gamesInLibrary == null) {
            gamesInLibrary = new ArrayList<>();
        }

        // Actualiza el estado de los botones basándote en los juegos en la biblioteca
        for (Game game : gamesInLibrary) {
            for (Game gameInList : gamesInDataBase) {
                if (gameInList.getName().equalsIgnoreCase(game.getName())) {
                    Button gameButton = gameButtonMap.get(game.getName()); // Obtener el botón del mapa
                    if (gameButton != null) {
                        gameButton.setText("✔");
                        gameButton.getStyleClass().remove("not-added");
                        gameButton.getStyleClass().add("added");
                    }
                }
            }
        }
    }


    private void loadGameImages(List<Game> games) {
        imageContainer.getChildren().clear();
        gameButtonMap.clear();  // Limpiamos el mapa al cargar las imágenes

        if (games == null) {
            return;
        }

        for (Game game : games) {
            if (game.getImageUrl() == null) {
                continue;
            }

            // Crear ImageView para el juego
            ImageView imageView = new ImageView(new Image(game.getImageUrl(), true));
            imageView.setPreserveRatio(false);
            imageView.setFitWidth(200);
            imageView.setFitHeight(300);
            imageView.setSmooth(true);

            // Crear botón superpuesto
            Button addButton = new Button("+");
            addButton.getStyleClass().add("addButton");
            addButton.getStyleClass().add("not-added");
            addButton.setUserData(game.getName()); // Asocia el nombre del juego al botón
            addButton.setOnAction(e -> addGameToLibrary((String) addButton.getUserData(), addButton));

            // Guardar el botón en el mapa
            gameButtonMap.put(game.getName(), addButton);

            // Crear cuadro de número en la parte inferior derecha
            int score = libraryController.getMeanGameScore(game.getId());
            Label numberLabel = new Label(score + "");  // Puedes cambiar el número inicial
            numberLabel.getStyleClass().add("number-box");
            StackPane.setAlignment(numberLabel, Pos.BOTTOM_RIGHT);  // Lo coloca abajo a la derecha
            StackPane.setMargin(numberLabel, new Insets(0, 5, 5, 0)); // Pequeño margen para no pegarlo demasiado a los bordes

            double price = game.getPrice();
            Label priceLabel = new Label(price == 0 ? "FREE" : price + "");  // Puedes cambiar el número inicial
            priceLabel.getStyleClass().add("price-box");
            StackPane.setAlignment(priceLabel, Pos.BOTTOM_LEFT);  // Lo coloca abajo a la derecha
            StackPane.setMargin(priceLabel, new Insets(0, 0, 5, 5)); // Pequeño margen para no pegarlo demasiado a los bordes

            // Crear StackPane para apilar la imagen, el botón y el número
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("image-container");
            stackPane.getChildren().addAll(imageView, addButton, numberLabel, priceLabel);

            // Añadir StackPane al FlowPane
            imageContainer.getChildren().add(stackPane);
        }
        loadLibraryState();
    }


    private void loadGenres() {
        genreComboBox.getItems().clear();
        genreComboBox.getItems().add("All");
        genreComboBox.getItems().addAll(libraryController.getAllGenres());
        genreComboBox.getSelectionModel().selectFirst();
    }

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

    private void addGameToLibrary(String gameName, Button addButton) {
        if (!addButton.getStyleClass().contains("added")) {
            addButton.setText("✔");
            addButton.getStyleClass().remove("not-added");
            addButton.getStyleClass().add("added");
            // Lógica para añadir el juego a la biblioteca
            if (libraryController.addGameToLibrary(gameName)) {
                System.out.println("El juego se ha añadido a la biblioteca.");
            } else {
                System.out.println("El juego ya está en la biblioteca.");
            }
        } else {
            addButton.setText("+");
            addButton.getStyleClass().remove("added");
            addButton.getStyleClass().add("not-added");
            if (libraryController.removeGameFromLibrary(gameName)) {
                System.out.println("El juego se ha eliminado de la biblioteca.");
            } else {
                System.out.println("Aun no tienes el juego en la biblioteca.");
            }
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
}
