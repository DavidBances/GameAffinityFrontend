package com.gameaffinity.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class MainApp extends Application {

    @Autowired
    private ApplicationContext context; // Para obtener el contexto de Spring y usar la inyección

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML de LoginPanel
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/login_panel.fxml"));
            loader.setControllerFactory(context::getBean); // Inyecta el controlador de Spring automáticamente
            AnchorPane root = loader.load();

            // Configurar la escena
            Scene scene = new Scene(root, 310, 210);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Game Affinity");

            // Mostrar la ventana
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
