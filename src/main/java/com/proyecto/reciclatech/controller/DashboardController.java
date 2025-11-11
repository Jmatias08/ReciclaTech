package com.proyecto.reciclatech.controller;

import com.proyecto.reciclatech.model.Historial;
import com.proyecto.reciclatech.model.Session;
import com.proyecto.reciclatech.model.Usuario;
import com.proyecto.reciclatech.repository.HistorialRepository;
import com.proyecto.reciclatech.service.PuntosService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Button btnRegresar;
    @FXML private Label lblPuntos;
    @FXML private Label lblMensaje;
    @FXML private TableView<Historial> tablaHistorial;
    @FXML private TableColumn<Historial, String> colResiduo;
    @FXML private TableColumn<Historial, String> colCategoria;
    @FXML private TableColumn<Historial, String> colFecha;
    @FXML private BarChart<String, Number> graficoPersonal;
    @FXML private BarChart<String, Number> graficoGeneral;

    private final HistorialRepository historialRepo = new HistorialRepository();
    private final PuntosService puntosService = new PuntosService();

    @FXML
    public void initialize() {
        colResiduo.setCellValueFactory(new PropertyValueFactory<>("nombreResiduo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        cargarDatos();
    }

    private void cargarDatos() {
        Usuario usuarioActual = Session.getInstancia().getUsuario();

        // 🔹 Mostrar puntos personales
        int puntos = puntosService.obtenerPuntos(usuarioActual.getCarnet());
        lblPuntos.setText("Puntos: " + puntos);

        // 🔹 Historial personal
        List<Historial> historialPersonal = historialRepo.obtenerPorCarnet(usuarioActual.getCarnet());
        tablaHistorial.getItems().setAll(historialPersonal);

        // 🔹 Graficos de conteo
        List<Historial> todos = historialRepo.obtenerTodos();

        Map<String, Long> conteoGeneral = todos.stream()
                .collect(Collectors.groupingBy(Historial::getCategoria, Collectors.counting()));

        Map<String, Long> conteoPersonal = historialPersonal.stream()
                .collect(Collectors.groupingBy(Historial::getCategoria, Collectors.counting()));

        actualizarGrafico(graficoPersonal, conteoPersonal);
        actualizarGrafico(graficoGeneral, conteoGeneral);
    }

    private void actualizarGrafico(BarChart<String, Number> grafico, Map<String, Long> datos) {
        grafico.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        datos.forEach((categoria, cantidad) -> series.getData().add(new XYChart.Data<>(categoria, cantidad)));
        grafico.getData().add(series);
    }

    @FXML
    private void irABasura() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyecto/reciclatech/view/BasuraView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Estadisticas ReciclaTech");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al cargar la ventana de ranking.");
        }
    }
}