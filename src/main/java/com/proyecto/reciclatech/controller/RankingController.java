package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.model.Puntaje;
import com.proyecto.reciclatech.service.PuntajeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class RankingController {

    @FXML private TableView<PuntajeFila> tablaRanking;
    @FXML private TableColumn<PuntajeFila, Integer> colPosicion;
    @FXML private TableColumn<PuntajeFila, String> colCarrera;
    @FXML private TableColumn<PuntajeFila, Integer> colPuntos;
    @FXML private Button btnVolver;

    private final PuntajeService puntajeService = new PuntajeService();

    @FXML
    public void initialize() {
        colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));

        cargarRanking();
    }

    private void cargarRanking() {
        List<Puntaje> rankingMongo = puntajeService.obtenerRanking();
        ObservableList<PuntajeFila> data = FXCollections.observableArrayList();

        int pos = 1;
        for (Puntaje p : rankingMongo) {
            data.add(new PuntajeFila(pos++, p.getCarrera(), p.getPuntajeTotal()));
        }

        tablaRanking.setItems(data);
    }


    public static class PuntajeFila {
        private int posicion;
        private String carrera;
        private int puntos;

        public PuntajeFila(int posicion, String carrera, int puntos) {
            this.posicion = posicion;
            this.carrera = carrera;
            this.puntos = puntos;
        }

        public int getPosicion() { return posicion; }
        public String getCarrera() { return carrera; }
        public int getPuntos() { return puntos; }
    }
    @FXML
    private void irABasura(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/BasuraView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tablaRanking.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Clasificador ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}