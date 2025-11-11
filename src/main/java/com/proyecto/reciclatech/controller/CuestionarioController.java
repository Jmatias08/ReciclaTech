package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.model.Pregunta;
import com.proyecto.reciclatech.model.Usuario;
import com.proyecto.reciclatech.service.CuestionarioService;
import com.proyecto.reciclatech.model.Session;
import com.proyecto.reciclatech.service.PuntajeService;
import com.proyecto.reciclatech.service.PuntosService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class CuestionarioController {

    @FXML private Label lblPregunta, lblTiempo, lblPuntos, lblMensaje;
    @FXML private RadioButton opcion1, opcion2, opcion3, opcion4;
    @FXML private Button btnIniciar, btnAgregar, btnBasura, btnResponder;
    @FXML private ToggleGroup grupoOpciones;

    private CuestionarioService service = new CuestionarioService();
    private PuntajeService puntajeService = new PuntajeService();
    private PuntosService puntosService = new PuntosService();

    private List<Pregunta> preguntas;
    private int indiceActual = 0;
    private int puntaje = 0;
    private Timeline temporizador;
    private int tiempoRestante = 15;
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        opcion1.setDisable(true);
        opcion2.setDisable(true);
        opcion3.setDisable(true);
        opcion4.setDisable(true);
        btnResponder.setVisible(false);
        lblMensaje.setVisible(false);
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void setDatosUsuario(String carnet, String carrera) {
        if (usuarioActual == null) {
            usuarioActual = Session.getInstancia().getUsuario();
        }
    }

    @FXML
    private void iniciarCuestionario() {
        // Obtener usuario desde sesión si no se pasó
        if (usuarioActual == null) {
            usuarioActual = Session.getInstancia().getUsuario();

        }

        if (usuarioActual == null) {
            lblMensaje.setVisible(true);
            lblMensaje.setText("Error: usuario no definido.");
            return;
        }

        opcion1.setDisable(false);
        opcion2.setDisable(false);
        opcion3.setDisable(false);
        opcion4.setDisable(false);
        btnResponder.setVisible(true);
        btnIniciar.setVisible(false);
        btnBasura.setVisible(false);
        btnAgregar.setVisible(false);
        preguntas = service.obtenerPreguntasAleatorias(7);
        puntaje = 0;
        indiceActual = 0;
        lblPuntos.setText("Puntaje: " + puntaje);
        mostrarPregunta();
    }

    private void mostrarPregunta() {
        if (indiceActual >= preguntas.size()) {
            btnResponder.setVisible(false);
            btnIniciar.setVisible(true);
            btnBasura.setVisible(true);
            btnAgregar.setVisible(true);
            opcion1.setDisable(true);
            opcion2.setDisable(true);
            opcion3.setDisable(true);
            opcion4.setDisable(true);
            lblPregunta.setText("¡Cuestionario terminado!");
            lblPuntos.setText("Puntaje final: " + puntaje);

            puntajeService.agregarPuntaje(usuarioActual, puntaje);
            puntosService.sumarPuntos(usuarioActual.getCarnet(), puntaje);

            detenerTemporizador();
            return;
        }

        Pregunta p = preguntas.get(indiceActual);
        lblPregunta.setText(p.getTexto());
        List<String> opciones = p.getOpciones();
        opcion1.setText(opciones.get(0));
        opcion2.setText(opciones.get(1));
        opcion3.setText(opciones.get(2));
        opcion4.setText(opciones.get(3));
        grupoOpciones.selectToggle(null);

        tiempoRestante = 15;
        lblTiempo.setText("Tiempo: " + tiempoRestante);
        iniciarTemporizador(p);
    }

    private void iniciarTemporizador(Pregunta pregunta) {
        detenerTemporizador();
        temporizador = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tiempoRestante--;
            lblTiempo.setText("Tiempo: " + tiempoRestante);

            if (tiempoRestante <= 0) {
                verificarRespuesta(pregunta);
            }
        }));
        temporizador.setCycleCount(Timeline.INDEFINITE);
        temporizador.play();
    }

    private void detenerTemporizador() {
        if (temporizador != null) temporizador.stop();
    }

    @FXML
    private void responder() {
        if (indiceActual < preguntas.size()) {
            verificarRespuesta(preguntas.get(indiceActual));
        }
    }

    private void verificarRespuesta(Pregunta p) {
        detenerTemporizador();
        RadioButton seleccion = (RadioButton) grupoOpciones.getSelectedToggle();
        if (seleccion != null && seleccion.getText().equalsIgnoreCase(p.getRespuestaCorrecta())) {
            puntaje += 10 * p.getDificultad();
        }
        lblPuntos.setText("Puntaje: " + puntaje);
        indiceActual++;
        mostrarPregunta();
    }
    @FXML
    private void irAAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/AgregarPreguntaView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAgregar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Pregunta");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de Agregar.");
        }
    }

    @FXML
    private void irABasura() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/BasuraView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBasura.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Clasificador ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de Basura.");
        }
    }
}