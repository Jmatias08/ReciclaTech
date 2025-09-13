package com.proyecto.reciclatech.app;

import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static MongoDatabase database;

    public static void main(String[] args) {
        // Inicializar conexión a MongoDB
        database = MongoConnection.getDatabase("ReciclaTech");
        if (database != null) {
            System.out.println("[MongoDB] Conexión exitosa a la base de datos.");
        } else {
            System.out.println("[MongoDB] No se pudo conectar a la base de datos.");
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar FXML desde resources
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/proyecto/reciclatech/view/LoginView.fxml")
            );

            if (loader.getLocation() == null) {
                throw new IllegalStateException("No se encontró el archivo vista.fxml en resources.");
            }

            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("ReciclaTech");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la interfaz gráfica.", e);
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
