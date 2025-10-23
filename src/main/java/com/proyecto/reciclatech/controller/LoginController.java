package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.model.Usuario;
import com.proyecto.reciclatech.service.UsuarioService;
import com.proyecto.reciclatech.session.Session;
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
        String carnet = txtCarnet.getText().trim();
        String password = txtContraseña.getText().trim();

        if (carnet.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Debe llenar todos los campos.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        if (usuarioService.validarLogin(carnet, password)) {
            Usuario usuario = usuarioService.buscarPorCarnet(carnet);

            // Guardar usuario en sesión para mantenerlo mientras cambian de ventana
            Session.getInstancia().setUsuario(usuario);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/BasuraView.fxml"));
                Parent root = loader.load();

                // No es necesario pasar el usuario manualmente, BasuraController lo obtiene de Session
                Stage stage = (Stage) btnConfirmar.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Clasificador ReciclaTech");
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                lblMensaje.setText("Error al cargar la ventana ReciclaTech.");
            }
        } else {
            lblMensaje.setText("Datos incorrectos");
            lblMensaje.setStyle("-fx-text-fill: red;");
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
