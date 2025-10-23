package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.model.Basura;
import com.proyecto.reciclatech.model.Usuario;
import com.proyecto.reciclatech.service.BasuraService;
import com.proyecto.reciclatech.session.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.Normalizer;

public class BasuraController {

    @FXML private TextField txtNombreResiduo;
    @FXML private Button btnClasificar;
    @FXML private Button btnCuestionario;
    @FXML private Button btnRanking;
    @FXML private Button btnCambio;
    @FXML private Label lblResultado;
    @FXML private Label lblMensaje;

    private final BasuraService service = new BasuraService();
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        usuarioActual = Session.getInstancia().getUsuario();
        lblMensaje.setVisible(false);
        lblResultado.setVisible(false);
    }

    @FXML
    private void clasificarResiduo(ActionEvent event) {
        String nombre = txtNombreResiduo.getText().trim().replaceAll("\\s+", " ");

        // Validaciones
        if (nombre.isEmpty()) {
            lblResultado.setVisible(true);
            lblResultado.setText("Por favor, ingresa el nombre de un residuo.");
            return;
        }

        if (nombre.length() < 2) {
            lblResultado.setVisible(true);
            lblResultado.setText("El nombre es demasiado corto.");
            return;
        }

        // solo letras y espacios
        String nombreSinTildes = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        if (!nombreSinTildes.matches("[a-zA-Z ]+")) {
            lblResultado.setVisible(true);
            lblResultado.setText("Solo se permiten letras y espacios.");
            txtNombreResiduo.clear();
            return;
        }

        // Convertir a minusculas
        String nombreFinal = nombre.toLowerCase();

        // Buscar en MongoDB o IA
        Basura basura = service.obtenerBasura(nombreFinal);

        if (basura != null) {
            lblResultado.setVisible(true);
            lblResultado.setText("Categoría: " + basura.getCategoria());
        } else {
            lblResultado.setVisible(true);
            lblResultado.setText("No se pudo clasificar el residuo.");
        }

        txtNombreResiduo.clear();
    }

    private void irAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/LoginView.fxml"));
            Parent root = loader.load();

            // Limpiar sesión
            Session.getInstancia().setUsuario(null);

            Stage stage = (Stage) btnCambio.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de login.");
        }
    }

    @FXML
    private void irACuestionario() {
        if (usuarioActual == null) {
            lblMensaje.setVisible(true);
            lblMensaje.setText("Usuario no definido. Inicie sesión.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/CuestionarioView.fxml"));
            Parent root = loader.load();

            // Pasar datos al controlador
            CuestionarioController controlador = loader.getController();
            controlador.setDatosUsuario(usuarioActual.getCarnet(), usuarioActual.getCarrera());

            Stage stage = (Stage) btnCuestionario.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cuestionario ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de cuestionario.");
        }
    }

    @FXML
    private void irARanking() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/RankingView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRanking.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ranking ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de ranking.");
        }
    }
}