package com.proyecto.reciclatech.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Historial {
    private String id;
    private String carnet;
    private String nombreResiduo;
    private String categoria;
    private String fecha;

    public Historial() {}

    public Historial(String carnet, String nombreResiduo, String categoria) {
        this.carnet = carnet;
        this.nombreResiduo = nombreResiduo;
        this.categoria = categoria;
        this.fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getNombreResiduo() { return nombreResiduo; }
    public void setNombreResiduo(String nombreResiduo) { this.nombreResiduo = nombreResiduo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
