package com.gameaffinity.view;

import com.gameaffinity.controller.GameManagementController;
import com.gameaffinity.model.Game;
import com.gameaffinity.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GameManagementView {

    @FXML
    public Button backButton;
    @FXML
    private TableView<Game> gameTable;
    @FXML
    private TableColumn<Game, String> nameColumn;
    @FXML
    private TableColumn<Game, String> genreColumn;
    @FXML
    private TableColumn<Game, Double> priceColumn;
    @FXML
    private Button addGameButton;
    @FXML
    private Button deleteGameButton;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private GameManagementController gameManagementController;

    @FXML
    public void initialize() {
        // Initialize table columns
        refreshGameTable();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        addGameButton.setOnAction(event -> {
            openGameFormDialog();
            refreshGameTable();
        });
        refreshGameTable();

        deleteGameButton.setOnAction(event -> {
            deleteGame(gameTable.getSelectionModel().getSelectedItem());
            refreshGameTable();
        });

        backButton.setOnAction(event -> back());

        refreshGameTable();
    }

    public void refreshGameTable() {
        List<Game> games = gameManagementController.getAllGames();
        if (games == null) {
            games = new ArrayList<>();
        }
        gameTable.setItems(FXCollections.observableArrayList(games));
    }

    public void openGameFormDialog() {
        try {
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/dialogs/game_form_dialog.fxml");
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Añadir Juego");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteGame(Game game) {
        if (game != null) {
            boolean confirmed = showConfirmationDialog("Are you sure you want to delete this game?");
            if (confirmed) {
                boolean deleted = gameManagementController.deleteGame(game);
                if (deleted) {
                    showAlert("Game deleted successfully!", "Éxito", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Failed to delete game.", "Error", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Please select a game to delete.", "Alerta", Alert.AlertType.WARNING);
        }
    }

    public void back() {
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/admin/admin_dashboard.fxml");
            Parent adminDashboard = loader.load();
            Scene adminScene = new Scene(adminDashboard);
            currentStage.setScene(adminScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String message) {
        // Mostrar un cuadro de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
