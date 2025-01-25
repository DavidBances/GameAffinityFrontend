package com.gameaffinity.view;

import com.gameaffinity.controller.RegisterController;
import com.gameaffinity.util.SpringFXMLLoader;
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
import org.springframework.stereotype.Component;

@Component
public class RegisterPanelView {
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createButton;

    @FXML
    private Button backButton;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;
    @Autowired
    private RegisterController registerController;

    @FXML
    public void initialize() {
        createButton.setOnAction(
                event -> {
                    register(nameField.getText(), emailField.getText(),
                            passwordField.getText());
                    back((Stage) createButton.getScene().getWindow());
                });
        backButton.setOnAction(event -> back((Stage) backButton.getScene().getWindow()));
    }

    public void register(String name, String email, String password) {
        showAlert(registerController.register(name, email, password), "Resultado", Alert.AlertType.INFORMATION);
    }

    public void back(Stage currentStage) {
        try {
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/auth/login_panel.fxml");
            Parent login = loader.load();
            Scene loginScene = new Scene(login);
            currentStage.setScene(loginScene);
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
