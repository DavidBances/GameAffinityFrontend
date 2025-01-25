package com.gameaffinity.view;

import com.gameaffinity.config.AppConfig;
import com.gameaffinity.util.SpringFXMLLoader; // Importar SpringFXMLLoader
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainApp extends Application {

    private ApplicationContext context;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicializar el contexto de Spring manualmente
            context = new AnnotationConfigApplicationContext(AppConfig.class);

            // Obtener el SpringFXMLLoader desde el contexto de Spring
            SpringFXMLLoader springFXMLLoader = context.getBean(SpringFXMLLoader.class);

            // Cargar el archivo FXML de LoginPanel utilizando SpringFXMLLoader
            FXMLLoader loader = springFXMLLoader.loadFXML("/fxml/auth/login_panel.fxml");

            // Cargar el archivo FXML
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
