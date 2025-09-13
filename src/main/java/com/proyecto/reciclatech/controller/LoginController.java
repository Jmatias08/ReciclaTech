package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtCarnet;
    @FXML private PasswordField txtContraseña;
    @FXML private Button btnConfirmar;
    @FXML private Button btnRegister;
    @FXML private Label lblMensaje;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        lblMensaje.setText("");
    }

    @FXML
    private void handleLogin() {
        String carnet = txtCarnet.getText();
        String password = txtContraseña.getText();
        if (carnet.isEmpty() || password.isEmpty()) {
            lblMensaje.setWrapText(true);
            lblMensaje.setText("Debe llenar todos los campos.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            txtCarnet.clear();
            txtContraseña.clear();
            return;
        }

        boolean valido = usuarioService.validarLogin(carnet, password);
        if (valido) {
            System.out.println("[App] Login correcto. Bienvenido " + carnet);

            lblMensaje.setWrapText(true);
            lblMensaje.setText("Sesion iniciada");
            lblMensaje.setStyle("-fx-text-fill: blue;");
            txtCarnet.clear();
            txtContraseña.clear();
        } else {
            lblMensaje.setWrapText(true);
            lblMensaje.setText("Datos incorrectos");
            lblMensaje.setStyle("-fx-text-fill: red;");
            txtCarnet.clear();
            txtContraseña.clear();
        }
    }

    @FXML
    private void irAlRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/RegisterView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)  btnRegister.getScene().getWindow(); // obtener ventana actual
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de login.");
        }
    }
}
