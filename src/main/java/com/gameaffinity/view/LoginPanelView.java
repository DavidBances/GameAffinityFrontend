package com.gameaffinity.view;

import com.gameaffinity.controller.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginPanelView {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    @Autowired
    private LoginController loginController;


    @FXML
    public void initialize() {
        loginButton.setOnAction(
                event -> login(emailField.getText().trim(), passwordField.getText().trim()));
        registerButton.setOnAction(event -> register());
    }

    /**
     * Authenticates a user using their email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
    public void login(String email, String password) {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            String role = loginController.login(email, password);
            if (role != null) {
                if ("ADMINISTRATOR".equalsIgnoreCase(role)) {
                    Parent adminDashboard = FXMLLoader.load(getClass().getResource("/fxml/admin/admin_dashboard.fxml"));
                    Scene adminScene = new Scene(adminDashboard);
                    currentStage.setScene(adminScene);
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/user_dashboard.fxml"));
                    Parent userDashboard = loader.load();

                    Scene userScene = new Scene(userDashboard);
                    currentStage.setScene(userScene);
                }
            } else {
                showAlert("Invalid credentials.", "Error", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("An error occurred.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public void register() {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            Parent registerPane = FXMLLoader.load(getClass().getResource("/fxml/auth/register_panel.fxml"));
            Scene registerScene = new Scene(registerPane);
            currentStage.setScene(registerScene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred.", "Error", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
