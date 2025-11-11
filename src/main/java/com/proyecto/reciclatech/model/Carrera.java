package com.proyecto.reciclatech.model;

public class Carrera {
    private String id;
    private String nombre;
    private int puntajeTotal;

    public Carrera() {}

    public Carrera(String nombre) {
        this.nombre = nombre;
        this.puntajeTotal = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPuntajeTotal() { return puntajeTotal; }
    public void setPuntajeTotal(int puntajeTotal) { this.puntajeTotal = puntajeTotal; }

    public void agregarPuntaje(int puntos) {
        this.puntajeTotal += puntos;
    }
}
