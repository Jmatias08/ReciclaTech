package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.model.Usuario;
import com.proyecto.reciclatech.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField txtCarnet;
    @FXML private ComboBox<String> cmbCarrera;
    @FXML private TextField txtContrasena;
    @FXML private Button btnRegistrarse;

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
                "ingenieria en quimica",
                "ingenieria en administracion de empresas"
        );
    }

    @FXML
    private void handleRegister() {
        String carnet = txtCarnet.getText();
        String carrera = cmbCarrera.getValue();
        String password = txtContrasena.getText();

        if (carnet.isEmpty() || carrera == null || password.isEmpty()) {
            System.out.println("[App] Debe llenar todos los campos.");
            return;
        }

        Usuario usuario = new Usuario(carnet, carrera, password);
        boolean creado = usuarioService.crearUsuario(usuario);

        if (creado) {
            System.out.println("[App] Usuario registrado correctamente.");
            // aquí podrías limpiar campos o ir a login.fxml
        } else {
            System.out.println("[App] Error: el carnet ya está registrado.");
        }
    }

    @FXML
    private void goToLogin() {
        System.out.println("[App] Ir a login");
        // cambiar de escena a login.fxml
    }
}