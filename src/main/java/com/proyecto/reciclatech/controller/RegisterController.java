package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.model.Usuario;
import com.proyecto.reciclatech.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField txtCarnet;
    @FXML private ComboBox<String> cmbCarrera;
    @FXML private TextField txtContrasena;
    @FXML private Button btnRegistrarse;
    @FXML private Button btnCambio;
    @FXML private Label lblMensaje;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        // Opciones para la carrera
        cmbCarrera.getItems().addAll(
                "Ingeniería en Sistemas",
                "Ingeniería Industrial",
                "ingeniria civil arquitectonica",
                "ingenieria en mecatronica",
                "ingenieria en electronica",
                "Psicologia", "Antropologia",
                "ingenieria  quimica",
                "ingenieria en administracion de empresas"
        );
        lblMensaje.setText("");
    }



    @FXML
    private void handleRegister() {
        String carnet = txtCarnet.getText();
        String carrera = cmbCarrera.getValue();
        String password = txtContrasena.getText();

        if (carnet.isEmpty() || carrera == null || password.isEmpty()) {
            lblMensaje.setWrapText(true);
            lblMensaje.setText("Debe llenar todos los campos.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            txtCarnet.clear();
            return;
        }

        // *** NUEVA VALIDACIÓN ***
        if (!carnet.endsWith("@uvg.edu.gt")) {
            lblMensaje.setWrapText(true);
            lblMensaje.setText("El carnet debe terminar en @uvg.edu.gt");
            lblMensaje.setStyle("-fx-text-fill: red;");
            txtCarnet.clear();
            return;
        }

        if (password.length() < 5) {
            lblMensaje.setWrapText(true);
            lblMensaje.setText("La contraseña debe tener al menos 5 caracteres.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            txtContrasena.clear();
            return;
        }

        Usuario usuario = new Usuario(carnet, carrera, password);
        boolean creado = usuarioService.crearUsuario(usuario);

        if (creado) {
            lblMensaje.setWrapText(true);
            lblMensaje.setText("Usuario creado");
            lblMensaje.setStyle("-fx-text-fill: blue;");
            txtCarnet.clear();
            txtContrasena.clear();
            cmbCarrera.setValue(null);
        } else {
            lblMensaje.setWrapText(true);
            lblMensaje.setText("Error: el correo ya está registrado.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }



    @FXML
    private void irAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnRegistrarse.getScene().getWindow(); // obtener ventana actual
            stage.setScene(new Scene(root));
            stage.setTitle("Login ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de login.");
        }
    }



}