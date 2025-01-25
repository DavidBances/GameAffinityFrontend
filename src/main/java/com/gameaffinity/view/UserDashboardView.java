package com.gameaffinity.view;

import com.gameaffinity.controller.UserController;
import com.gameaffinity.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserDashboardView {

    @FXML
    private Button viewLibraryButton;
    @FXML
    private Button manageFriendsButton;
    @FXML
    private Button viewGameDatabaseButton;
    @FXML
    private Button modifyProfileButton;
    @FXML
    private Button logoutButton;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private UserController userController;

    public void initialize() {
        viewLibraryButton
                .setOnAction(e -> openLibraryView());
        manageFriendsButton.setOnAction(
                e -> openFriendshipView());
        viewGameDatabaseButton.setOnAction(
                e -> openGameDatabaseView());
        modifyProfileButton.setOnAction(e -> openModifyProfileDialog());
        logoutButton.setOnAction(e -> logout());
    }

    public void openLibraryView() {
        try {
            Stage currentStage = (Stage) viewLibraryButton.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/library/library_view.fxml");
            Parent libraryView = loader.load();

            Scene libraryViewScene = new Scene(libraryView);
            currentStage.setScene(libraryViewScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openFriendshipView() {
        try {
            Stage currentStage = (Stage) manageFriendsButton.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/friendship/friendship_view.fxml");
            Parent friendshipView = loader.load();

            Scene friendshipViewScene = new Scene(friendshipView);
            currentStage.setScene(friendshipViewScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openGameDatabaseView() {
        try {
            Stage currentStage = (Stage) viewGameDatabaseButton.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/gameDatabase/game_database_view.fxml");
            Parent gameDatabaseView = loader.load();

            Scene gameDatabaseViewScene = new Scene(gameDatabaseView);
            currentStage.setScene(gameDatabaseViewScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openModifyProfileDialog() {
        try {
            // Cargar el FXML
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/dialogs/modify_profile_dialog.fxml");
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Modificar Perfil");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            userController.logout();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/auth/login_panel.fxml");
            Parent loginPane = loader.load();
            Scene loginPaneScene = new Scene(loginPane);
            currentStage.setScene(loginPaneScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
