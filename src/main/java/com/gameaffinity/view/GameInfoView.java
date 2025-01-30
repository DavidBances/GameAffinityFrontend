package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class GameInfoView {

    @FXML
    private ImageView imageView;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label gameName;
    @FXML
    private ComboBox<String> statusBox;
    @FXML
    private HBox starRating;

    @Autowired
    private LibraryController libraryController;

    private Game game;

    private List<ToggleButton> stars = new ArrayList<>();

    public void setGame(Game game) {
        this.game = game;
        if (this.game != null) {
            gameName.setText(this.game.getName());
            loadStates();
            loadGameImage(this.game);
            if (!stars.isEmpty()) {
                updateStarsFromGame(game.getScore());
            }
        }
    }

    @FXML
    public void initialize() {
        //descriptionArea.setText(this.game.getDescription());
        descriptionArea.setText("Aun por implementar.");

        stars.clear();

        // Obtener los botones dentro de la HBox
        for (Node node : starRating.getChildren()) {
            if (node instanceof ToggleButton) {
                ToggleButton star = (ToggleButton) node;
                stars.add(star);
                star.setOnAction(event -> updateStars(stars.indexOf(star)));
            }
        }

        statusBox.setOnAction(e -> updateGameState(game, statusBox.getValue()));
    }

    private void loadStates() {
        statusBox.getItems().clear();
        statusBox.setValue(game.getState()); // Usar el estado actual del juego
        statusBox.getItems().addAll("Jugando", "Completado", "Pendiente");
        statusBox.getStyleClass().add("status-box");
        statusBox.getSelectionModel().selectFirst();
    }

    // Método para actualizar solo la visualización de las estrellas al cargar un juego
    private void updateStarsFromGame(int score) {
        System.out.println(score);
        int selectedIndex = Math.max(0, score - 1); // Asegura que el índice sea válido
        System.out.println(selectedIndex);
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setSelected(i <= selectedIndex);
        }
    }

    // Método para actualizar la selección de estrellas
    private void updateStars(int selectedIndex) {
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setSelected(i <= selectedIndex);
        }
        int rating = selectedIndex + 1; // La puntuación es de 1 a 10
        updateGameScore(this.game, rating);
    }

    private void loadGameImage(Game game) {
        if (game == null) {
            return;
        }
        if (game.getImageUrl() == null) {
            return;
        }
        Image image = new Image(game.getImageUrl(), true);
        imageView.setImage(image);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(350);
        imageView.setFitHeight(500);

        // Activar suavizado para mejorar la calidad visual
        imageView.setSmooth(true);
    }

    public void updateGameScore(Game game, Integer newScore) {
        if (newScore != null && newScore >= 0 && newScore <= 10) {
            boolean success = libraryController.updateGameScore(game.getId(), newScore);
            showAlert(success ? "Score updated successfully!" : "Failed to update score.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Invalid score. Please enter a value between 0 and 10.", Alert.AlertType.WARNING);
        }
    }

    public void updateGameState(Game game, String newState) {
        boolean success = libraryController.updateGameState(game.getId(), newState);
        showAlert(success ? "State updated successfully!" : "Failed to update state.", Alert.AlertType.INFORMATION);
    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
