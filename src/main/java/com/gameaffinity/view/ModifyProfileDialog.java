package com.gameaffinity.view;

import com.gameaffinity.controller.UserController;
import com.gameaffinity.model.UserBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyProfileDialog {
    @FXML
    private TextField newNameField;
    @FXML
    private TextField newEmailField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;

    @Autowired
    private UserController userController;

    @FXML
    private void initialize() {

        updateButton.setOnAction(
                event -> {
                    updateProfile(newNameField.getText().trim(),
                            newEmailField.getText().trim(),
                            newPasswordField.getText().trim());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    stage.close();
                });

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Updates the user's profile with new information.
     *
     * @param newName     The new name for the user (optional, can be null).
     * @param newEmail    The new email address for the user (optional, can be null).
     * @param newPassword The new password for the user (optional, can be null).
     */
    public void updateProfile(String newName, String newEmail,
                              String newPassword) {

        try {
            boolean success = userController.updateProfile(newName, newEmail, newPassword);
            if (success) {
                showAlert("Perfil actualizado con éxito.", "Exito", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error al actualizar el perfil.", "Error", Alert.AlertType.ERROR);
            }
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage(), "Alerta", Alert.AlertType.WARNING);
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
}
