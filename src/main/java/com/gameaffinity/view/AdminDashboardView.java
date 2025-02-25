package com.gameaffinity.view;

import com.gameaffinity.controller.AdminController;
import com.gameaffinity.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminDashboardView {

    @FXML
    public ImageView userManagementImage;
    @FXML
    public ImageView gameManagementImage;
    @FXML
    private StackPane mainContent;
    @FXML
    private Button logoutButton;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private AdminController adminController;

    @FXML
    public void initialize() {
        initializeImages();
        userManagementImage.setOnMouseClicked(
                event -> openUserManagementView());
        gameManagementImage.setOnMouseClicked(
                event -> openGamesManagementView());
        logoutButton.setOnAction(event -> logout());

    }

    private void initializeImages() {
        Image userManagementImg = new Image(getClass().getResource("/images/admin/userManagement.png").toExternalForm());
        userManagementImage.setImage(userManagementImg);

        Image gameManagementImg = new Image(getClass().getResource("/images/admin/gameManagement.png").toExternalForm());
        gameManagementImage.setImage(gameManagementImg);
    }

    public void openUserManagementView() {
        try {
            Stage currentStage = (Stage) userManagementImage.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/admin/user_management.fxml");
            Parent userManagement = loader.load();
            Scene userManagementScene = new Scene(userManagement);
            currentStage.setScene(userManagementScene);
        } catch (Exception e) {
            showAlert("Error.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openGamesManagementView() {
        try {
            Stage currentStage = (Stage) gameManagementImage.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/admin/game_management.fxml");
            Parent gameManagement = loader.load();
            Scene gameManagementScene = new Scene(gameManagement);
            currentStage.setScene(gameManagementScene);
        } catch (Exception e) {
            showAlert("Error.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            adminController.logout();
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/auth/login_panel.fxml");
            Parent login = loader.load();
            Scene loginScene = new Scene(login);
            currentStage.setScene(loginScene);
        } catch (Exception e) {
            showAlert("Error.", "Error", Alert.AlertType.ERROR);
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
