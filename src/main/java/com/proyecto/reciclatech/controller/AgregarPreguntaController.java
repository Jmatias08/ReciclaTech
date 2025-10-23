package com.proyecto.reciclatech.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proyecto.reciclatech.db.MongoConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AgregarPreguntaController {

    @FXML private TextArea txtPregunta;
    @FXML private TextField txtOpcion1;
    @FXML private TextField txtOpcion2;
    @FXML private TextField txtOpcion3;
    @FXML private TextField txtOpcion4;
    @FXML private Button btnCancelar;
    @FXML private ChoiceBox<String> choiceCorrecta;
    @FXML private ChoiceBox<Integer> choiceDificultad;
    @FXML private Label lblEstado;

    private final MongoDatabase db = MongoConnection.getDatabase("ReciclaTech");

    @FXML
    public void initialize() {
        choiceCorrecta.getItems().addAll("Opción 1", "Opción 2", "Opción 3", "Opción 4");
        choiceDificultad.getItems().addAll(1, 2, 3);
    }

    @FXML
    public void guardarPregunta() {
        String pregunta = txtPregunta.getText().trim();
        List<String> opciones = Arrays.asList(
                txtOpcion1.getText().trim(),
                txtOpcion2.getText().trim(),
                txtOpcion3.getText().trim(),
                txtOpcion4.getText().trim()
        );
        String correcta = choiceCorrecta.getValue();
        Integer dificultad = choiceDificultad.getValue();

        if (pregunta.isEmpty() || opciones.stream().anyMatch(String::isEmpty) || correcta == null || dificultad == null) {
            lblEstado.setText("Completa todos los campos antes de guardar.");
            lblEstado.setStyle("-fx-text-fill: red;");
            return;
        }

        String respuesta = switch (correcta) {
            case "Opción 1" -> opciones.get(0);
            case "Opción 2" -> opciones.get(1);
            case "Opción 3" -> opciones.get(2);
            default -> opciones.get(3);
        };

        MongoCollection<Document> preguntas = db.getCollection("preguntas");

        Document doc = new Document("texto", pregunta)
                .append("opciones", opciones)
                .append("respuestaCorrecta", respuesta)
                .append("dificultad", dificultad);

        preguntas.insertOne(doc);
        lblEstado.setText("✅ Pregunta guardada correctamente.");
        lblEstado.setStyle("-fx-text-fill: green;");

        limpiarCampos();
    }

    private void limpiarCampos() {
        txtPregunta.clear();
        txtOpcion1.clear();
        txtOpcion2.clear();
        txtOpcion3.clear();
        txtOpcion4.clear();
        choiceCorrecta.setValue(null);
        choiceDificultad.setValue(null);
    }

    @FXML
    private void irACuestionario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/CuestionarioView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cuestionario ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}